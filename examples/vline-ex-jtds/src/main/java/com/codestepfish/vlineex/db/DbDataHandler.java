package com.codestepfish.vlineex.db;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.RandomUtil;
import com.codestepfish.vline.core.VLineContext;
import com.codestepfish.vline.jtds.MssqlNode;
import com.codestepfish.vline.jtds.handler.MssqlReadHandler;
import com.codestepfish.vlineex.model.DataItem;
import lombok.extern.slf4j.Slf4j;
import org.anyline.entity.DataSet;
import org.anyline.proxy.ServiceProxy;
import org.anyline.service.AnylineService;
import org.springframework.util.ObjectUtils;

@Slf4j
public class DbDataHandler implements MssqlReadHandler {

    private AnylineService dbService;
    private AnylineService cacheService;

    @Override
    public void read(MssqlNode node) {

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

    private void dataHandle(MssqlNode node) {
        // db raed data

        DataSet dataSet = dbService.querys("x_test", "order by id desc");

        log.info("=====> db read data : {}", dataSet);

        DataItem dataItem = DataItem.builder().id(RandomUtil.randomLong()).build();

        VLineContext.posMsg(node.getName(), dataItem);

    }

    // ds 延迟初始化
    private void initDS() {
        //        anyline 全局配置
        // ConfigTable.IS_LOG_SQL = false;
        // ConfigTable.IS_LOG_SQL_TIME = false;

        do {
            dbService = ServiceProxy.service("db");
        } while (ObjectUtils.isEmpty(dbService));

        do {
            cacheService = ServiceProxy.service("cache");
        } while (ObjectUtils.isEmpty(cacheService));
    }
}