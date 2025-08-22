package com.codestepfish.vline.core.eventbus;

import com.lmax.disruptor.EventFactory;

public class VLineEventFactory implements EventFactory<VLineEvent> {
    @Override
    public VLineEvent newInstance() {
        return new VLineEvent();
    }
}
