/**
 * <h1>HttpServerHandler.java</h1> <p> This program is free software: you can redistribute it
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

package dpfmanager.shell.modules.server.core;

import dpfmanager.shell.core.context.DpfContext;
import dpfmanager.shell.modules.server.get.HttpGetHandler;
import dpfmanager.shell.modules.server.post.HttpPostHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;

import java.util.List;

/**
 * Created by Adria Llorens on 25/05/2016.
 */
public class HttpServerHandler extends ByteToMessageDecoder {

  private DpfContext context;
  private HttpRequest request;
  private final StringBuilder responseContent = new StringBuilder();

  public HttpServerHandler(DpfContext context) {
    this.context = context;
  }

  @Override
  public void channelInactive(ChannelHandlerContext ctx) throws Exception {
  }

  @Override
  protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
    // Will use the first five bytes to detect a protocol.
    if (in.readableBytes() < 5) {
      in.clear();
      ctx.close();
      return;
    }

    final int magic1 = in.getUnsignedByte(in.readerIndex());
    final int magic2 = in.getUnsignedByte(in.readerIndex() + 1);
    if (isPost(magic1, magic2) || isOptions(magic1, magic2)) {
      // POST
      ChannelPipeline pipeline = ctx.pipeline();
      pipeline.addLast(new HttpRequestDecoder());
      pipeline.addLast(new HttpResponseEncoder());
      pipeline.addLast(new HttpPostHandler(context));
      pipeline.remove(this);
    } else if (isGet(magic1, magic2)) {
      // GET
      ChannelPipeline pipeline = ctx.pipeline();
      pipeline.addLast(new HttpServerCodec());
      pipeline.addLast(new HttpObjectAggregator(65536));
      pipeline.addLast(new ChunkedWriteHandler());
      pipeline.addLast(new HttpGetHandler(context));
      pipeline.remove(this);
    } else {
      in.clear();
      ctx.close();
    }
  }

  private boolean isPost(int magic1, int magic2) {
    return magic1 == 'P' && magic2 == 'O';
  }

  private boolean isOptions(int magic1, int magic2) {
    return magic1 == 'O' && magic2 == 'P';
  }

  private boolean isGet(int magic1, int magic2) {
    return magic1 == 'G' && magic2 == 'E';
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    ctx.channel().close();
  }
}
