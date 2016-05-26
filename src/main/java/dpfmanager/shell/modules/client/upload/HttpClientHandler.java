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

import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.context.DpfContext;
import dpfmanager.shell.modules.client.messages.ResponseMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.util.CharsetUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Handler that just dumps the contents of the response from the server
 */
public class HttpClientHandler extends SimpleChannelInboundHandler<HttpObject> {

  private DpfContext context;
  private String message;
  private File config;
  private boolean plain;
  private File file;
  private PrintWriter writer;

  public HttpClientHandler(DpfContext context, File config) {
    this.context = context;
    this.config = config;
    message = "";
  }

  @Override
  public void channelRead0(ChannelHandlerContext ctx, HttpObject msg) {
    if (msg instanceof HttpContent) {
      HttpContent chunk = (HttpContent) msg;
      String content = chunk.content().toString(CharsetUtil.UTF_8);
      message = message + content;
      if (msg instanceof LastHttpContent) {
        // End response content
        if (config != null && config.exists() && config.isFile()) {
          config.delete();
        }
        context.send(BasicConfig.MODULE_CLIENT, new ResponseMessage(message));
      }
    }
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    cause.printStackTrace();
    ctx.channel().close();
  }
}
