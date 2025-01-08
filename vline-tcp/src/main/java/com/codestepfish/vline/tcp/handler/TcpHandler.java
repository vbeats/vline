package com.codestepfish.vline.tcp.handler;

import com.codestepfish.vline.core.tcp.TcpProperties;
import com.codestepfish.vline.tcp.TcpNode;
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

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Slf4j
public class TcpHandler {

    // key node name
    public static final Map<String, ChannelFuture> CHANNEL_FUTURES = new ConcurrentHashMap<>(10);

    public static void start(TcpNode node) {
        switch (node.getTcp().getMode()) {
            case SERVER -> startServer(node);
            case CLIENT -> startClient(node);
        }
    }

    // tcp server
    private static void startServer(TcpNode node) {
        TcpProperties tp = node.getTcp();
        if (!StringUtils.hasText(tp.getChildHandler())) {
            log.warn("tcp node : {} not config childHandler.....", node.getName());
            return;
        }

        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();

        try {
            Class<ChannelHandler> clazz = (Class<ChannelHandler>) Objects.requireNonNull(ClassUtils.getDefaultClassLoader()).loadClass(tp.getChildHandler());

            bootstrap.group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(clazz.getDeclaredConstructor().newInstance())
            ;

            ChannelFuture future = bootstrap.bind(tp.getHost(), tp.getPort()).sync();
            CHANNEL_FUTURES.put(node.getName(), future);

            future.channel().closeFuture().sync();
        } catch (Exception e) {
            log.error("tcp server start failed : ", e);
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
            log.warn("tcp node not config childHandler.....: {}", node.getName());
            return;
        }

        EventLoopGroup worker = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();

        try {
            Class<ChannelHandler> clazz = (Class<ChannelHandler>) Objects.requireNonNull(ClassUtils.getDefaultClassLoader()).loadClass(tp.getChildHandler());

            bootstrap.group(worker)
                    .channel(NioSocketChannel.class)
                    .handler(clazz.getDeclaredConstructor().newInstance());

            ChannelFuture future = bootstrap.connect(tp.getHost(), tp.getPort());
            CHANNEL_FUTURES.put(node.getName(), future);


            future.addListener(f -> {
                if (f.isSuccess()) {
                    log.info("tcp client : {} connect to {}:{} success...", node.getName(), tp.getHost(), tp.getPort());

                    // 成功建立连接的channel future add 断开连接监听
                    future.channel().closeFuture().addListener(f2 -> {
                        log.warn("tcp client : {} disconnect with {}:{} reconnect after {} s ...", node.getName(), tp.getHost(), tp.getPort(), tp.getReconnectDelay().toSeconds());
                        future.channel().eventLoop().schedule(() -> startClient(node), tp.getReconnectDelay().getSeconds(), TimeUnit.SECONDS);
                    });
                } else {
                    log.warn("tcp client : {} disconnect with {}:{} reconnect after {} s ...", node.getName(), tp.getHost(), tp.getPort(), tp.getReconnectDelay().toSeconds());
                    future.channel().eventLoop().schedule(() -> startClient(node), tp.getReconnectDelay().getSeconds(), TimeUnit.SECONDS);
                }
            });
        } catch (Exception e) {
            log.error("tcp client : {} connect failed : ", node.getName(), e);
            throw new RuntimeException(e);
        }
    }

}
