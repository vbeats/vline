package com.codestepfish.vline.tcp;

import com.codestepfish.vline.core.Node;
import com.codestepfish.vline.tcp.handler.TcpHandler;
import com.codestepfish.vline.tcp.util.TcpHolder;
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
public class TcpNode extends Node {
    @Override
    public void init() {
        super.init();
        Thread.ofVirtual().start(() -> TcpHandler.init(this));
    }

    @Override
    public void destroy() {
        super.destroy();
        Thread.ofVirtual().start(() -> {
            try {
                TcpHolder.CHANNEL_FUTURES.get(this.getName()).channel().close().sync();
            } catch (Exception e) {
                log.error("【{}】 Destroy Failed: ", this.getName(), e);
            }
        });
        Thread.ofVirtual().start(() -> {
            try {
                TcpHolder.CLIENT_CHANNELS.get(this.getName()).close().sync();
            } catch (Exception e) {
                log.error("【{}】 Destroy Failed: ", this.getName(), e);
            }
        });
    }

    @Override
    public <T> void receiveData(T data) {
        ChannelFuture future = TcpHolder.CHANNEL_FUTURES.get(this.getName());
        if (ObjectUtils.isEmpty(future)) {
            log.warn("【{}】 Offline ... Data Send Failed: {} ", this.getName(), data);
            return;
        }

        future.channel().writeAndFlush(data);  // send 失败不处理...
    }
}
