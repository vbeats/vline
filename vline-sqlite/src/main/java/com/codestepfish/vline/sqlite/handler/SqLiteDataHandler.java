package com.codestepfish.vline.sqlite.handler;


import com.codestepfish.vline.sqlite.SqLiteNode;

public interface SqLiteDataHandler {

    void handle(SqLiteNode node, Object data);
}
