package com.codestepfish.vline.mssql2008r2.handler;

import com.codestepfish.vline.mssql2008r2.MssqlNode;

public interface MssqlWriteHandler {

    <T> void write(MssqlNode node, T data); // write mode
}
