package com.codestepfish.vline.jtds.handler;


import com.codestepfish.vline.jtds.MssqlNode;

public interface MssqlDataHandler {

    void handle(MssqlNode node, Object data);
}
