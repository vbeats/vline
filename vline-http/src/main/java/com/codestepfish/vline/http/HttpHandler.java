package com.codestepfish.vline.http;

public interface HttpHandler {

    void init(HttpNode node);

    void rec(HttpNode node, Object data);
}
