package com.codestepfish.vline.tcp;

import cn.hutool.core.thread.ThreadUtil;
import com.codestepfish.vline.core.Node;
import com.codestepfish.vline.tcp.handler.TcpHandler;
import io.netty.channel.ChannelFuture;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;

@Getter
@Setter
@Slf4j
@NoArgsConstructor
@Accessors(chain = true)
public class TcpNode<T> extends Node<T> {
    @Override
    public void init() {
        super.init();
        ThreadUtil.execute(() -> TcpHandler.start(this));
    }

    @Override
    public void destroy() {
        super.destroy();
    }

    @Override
    public void sendMsg(T msg) {
        ChannelFuture future = TcpHandler.CHANNEL_FUTURES.get(this.getName());
        if (ObjectUtils.isEmpty(future)) {
            log.warn("tcp node offline ... : {}  msg not send: {} ", this.getName(), msg);
            return;
        }

        future.channel().writeAndFlush(msg);  // send 失败不处理...
    }
}
