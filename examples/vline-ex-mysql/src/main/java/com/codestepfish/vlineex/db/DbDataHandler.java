package com.codestepfish.vlineex.db;

import com.codestepfish.vline.mysql.MysqlNode;
import com.codestepfish.vline.mysql.handler.MysqlDataHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DbDataHandler implements MysqlDataHandler {


    @Override
    public void init(MysqlNode node) {

    }

    @Override
    public void rec(MysqlNode node, Object data) {

    }
}