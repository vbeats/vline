package com.codestepfish.vline.mssql.handler;

import com.codestepfish.vline.mssql.MssqlNode;

public interface MssqlDataHandler {

    void init(MssqlNode node);

    void rec(MssqlNode node, Object data);
}
