package com.codestepfish.vlineex.api;

import com.codestepfish.vline.http.HttpHandler;
import com.codestepfish.vline.http.HttpNode;
import com.codestepfish.vlineex.model.DataItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApiHandler implements HttpHandler<DataItem> {

    private final ApiClient apiClient;

    @Override
    public void handle(HttpNode node, DataItem data) {

        log.info("=========> 【Request】 数据推送请求: {}", data);

        apiClient.uploadTrade(data, (body, req, res) -> {
            log.info("===============> 【Success】 数据推送成功: {}", data);
        }, (ex, req, res) -> {
            log.error("===============> 【Error】 数据推送失败: {}", data);
        });
    }
}
