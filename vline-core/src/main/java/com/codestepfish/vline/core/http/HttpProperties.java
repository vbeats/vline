package com.codestepfish.vline.core.http;

import cn.hutool.http.Method;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class HttpProperties {
    private String url;

    @JSONField(serializeFeatures = JSONWriter.Feature.WriteEnumsUsingName)
    private Method method = Method.POST;

    private Integer maxRetryCount = 0;  // 最大重试次数 0 不重试

    private Long maxRetryInterval = 0L;// 最大重试间隔时间 毫秒

    private String retryWhen; // 重试条件 实现 RetryWhen 接口
    private String successWhen; // 请求成功条件 实现 SuccessWhen 接口
    private String onError; // 请求失败处理 实现 OnError 接口

    private String handler; // http请求处理器 实现 ForestHandler 接口
}
