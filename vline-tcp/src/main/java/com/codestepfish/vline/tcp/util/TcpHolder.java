package com.codestepfish.vline.tcp.util;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TcpHolder {

    // key node name
    public static final Map<String, ChannelFuture> CHANNEL_FUTURES = new ConcurrentHashMap<>(5);

    // key node name 客户端channel
    public static final Map<String, Channel> CLIENT_CHANNELS = new ConcurrentHashMap<>(5);
}
