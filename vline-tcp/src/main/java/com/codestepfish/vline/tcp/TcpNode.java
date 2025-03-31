package com.codestepfish.vline.tcp;

import cn.hutool.core.thread.ThreadUtil;
import com.codestepfish.vline.core.Node;
import com.codestepfish.vline.tcp.handler.TcpHandler;
import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Accessors(chain = true)
public class TcpNode<T> extends Node<T> {
    @Override
    public void init() {
        super.init();
        ThreadUtil.execute(() -> TcpHandler.init(this));
    }

    @Override
    public void destroy() {
        log.info("node destroy: {}", this.getName());
    }

    @Override
    public void sendData(T data) {
        ChannelFuture future = TcpHandler.CHANNEL_FUTURES.get(this.getName());
        if (ObjectUtils.isEmpty(future)) {
            log.warn("tcp node offline ... : {}  data not send: {} ", this.getName(), data);
            return;
        }

        future.channel().writeAndFlush(data);  // send 失败不处理...
    }
}
