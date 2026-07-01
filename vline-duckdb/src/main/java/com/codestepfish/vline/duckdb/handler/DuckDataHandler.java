package com.codestepfish.vline.duckdb.handler;

import com.codestepfish.vline.duckdb.DuckNode;

public interface DuckDataHandler {

    void init(DuckNode node);

    void rec(DuckNode node, Object data);
}
