package com.codestepfish.vline.postgres.handler;

import com.codestepfish.vline.postgres.PostgresNode;

public interface PostgresDataHandler {

    void init(PostgresNode node);

    void rec(PostgresNode node, Object data);
}
