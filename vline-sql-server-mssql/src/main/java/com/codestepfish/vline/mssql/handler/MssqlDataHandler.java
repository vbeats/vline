package com.codestepfish.vline.mssql.handler;

import com.codestepfish.vline.mssql.MssqlNode;

public interface MssqlDataHandler {

    void handle(MssqlNode node, Object data);
}
