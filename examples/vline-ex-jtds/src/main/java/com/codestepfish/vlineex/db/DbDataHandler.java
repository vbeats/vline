package com.codestepfish.vlineex.db;

import com.codestepfish.vline.jtds.MssqlNode;
import com.codestepfish.vline.jtds.handler.MssqlDataHandler;
import lombok.extern.slf4j.Slf4j;
import org.anyline.service.AnylineService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DbDataHandler implements MssqlDataHandler {

    private AnylineService anylineService;


    @Override
    public void init(MssqlNode node) {

    }

    @Override
    public void rec(MssqlNode node, Object data) {

    }
}