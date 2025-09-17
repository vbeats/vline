package com.codestepfish.vline.mssql.handler;

import com.codestepfish.vline.mssql.MssqlNode;

public interface MssqlWriteHandler {

    <T> void write(MssqlNode node, T data); // write mode
}
