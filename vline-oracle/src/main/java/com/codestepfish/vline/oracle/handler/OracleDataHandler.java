package com.codestepfish.vline.oracle.handler;

import com.codestepfish.vline.oracle.OracleNode;

public interface OracleDataHandler {

    void handle(OracleNode node, Object data);
}
