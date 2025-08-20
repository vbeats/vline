package com.codestepfish.vline.sqlite.handler;


import com.codestepfish.vline.sqlite.SqLiteNode;

public interface SqLiteWriteHandler {

    <T> void write(SqLiteNode node, T data); // write mode
}
