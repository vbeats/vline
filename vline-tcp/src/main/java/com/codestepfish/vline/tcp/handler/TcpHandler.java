package com.codestepfish.vline.tcp.handler;

import com.codestepfish.vline.core.tcp.TcpProperties;
import com.codestepfish.vline.tcp.TcpNode;
import com.codestepfish.vline.tcp.util.TcpHolder;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
public class TcpHandler {

    public static void init(TcpNode node) {
        switch (node.getTcp().getMode()) {
            case SERVER -> startServer(node);
            case CLIENT -> startClient(node);
        }
    }

    // tcp server
    private static void startServer(TcpNode node) {
        TcpProperties tp = node.getTcp();
        if (!StringUtils.hasText(tp.getChildHandler())) {
            log.warn("【{}】 Require Config ChildHandler.....", node.getName());
            return;
        }

        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();

        try {
            Class<? extends ChannelHandler> clazz = Objects.requireNonNull(ClassUtils.getDefaultClassLoader()).loadClass(tp.getChildHandler()).asSubclass(ChannelHandler.class);

            bootstrap.group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(clazz.getDeclaredConstructor().newInstance())
            ;

            ChannelFuture future = bootstrap.bind(tp.getHost(), tp.getPort()).sync();
            TcpHolder.CHANNEL_FUTURES.put(node.getName(), future);

            future.channel().closeFuture().sync();
        } catch (Exception e) {
            log.error("【{}】 Start Failed : ", node.getName(), e);
            throw new RuntimeException(e);
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

    // tcp client
    private static void startClient(TcpNode node) {
        TcpProperties tp = node.getTcp();
        if (!StringUtils.hasText(tp.getChildHandler())) {
            log.warn("【{}】 Require Config ChildHandler.....", node.getName());
            return;
        }

        EventLoopGroup worker = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();

        try {
            Class<? extends ChannelHandler> clazz = Objects.requireNonNull(ClassUtils.getDefaultClassLoader()).loadClass(tp.getChildHandler()).asSubclass(ChannelHandler.class);

            bootstrap.group(worker)
                    .channel(NioSocketChannel.class)
                    .handler(clazz.getDeclaredConstructor().newInstance());

            ChannelFuture future = bootstrap.connect(tp.getHost(), tp.getPort());
            TcpHolder.CHANNEL_FUTURES.put(node.getName(), future);


            future.addListener(f -> {
                if (f.isSuccess()) {
                    log.info("【{}】 Connect To {}:{} Success...", node.getName(), tp.getHost(), tp.getPort());

                    // 成功建立连接的channel future add 断开连接监听
                    future.channel().closeFuture().addListener(f2 -> {
                        log.warn("【{}】 Disconnect With {}:{} Reconnect After {} S...", node.getName(), tp.getHost(), tp.getPort(), tp.getReconnectDelay().toSeconds());
                        future.channel().eventLoop().schedule(() -> startClient(node), tp.getReconnectDelay().getSeconds(), TimeUnit.SECONDS);
                    });
                } else {
                    log.warn("【{}】 Disconnect With {}:{} Reconnect After {} S...", node.getName(), tp.getHost(), tp.getPort(), tp.getReconnectDelay().toSeconds());
                    future.channel().eventLoop().schedule(() -> startClient(node), tp.getReconnectDelay().getSeconds(), TimeUnit.SECONDS);
                }
            });
        } catch (Exception e) {
            log.error("【{}】 Connect Failed : ", node.getName(), e);
            throw new RuntimeException(e);
        }
    }

}
