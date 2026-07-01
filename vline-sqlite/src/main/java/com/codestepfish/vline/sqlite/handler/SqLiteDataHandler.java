package com.codestepfish.vline.sqlite.handler;


import com.codestepfish.vline.sqlite.SqLiteNode;

public interface SqLiteDataHandler {

    void init(SqLiteNode node);

    void rec(SqLiteNode node, Object data);
}
