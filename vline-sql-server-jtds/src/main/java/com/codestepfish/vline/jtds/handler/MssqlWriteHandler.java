package com.codestepfish.vline.jtds.handler;


import com.codestepfish.vline.jtds.MssqlNode;

public interface MssqlWriteHandler {

    <T> void write(MssqlNode node, T data); // write mode
}
