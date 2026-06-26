package com.codestepfish.vline.mysql.handler;

import com.codestepfish.vline.mysql.MysqlNode;

public interface MysqlDataHandler {

    void handle(MysqlNode node, Object data);
}
