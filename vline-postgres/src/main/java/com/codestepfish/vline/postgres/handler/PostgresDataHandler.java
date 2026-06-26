package com.codestepfish.vline.postgres.handler;

import com.codestepfish.vline.postgres.PostgresNode;

public interface PostgresDataHandler {

    void handle(PostgresNode node, Object data);
}
