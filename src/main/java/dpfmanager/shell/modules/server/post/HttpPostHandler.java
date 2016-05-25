/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package dpfmanager.shell.modules.server.post;

import static io.netty.buffer.Unpooled.copiedBuffer;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.context.DpfContext;
import dpfmanager.shell.modules.server.messages.PostMessage;
import dpfmanager.shell.modules.server.messages.StatusMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.cookie.ServerCookieDecoder;
import io.netty.handler.codec.http.cookie.ServerCookieEncoder;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.DiskAttribute;
import io.netty.handler.codec.http.multipart.DiskFileUpload;
import io.netty.handler.codec.http.multipart.FileUpload;
import io.netty.handler.codec.http.multipart.HttpData;
import io.netty.handler.codec.http.multipart.HttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder.ErrorDataDecoderException;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.InterfaceHttpData.HttpDataType;
import io.netty.util.CharsetUtil;

import org.apache.commons.io.IOUtils;
import org.apache.tools.zip.ZipEntry;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.Set;
import java.util.zip.ZipOutputStream;

public class HttpPostHandler extends SimpleChannelInboundHandler<HttpObject> {

  private final HttpDataFactory factory = new DefaultHttpDataFactory(DefaultHttpDataFactory.MINSIZE);

  private DpfContext context;

  private HttpRequest request;
  private HttpData partialContent;
  private final StringBuilder responseContent = new StringBuilder();
  private HttpPostRequestDecoder decoder;

  // Request params
  private Long uuid;
  private Long id;
  private String filepath;
  private String configpath;
  private File destFolder;

  static {
    // should delete file on exit (in normal exit)
    DiskAttribute.deleteOnExitTemporaryFile = true;
    DiskFileUpload.deleteOnExitTemporaryFile = true;
    // system temp directory
    DiskFileUpload.baseDirectory = null;
    DiskAttribute.baseDirectory = null;
  }

  public HttpPostHandler(DpfContext context) {
    this.context = context;
  }

