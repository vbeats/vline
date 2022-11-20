package com.codestepfish.vline.spring.boot.starter;

import com.codestepfish.vline.core.Device;
import com.codestepfish.vline.core.VLine;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Getter
@Setter
@ConfigurationProperties(prefix = VLine.PREFIX)
public class VLineProperties {

    private Boolean enabled = false;

    private List<Device> devices;

}
