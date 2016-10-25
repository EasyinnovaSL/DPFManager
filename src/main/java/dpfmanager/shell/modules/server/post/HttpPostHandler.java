/**
 * <h1>HttpPostHandler.java</h1> <p> This program is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version; or, at your
 * choice, under the terms of the Mozilla Public License, v. 2.0. SPDX GPL-3.0+ or MPL-2.0+. </p>
 * <p> This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License and the Mozilla Public License for more details. </p> <p> You should
 * have received a copy of the GNU General Public License and the Mozilla Public License along with
 * this program. If not, see <a href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a>
 * and at <a href="http://mozilla.org/MPL/2.0">http://mozilla.org/MPL/2.0</a> . </p> <p> NB: for the
 * © statement, include Easy Innova SL or other company/Person contributing the code. </p> <p> ©
 * 2015 Easy Innova, SL </p>
 *
 * @author Adrià Llorens
 * @version 1.0
 * @since 23/7/2015
 */

package dpfmanager.shell.modules.server.post;

import static io.netty.buffer.Unpooled.copiedBuffer;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.context.DpfContext;
import dpfmanager.shell.modules.messages.messages.ExceptionMessage;
import dpfmanager.shell.modules.messages.messages.LogMessage;
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

import com.google.gson.Gson;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.net.util.Base64;
import org.apache.logging.log4j.Level;
import org.apache.tools.zip.ZipEntry;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.zip.ZipOutputStream;

public class HttpPostHandler extends SimpleChannelInboundHandler<HttpObject> {

  private final HttpDataFactory factory = new DefaultHttpDataFactory(DefaultHttpDataFactory.MINSIZE);

  private DpfContext context;
  private ResourceBundle bundle;

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
    bundle = DPFManagerProperties.getBundle();
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
      if (request.method().equals(HttpMethod.POST) || request.method().equals(HttpMethod.OPTIONS)) {
        // Start new POST request
        init();
        decoder = new HttpPostRequestDecoder(factory, request);
      } else {
        sendError(ctx, HttpResponseStatus.METHOD_NOT_ALLOWED);
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
    Map<String, String> map = new HashMap<>();
    map.put("type", "ASK");
    map.put("id", id.toString());
    if (sm.isRunning()) {
      map.put("status", "Running");
      map.put("processed", sm.getProcessed().toString());
      map.put("total", sm.getTotal().toString());
    } else if (sm.isFinished()) {
      // Zip folder
      String path = zipFolder(sm.getFolder());
      String link = parsePathToLink(path);
      map.put("status", "Finished");
      map.put("path", link);
      printOut("Finished check, sending to user...");
      printOut("  Uuid: " + id.toString());
      printOut("  Path: " + path);
    } else {
      map.put("status", "NotFound");
    }
    responseContent.append(new Gson().toJson(map));
    writeResponse(ctx.channel());
  }

  private void newFileCheck(ChannelHandlerContext ctx) {
    Map<String, String> map = new HashMap<>();
    map.put("type", "CHECK");
    map.put("id", uuid.toString());
    map.put("input", getName(filepath));
    if (configpath == null) {
      // Error miss config file
      map.put("myerror", bundle.getString("missingConfig"));
    } else if (filepath == null) {
      // Error miss file to check
      map.put("myerror", bundle.getString("missingFile"));
    }
    responseContent.append(new Gson().toJson(map));
    writeResponse(ctx.channel());

    // OK start the check
    if (!map.containsKey("myerror")) {
      context.send(BasicConfig.MODULE_SERVER, new PostMessage(PostMessage.Type.POST, uuid, filepath, configpath));
      printOut("");
      printOut("New file check received.");
      printOut("  Uuid: " + uuid);
    } else {
      deleteTmpFolder(uuid);
    }
  }

  private void deleteTmpFolder(Long uuid) {
    try {
      File folder = new File(DPFManagerProperties.getServerDir() + "/" + uuid);
      if (folder.exists() && folder.isDirectory()) {
        FileUtils.deleteDirectory(folder);
      }
    } catch (Exception e) {
      context.send(BasicConfig.MODULE_MESSAGE, new ExceptionMessage("Exception in remove server folder", e));
    }
  }

  private String getName(String path) {
    String ret = "";
    if (path != null) {
      if (path.contains(";")) {
        // List files
        String[] paths = path.split(";");
        for (String newPath : paths) {
          ret = ret + newPath.substring(newPath.lastIndexOf("\\") + 1, newPath.length()) + ", ";
        }
        ret = ret.substring(0, ret.length() - 1);
      } else {
        ret = path.substring(path.lastIndexOf("\\") + 1, path.length());
      }
    }
    return ret;
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
        compressDirectoryToZipfile(rootDir, sourceDir + file.getName() + "/", out);
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
    } else if (name.equals("config")) {
      destFolder = createNewDirectory(uuid);
      String encoded = attribute.getValue();
      String decoded = new String(Base64.decodeBase64(encoded), "UTF-8");
      File dest = new File(destFolder.getAbsolutePath() + "/config.dpf");
      configpath = dest.getAbsolutePath();
      FileUtils.writeStringToFile(dest, decoded);
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
        if (filepath == null) {
          filepath = dest.getAbsolutePath();
        } else {
          filepath = filepath + ";" + dest.getAbsolutePath();
        }
        fileUpload.renameTo(dest);
      }
    }
  }

  private File createNewDirectory(Long uuid) {
    String serverDir = DPFManagerProperties.getServerDir();
    File folder = new File(serverDir + "/" + uuid);
    if (folder.exists()) {
      return folder;
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

    // Extra headers
    response.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN, "*");

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

  private void printOut(String message) {
    context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, message));
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    ctx.channel().close();
  }
}
