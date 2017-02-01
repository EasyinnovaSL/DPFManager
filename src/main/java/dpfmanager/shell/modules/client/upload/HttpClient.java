/**
 * <h1>HttpClient.java</h1> <p> This program is free software: you can redistribute it
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

package dpfmanager.shell.modules.client.upload;

import dpfmanager.shell.core.context.DpfContext;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.DiskAttribute;
import io.netty.handler.codec.http.multipart.DiskFileUpload;
import io.netty.handler.codec.http.multipart.HttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestEncoder;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

import java.io.File;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public final class HttpClient {

  private DpfContext context;

  private URI uri;
  private EventLoopGroup group = new NioEventLoopGroup();
  private HttpDataFactory factory = new DefaultHttpDataFactory(DefaultHttpDataFactory.MINSIZE);
  private String host;
  private Integer port;
  private SslContext sslCtx;
  private boolean error;

  private List<File> files;
  private List<File> tmpFiles;
  private File config = null;
  private String id = null;
  private String path = null;

  public HttpClient(DpfContext context, String url) {
    this.context = context;
    files = new ArrayList<>();
    tmpFiles = new ArrayList<>();
    try {
      uri = new URI(url);
      String scheme = uri.getScheme() == null ? "http" : uri.getScheme();
      host = uri.getHost() == null ? "127.0.0.1" : uri.getHost();
      port = uri.getPort();
      if (port == -1) {
        if ("http".equalsIgnoreCase(scheme)) {
          port = 80;
        } else if ("https".equalsIgnoreCase(scheme)) {
          port = 443;
        }
      }

      final boolean ssl = "https".equalsIgnoreCase(scheme);
      if (ssl) {
        sslCtx = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build();
      } else {
        sslCtx = null;
      }

      // Configure the client.
      DiskFileUpload.deleteOnExitTemporaryFile = true;
      DiskFileUpload.baseDirectory = null;
      DiskAttribute.deleteOnExitTemporaryFile = true;
      DiskAttribute.baseDirectory = null;

      error = false;
    } catch (Exception e) {
      error = true;
    }
  }

  public boolean isError() {
    return error;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public void addFile(File file) {
    files.add(file);
  }

  public void addTmpFile(File file) {
    tmpFiles.add(file);
  }

  public void addConfig(File file) {
    config = file;
  }

  public void send() throws Exception {
    try {
      List<File> deletes = new ArrayList<>();
      deletes.addAll(tmpFiles);
      if (config != null){
        deletes.add(config);
      }
      Bootstrap b = new Bootstrap();
      b.group(group).channel(NioSocketChannel.class).handler(new HttpClientIntializer(context, sslCtx, deletes));
      formpostmultipart(b, host, port, uri, factory);
    } finally {
      group.shutdownGracefully();
      factory.cleanAllHttpData();
    }
  }

  /**
   * Multipart POST
   */
  private void formpostmultipart(Bootstrap bootstrap, String host, int port, URI uriFile, HttpDataFactory factory) throws Exception {
    // Start the connection attempt.
    ChannelFuture future = bootstrap.connect(new InetSocketAddress(host, port));
    Channel channel = future.sync().channel();

    // Prepare the HTTP request.
    HttpRequest request = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST, uriFile.toASCIIString());

    // Use the PostBody encoder
    HttpPostRequestEncoder bodyRequestEncoder = new HttpPostRequestEncoder(factory, request, true);

    HttpHeaders headers = request.headers();
    headers.set(HttpHeaderNames.HOST, host);
    headers.set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);

    // Add the files
    int i = 0;
    for (File file : files) {
      bodyRequestEncoder.addBodyFileUpload("file" + i, file, "image/tiff", false);
      i++;
    }
    for (File file : tmpFiles) {
      bodyRequestEncoder.addBodyFileUpload("file" + i, file, "image/tiff", false);
      i++;
    }

    // Add configuration
    if (config != null) {
      bodyRequestEncoder.addBodyFileUpload("config", config, "application/octet-stream", false);
    }
    // Add job id
    if (id != null) {
      bodyRequestEncoder.addBodyAttribute("id", id);
    }

    // finalize request
    bodyRequestEncoder.finalizeRequest();

    // send request
    channel.write(request);

    // test if request was chunked and if so, finish the write
    if (bodyRequestEncoder.isChunked()) {
      channel.write(bodyRequestEncoder);
    }
    channel.flush();

    // Now no more use of file representation (and list of HttpData)
    bodyRequestEncoder.cleanFiles();

    // Wait for the server to close the connection.
    channel.closeFuture().sync();
  }

}
