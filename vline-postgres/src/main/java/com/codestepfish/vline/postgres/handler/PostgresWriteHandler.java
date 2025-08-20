package com.codestepfish.vline.postgres.handler;

import com.codestepfish.vline.postgres.PostgresNode;

public interface PostgresWriteHandler {

    <T> void write(PostgresNode node, T data); // write mode
}
