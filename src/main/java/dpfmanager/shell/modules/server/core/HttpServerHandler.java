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
 * Created by Adrià Llorens on 25/05/2016.
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
    if (isPost(magic1, magic2)) {
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

  private boolean isGet(int magic1, int magic2) {
    return magic1 == 'G' && magic2 == 'E';
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    ctx.channel().close();
  }
}
