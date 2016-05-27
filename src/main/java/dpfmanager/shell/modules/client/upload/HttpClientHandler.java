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

import java.io.File;
import java.io.PrintWriter;
import java.util.List;

/**
 * Handler that just dumps the contents of the response from the server
 */
public class HttpClientHandler extends SimpleChannelInboundHandler<HttpObject> {

  private DpfContext context;
  private String message;
  private List<File> deletes;
  private boolean plain;
  private File file;
  private PrintWriter writer;

  public HttpClientHandler(DpfContext context, List<File> deletes) {
    this.context = context;
    this.deletes = deletes;
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
        for (File delete : deletes) {
          delete.delete();
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
