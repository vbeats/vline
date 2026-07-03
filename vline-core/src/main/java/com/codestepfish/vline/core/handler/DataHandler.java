package com.codestepfish.vline.core.handler;

import com.codestepfish.vline.core.Node;

public interface DataHandler<N extends Node> {

    void init(N node);

    void rec(N node, Object data);

    void destroy(N node);
}
