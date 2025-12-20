package com.codestepfish.vline.duckdb.handler;

import com.codestepfish.vline.duckdb.DuckNode;

public interface DuckDataHandler {
    <T> void handle(DuckNode node, T data);
}
