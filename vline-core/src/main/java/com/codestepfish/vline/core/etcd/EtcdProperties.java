package com.codestepfish.vline.core.etcd;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
@NoArgsConstructor
@Accessors(chain = true)
public class EtcdProperties {

    private String endpoints;  // 多个逗号分隔  http://127.0.0.1:2379,http://127.0.0.1:2380

    private String dataHandler;

}
