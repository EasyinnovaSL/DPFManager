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
package dpfmanager.shell.modules.client.upload;

import dpfmanager.shell.core.context.DpfContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.stream.ChunkedWriteHandler;

import java.io.File;

public class HttpClientIntializer extends ChannelInitializer<SocketChannel> {

  private DpfContext context;
  private SslContext sslCtx;
  private File config;

  public HttpClientIntializer(DpfContext context, SslContext sslCtx, File config) {
    this.sslCtx = sslCtx;
    this.context = context;
    this.config = config;
  }

  @Override
  public void initChannel(SocketChannel ch) {
    ChannelPipeline pipeline = ch.pipeline();
    if (sslCtx != null) {
      pipeline.addLast("ssl", sslCtx.newHandler(ch.alloc()));
    }

    pipeline.addLast(new HttpClientCodec());
    pipeline.addLast(new ChunkedWriteHandler());
    pipeline.addLast(new HttpClientHandler(context, config));
  }
}
