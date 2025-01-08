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

}
