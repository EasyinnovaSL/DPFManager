/**
 * <h1>HttpGetHandler.java</h1> <p> This program is free software: you can redistribute it
 * and/or modify it under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any later version; or,
 * at your choice, under the terms of the Mozilla Public License, v. 2.0. SPDX GPL-3.0+ or MPL-2.0+.
 * </p> <p> This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. See the GNU General Public License and the Mozilla Public License for more details. </p>
 * <p> You should have received a copy of the GNU General Public License and the Mozilla Public
 * License along with this program. If not, see <a href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a>
 * and at <a href="http://mozilla.org/MPL/2.0">http://mozilla.org/MPL/2.0</a> . </p> <p> NB: for the
 * © statement, include Easy Innova SL or other company/Person contributing the code. </p> <p> ©
 * 2015 Easy Innova, SL </p>
 *
 * @author Adria Llorens
 * @version 1.0
 * @since 23/7/2015
 */

package dpfmanager.shell.modules.server.get;

import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.context.DpfContext;
import dpfmanager.shell.modules.server.messages.PostMessage;
import dpfmanager.shell.modules.server.messages.StatusMessage;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.DiskAttribute;
import io.netty.handler.codec.http.multipart.DiskFileUpload;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedFile;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.io.RandomAccessFile;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.*;

import static io.netty.handler.codec.http.HttpResponseStatus.NOT_FOUND;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class HttpGetHandler extends SimpleChannelInboundHandler<HttpObject> {

  private DpfContext context;

  private HttpRequest request;
  private final StringBuilder responseContent = new StringBuilder();

  public static final String HTTP_DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss zzz";
  public static final String HTTP_DATE_GMT_TIMEZONE = "GMT";
  public static final int HTTP_CACHE_SECONDS = 60;

  static {
    // should delete file on exit (in normal exit)
    DiskAttribute.deleteOnExitTemporaryFile = true;
    DiskFileUpload.deleteOnExitTemporaryFile = true;
    // system temp directory
    DiskFileUpload.baseDirectory = null;
    DiskAttribute.baseDirectory = null;
  }

  public HttpGetHandler(DpfContext context) {
    this.context = context;
  }

  @Override
  public void channelInactive(ChannelHandlerContext ctx) throws Exception {
  }

  @Override
  public void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
    if (msg instanceof HttpRequest) {
      HttpRequest request = this.request = (HttpRequest) msg;
      URI uri = new URI(request.uri());
      if (request.method().equals(HttpMethod.GET)) {
        // New GET request
        String path = uri.getPath();
        tractReadGet(ctx, path);
        return;
      } else {
        sendError(ctx, HttpResponseStatus.METHOD_NOT_ALLOWED);
        return;
      }
    }
  }

  /**
   * Main functions
   */

  private void tractReadGet(ChannelHandlerContext ctx, String zipPath) {
    // Parse params
    String path = DPFManagerProperties.getReportsDir() + zipPath;
    if (!zipPath.endsWith(".zip")){
      String hash = zipPath.substring(1,zipPath.length());
      StatusMessage sm = (StatusMessage) context.sendAndWaitResponse(BasicConfig.MODULE_DATABASE, new PostMessage(PostMessage.Type.ASK, hash));
      path = sm.getFolder().substring(0, sm.getFolder().length()-1)+ ".zip";
    }

    // Send the zip report
    File file = new File(path);
    if (file.exists()) {
      try {
        RandomAccessFile raf = new RandomAccessFile(file, "r");

        long fileLength = raf.length();

        HttpResponse response = new DefaultHttpResponse(HTTP_1_1, OK);
        HttpUtil.setContentLength(response, fileLength);
        setContentTypeHeader(response, file);
        setDateAndCacheHeaders(response, file);
        response.headers().remove(HttpHeaderNames.CONNECTION);

        // Write the initial line and the header.
        ctx.write(response);

        // Write the content.
        ChannelFuture lastContentFuture;
        if (ctx.pipeline().get(SslHandler.class) == null) {
          ctx.write(new DefaultFileRegion(raf.getChannel(), 0, fileLength), ctx.newProgressivePromise());
          // Write the end marker.
          lastContentFuture = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
        } else {
          lastContentFuture = ctx.writeAndFlush(new HttpChunkedInput(new ChunkedFile(raf, 0, fileLength, 8192)), ctx.newProgressivePromise());
        }

        // Delete the zip after download?
        lastContentFuture.addListener(new GenericFutureListener() {
          @Override
          public void operationComplete(Future future) throws Exception {
//            file.delete();
          }
        });
        // Decide whether to close the connection or not.
        if (!HttpUtil.isKeepAlive(request)) {
          lastContentFuture.addListener(ChannelFutureListener.CLOSE);
        }
      } catch (Exception ignore) {
        sendError(ctx, NOT_FOUND);
      }
    } else {
      // No exists
      sendError(ctx, NOT_FOUND);
    }
  }

  private void setContentTypeHeader(HttpResponse response, File file) {
    MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
    response.headers().set(HttpHeaderNames.CONTENT_TYPE, mimeTypesMap.getContentType(file.getPath()));
    response.headers().set(HttpHeaderNames.CONTENT_DISPOSITION, "attachment; filename=report.zip");
  }

  private void setDateAndCacheHeaders(HttpResponse response, File fileToCache) {
    SimpleDateFormat dateFormatter = new SimpleDateFormat(HTTP_DATE_FORMAT, Locale.US);
    dateFormatter.setTimeZone(TimeZone.getTimeZone(HTTP_DATE_GMT_TIMEZONE));

    // Date header
    Calendar time = new GregorianCalendar();
    response.headers().set(HttpHeaderNames.DATE, dateFormatter.format(time.getTime()));

    // Add cache headers
    time.add(Calendar.SECOND, HTTP_CACHE_SECONDS);
    response.headers().set(HttpHeaderNames.EXPIRES, dateFormatter.format(time.getTime()));
    response.headers().set(HttpHeaderNames.CACHE_CONTROL, "private, max-age=" + HTTP_CACHE_SECONDS);
    response.headers().set(HttpHeaderNames.LAST_MODIFIED, dateFormatter.format(new Date(fileToCache.lastModified())));
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
