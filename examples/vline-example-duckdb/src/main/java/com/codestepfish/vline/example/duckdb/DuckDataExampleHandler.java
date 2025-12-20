package com.codestepfish.vline.example.duckdb;

import com.codestepfish.vline.duckdb.DuckClientHolder;
import com.codestepfish.vline.duckdb.DuckNode;
import com.codestepfish.vline.duckdb.handler.DuckDataHandler;
import lombok.extern.slf4j.Slf4j;
import org.duckdb.DuckDBConnection;

import java.sql.ResultSet;
import java.sql.Statement;

@Slf4j
public class DuckDataExampleHandler implements DuckDataHandler {
    @Override
    public void handle(DuckNode node, Object data) {

        DuckDBConnection conn = DuckClientHolder.DUCK_CLIENTS.get(node.getName());

        try {
            Statement stmt = conn.createStatement();
            stmt.execute("CREATE TABLE items (item VARCHAR, value DECIMAL(10, 2), count INTEGER)");
            stmt.execute("INSERT INTO items VALUES ('jeans', 20.0, 1), ('hammer', 42.2, 2)");

            try (ResultSet rs = stmt.executeQuery("SELECT * FROM items")) {
                while (rs.next()) {
                    log.info("=========> item: {}, value: {}, count: {}", rs.getString(1), rs.getDouble(2), rs.getInt(3));
                }
            }

            stmt.close();

        } catch (Exception e) {
            log.error("【{}】 Handle Failed : ", node.getName(), e);
        }
    }
}
