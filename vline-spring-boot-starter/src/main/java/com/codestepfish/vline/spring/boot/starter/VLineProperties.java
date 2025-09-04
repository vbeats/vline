package com.codestepfish.vline.spring.boot.starter;

import com.codestepfish.vline.core.Node;
import com.codestepfish.vline.core.VLine;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
@Configuration
@ConfigurationProperties(prefix = VLine.PREFIX)
public class VLineProperties {

    private List<Node> nodes; // 节点

    private Map<String, List<String>> struct; // 拓扑结构

    private Boolean cacheStats = false; // 是否开启caffeine cache统计  60s一次
}
