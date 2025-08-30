package com.codestepfish.vline.oracle.handler;

import com.codestepfish.vline.oracle.OracleNode;

public interface OracleWriteHandler {

    <T> void write(OracleNode node, T data); // write mode
}
