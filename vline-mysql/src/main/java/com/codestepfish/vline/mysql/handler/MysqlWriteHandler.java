package com.codestepfish.vline.mysql.handler;

import com.codestepfish.vline.mysql.MysqlNode;

public interface MysqlWriteHandler {

    <T> void write(MysqlNode node, T data); // write mode
}
