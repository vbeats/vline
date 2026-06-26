package com.codestepfish.vline.duckdb.handler;

import com.codestepfish.vline.duckdb.DuckNode;

public interface DuckDataHandler {
    void handle(DuckNode node, Object data);
}
