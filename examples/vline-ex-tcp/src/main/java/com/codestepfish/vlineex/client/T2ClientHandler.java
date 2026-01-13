package com.codestepfish.vlineex.client;

import com.codestepfish.vline.core.VLineContext;
import com.codestepfish.vline.tcp.util.TcpHolder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class T2ClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.info("t2 receive: {}", msg);

        VLineContext.posMsg("t2", msg);

        TcpHolder.CLIENT_CHANNELS.get("t1").writeAndFlush(msg);

        // other business code
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("t2 exception: ", cause);
        ctx.close();
    }
}
