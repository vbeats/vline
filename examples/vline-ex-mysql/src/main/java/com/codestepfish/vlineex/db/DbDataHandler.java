package com.codestepfish.vlineex.db;

import cn.hutool.core.thread.ThreadUtil;
import com.codestepfish.vline.mysql.MysqlNode;
import com.codestepfish.vline.mysql.handler.MysqlReadHandler;
import lombok.extern.slf4j.Slf4j;
import org.anyline.entity.DataSet;
import org.anyline.proxy.ServiceProxy;
import org.anyline.service.AnylineService;
import org.springframework.util.ObjectUtils;

@Slf4j
public class DbDataHandler implements MysqlReadHandler {

    private AnylineService dbService;

    @Override
    public void read(MysqlNode node) {

        initDS();

        while (true) {
            try {

                dataHandle(node);

            } catch (Exception e) {
                log.error("===============> vline data处理异常 .... ", e);
            } finally {
                ThreadUtil.safeSleep(1000L);
            }
        }
    }

    private void dataHandle(MysqlNode node) {
        // db raed data

        DataSet dataSet = dbService.querys("x_test", "order by id desc");

        log.info("=====> db read data : {}", dataSet);
    }

    private void initDS() {
        //        anyline 全局配置
        // ConfigTable.IS_LOG_SQL = false;
        // ConfigTable.IS_LOG_SQL_TIME = false;

        do {
            dbService = ServiceProxy.service("db");
        } while (ObjectUtils.isEmpty(dbService));
    }
}