  @Override
  public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    if (decoder != null) {
      decoder.cleanFiles();
    }
  }

  @Override
  public void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
    if (msg instanceof HttpRequest) {
      HttpRequest request = this.request = (HttpRequest) msg;
      URI uri = new URI(request.uri());
      if (uri.getPath().startsWith("/dpfmanager")) {
        if (request.method().equals(HttpMethod.POST)) {
          // Start new POST request
          init();
          decoder = new HttpPostRequestDecoder(factory, request);
        } else {
          sendError(ctx, HttpResponseStatus.METHOD_NOT_ALLOWED);
          return;
        }
      } else {
        // Wrong URI
        responseContent.append("Wrong request uri!");
        writeResponse(ctx.channel());
        return;
      }
    }

    // POST request
    if (decoder != null) {
      if (msg instanceof HttpContent) {
        // New chunk is received
        try {
          HttpContent chunk = (HttpContent) msg;
          decoder.offer(chunk);
          readHttpDataChunkByChunk();
          if (chunk instanceof LastHttpContent) {
            tractReadPost(ctx);
            reset();
            return;
          }
        } catch (ErrorDataDecoderException e1) {
          e1.printStackTrace();
          ctx.channel().close();
          return;
        }
      }
    }
  }

  private void init() {
    // Init new request
    uuid = System.currentTimeMillis();
    id = null;
    filepath = null;
    configpath = null;
  }

  private void reset() {
    // End of request, destroy decoder
    decoder.destroy();
    decoder = null;
    request = null;
  }

  private void tractReadPost(ChannelHandlerContext ctx) {
    if (id != null) {
      askForJob(ctx);
    } else {
      newFileCheck(ctx);
    }
  }

  /**
   * Main methods
   */
  private void askForJob(ChannelHandlerContext ctx) {
    StatusMessage sm = (StatusMessage) context.sendAndWaitResponse(BasicConfig.MODULE_DATABASE, new PostMessage(PostMessage.Type.ASK, id));
    responseContent.append("{ \"status\": \"");
    if (sm.isRunning()) {
      responseContent.append("Running\"");
    } else {
      // Zip folder
      String path = zipFolder(sm.getFolder());
      String link = parsePathToLink(path);
      responseContent.append("Finished\" , \"path\": \"" + link + "\"");
    }
    responseContent.append("}");
    writeResponse(ctx.channel());
  }

  private void newFileCheck(ChannelHandlerContext ctx) {
    if (configpath == null) {
      // Error miss config file
      responseContent.append("{ \"error\": \"Missing config file (name = config)\"}");
      writeResponse(ctx.channel());
    } else if (filepath == null) {
      // Error miss file to check
      responseContent.append("{ \"error\": \"Missing file to check\"}");
      writeResponse(ctx.channel());
    } else {
      // OK
      responseContent.append("{ \"id\": " + uuid + "}");
      writeResponse(ctx.channel());
      // now start the check
      context.send(BasicConfig.MODULE_SERVER, new PostMessage(PostMessage.Type.POST, uuid, filepath, configpath));
    }
  }

  /**
   * Zip functoins
   */
  public String parsePathToLink(String path) {
    String[] splited = path.split("/");
    String last = splited[splited.length - 1];
    String last2 = splited[splited.length - 2];
    return "/" + last2 + "/" + last;
  }

  public String zipFolder(String folder) {
    // Zip path
    String outputFile = folder + ".zip";
    if (folder.endsWith("/")) {
      outputFile = folder.substring(0, folder.length() - 1) + ".zip";
    }
    // Check if exists
    if (new File(outputFile).exists()) {
      return outputFile;
    }
    // Make the zip
    try {
      ZipOutputStream zipFile = new ZipOutputStream(new FileOutputStream(outputFile));
      compressDirectoryToZipfile(folder, folder, zipFile);
      IOUtils.closeQuietly(zipFile);
      return outputFile;
    } catch (Exception e) {
      return null;
    }
  }

  private void compressDirectoryToZipfile(String rootDir, String sourceDir, ZipOutputStream out) throws IOException, FileNotFoundException {
    for (File file : new File(sourceDir).listFiles()) {
      if (file.isDirectory()) {
        compressDirectoryToZipfile(rootDir, sourceDir + file.getName() + File.separator, out);
      } else {
        ZipEntry entry = new ZipEntry(sourceDir.replace(rootDir, "") + file.getName());
        out.putNextEntry(entry);

        FileInputStream in = new FileInputStream(sourceDir + file.getName());
        IOUtils.copy(in, out);
        IOUtils.closeQuietly(in);
      }
    }
  }

  /**
   * Example of reading request by chunk and getting values from chunk to chunk
   */
  private void readHttpDataChunkByChunk() {
    try {
      while (decoder.hasNext()) {
        InterfaceHttpData data = decoder.next();
        if (data != null) {
          if (partialContent == data) {
            partialContent = null;
          }
          try {
            if (data.getHttpDataType() == HttpDataType.Attribute) {
              parseAttributeData((Attribute) data);
            } else if (data.getHttpDataType() == HttpDataType.FileUpload) {
              parseFileUploadData((FileUpload) data);
            } else {
            }
          } catch (Exception e) {
            e.printStackTrace();
            return;
          } finally {
            data.release();
          }
        }
      }
    } catch (HttpPostRequestDecoder.EndOfDataDecoderException e1) {
      // End
    }
  }

  private void parseAttributeData(Attribute attribute) throws IOException {
    String name = attribute.getName();
    if (name.equals("id")) {
      id = Long.parseLong(attribute.getValue());
    }
  }

  private void parseFileUploadData(FileUpload fileUpload) throws IOException {
    if (fileUpload.isCompleted()) {
      destFolder = createNewDirectory(uuid);
      if (fileUpload.getName().equals("config")) {
        // Save config file
        File dest = new File(destFolder.getAbsolutePath() + "/config.dpf");
        configpath = dest.getAbsolutePath();
        fileUpload.renameTo(dest);
      } else {
        // Save file to check
        File dest = new File(destFolder.getAbsolutePath() + "/" + fileUpload.getFilename());
        filepath = dest.getAbsolutePath();
        fileUpload.renameTo(dest);
      }
    }
  }

  private File createNewDirectory(Long uuid) {
    if (destFolder != null) {
      return destFolder;
    }

    String serverDir = DPFManagerProperties.getServerDir();
    File folder = new File(serverDir + "/" + uuid);
    if (folder.exists()) {
      return null;
    }
    folder.mkdirs();
    return folder;
  }

  /**
   * Util functions
   */

  private void writeResponse(Channel channel) {
    // Convert the response content to a ChannelBuffer.
    ByteBuf buf = copiedBuffer(responseContent.toString(), CharsetUtil.UTF_8);
    responseContent.setLength(0);

    // Decide whether to close the connection or not.
    boolean close = request.headers().contains(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE, true)
        || request.protocolVersion().equals(HttpVersion.HTTP_1_0)
        && !request.headers().contains(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE, true);

    // Build the response object.
    FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf);
    response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");

    if (!close) {
      response.headers().setInt(HttpHeaderNames.CONTENT_LENGTH, buf.readableBytes());
    }

    Set<Cookie> cookies;
    String value = request.headers().get(HttpHeaderNames.COOKIE);
    if (value == null) {
      cookies = Collections.emptySet();
    } else {
      cookies = ServerCookieDecoder.STRICT.decode(value);
    }
    if (!cookies.isEmpty()) {
      // Reset the cookies if necessary.
      for (Cookie cookie : cookies) {
        response.headers().add(HttpHeaderNames.SET_COOKIE, ServerCookieEncoder.STRICT.encode(cookie));
      }
    }
    // Write the response.
    ChannelFuture future = channel.writeAndFlush(response);
    // Close the connection after the write operation is done if necessary.
    if (close) {
      future.addListener(ChannelFutureListener.CLOSE);
    }
  }

  private void sendError(ChannelHandlerContext ctx, HttpResponseStatus status) {
    FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, status, Unpooled.copiedBuffer("Failure: " + status + "\r\n", CharsetUtil.UTF_8));
    response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");
    ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    ctx.channel().close();
  }
}
