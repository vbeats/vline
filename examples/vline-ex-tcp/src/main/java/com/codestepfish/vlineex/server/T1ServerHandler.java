package com.codestepfish.vlineex.server;

import com.codestepfish.vline.core.VLineContext;
import com.codestepfish.vline.tcp.util.TcpHolder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class T1ServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("客户端 connected ... {}", ctx.channel().localAddress());
        TcpHolder.CLIENT_CHANNELS.put("t1", ctx.channel());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.info("t1 server reveive data: {}", msg);

        // data --> vline event bus
        VLineContext.posMsg("t1", msg);

        // other business code
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
        TcpHolder.CLIENT_CHANNELS.remove("t1");
    }
}
