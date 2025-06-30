package com.codestepfish.vline.postgres.handler;

import com.codestepfish.vline.postgres.PostgresNode;

public interface PostgresReadHandler {

    void read(PostgresNode node); // read mode
}
