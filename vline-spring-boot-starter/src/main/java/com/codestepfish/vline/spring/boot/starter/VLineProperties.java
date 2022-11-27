package com.codestepfish.vline.spring.boot.starter;

import com.codestepfish.vline.core.Device;
import com.codestepfish.vline.core.VLine;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@ConfigurationProperties(prefix = VLine.PREFIX)
public class VLineProperties {

    private Boolean enabled = false;

    private List<Device> devices;

    private Map<String, List<String>> topo; // 拓扑结构
}
