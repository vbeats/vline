package com.codestepfish.vline.oracle.handler;

import com.codestepfish.vline.oracle.OracleNode;

public interface OracleDataHandler {

    void init(OracleNode node);

    void rec(OracleNode node, Object data);
}
