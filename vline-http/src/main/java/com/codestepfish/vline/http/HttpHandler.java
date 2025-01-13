package com.codestepfish.vline.http;

public interface HttpHandler<T> {
    void handle(HttpNode node, T data);
}
