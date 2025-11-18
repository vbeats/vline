package com.codestepfish.vline.h2.handler;


import com.codestepfish.vline.h2.H2Node;

public interface H2WriteHandler {

    <T> void write(H2Node node, T data); // write mode
}
