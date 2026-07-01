package com.codestepfish.vline.mysql.handler;

import com.codestepfish.vline.mysql.MysqlNode;

public interface MysqlDataHandler {

    void init(MysqlNode node);

    void rec(MysqlNode node, Object data);
}
