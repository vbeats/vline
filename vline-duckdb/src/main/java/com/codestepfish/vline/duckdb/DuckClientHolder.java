package com.codestepfish.vline.duckdb;

import org.duckdb.DuckDBConnection;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DuckClientHolder {

    // node name -> duckdb connection
    public static final Map<String, DuckDBConnection> DUCK_CLIENTS = new ConcurrentHashMap<>(1);
}
