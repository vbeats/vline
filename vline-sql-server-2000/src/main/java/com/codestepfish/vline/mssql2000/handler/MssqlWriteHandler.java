package com.codestepfish.vline.mssql2000.handler;


import com.codestepfish.vline.mssql2000.MssqlNode;

public interface MssqlWriteHandler<T> {

    void write(MssqlNode node, T data); // write mode
}
