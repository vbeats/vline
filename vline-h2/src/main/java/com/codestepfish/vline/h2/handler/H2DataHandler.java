package com.codestepfish.vline.h2.handler;


import com.codestepfish.vline.h2.H2Node;

public interface H2DataHandler {

    void init(H2Node node);

    void rec(H2Node node, Object data);
}
