package com.codestepfish.vline.jtds.handler;


import com.codestepfish.vline.jtds.MssqlNode;

public interface MssqlDataHandler {

    void init(MssqlNode node);

    void rec(MssqlNode node, Object data);
}
