package com.codestepfish.vlineex.config;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.dtflys.forest.callback.SuccessWhen;
import com.dtflys.forest.http.ForestRequest;
import com.dtflys.forest.http.ForestResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PushSuccessCondition implements SuccessWhen {
    @Override
    public boolean successWhen(ForestRequest req, ForestResponse res) {

        try {
            String content = res.getContent();

            JSONObject jsonObject = JSON.parseObject(content);

            return res.noException() &&   // 请求过程没有异常
                    res.statusOk() &&     // 并且状态码在 100 ~ 399 范围内
                    (200 == jsonObject.getIntValue("code"));
        } catch (Exception e) {
            log.error("=========> PushSuccessCondition判断异常", e);
        }

        return false;
    }
}
