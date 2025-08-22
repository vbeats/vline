package com.codestepfish.vline.core.eventbus;

import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class VLineEvent implements Serializable {

    @Serial
    private static final long serialVersionUID = -1342729915234628231L;

    private String key;  // node name

    private Long sequence;  // 事件序列

    private Object payload;  // payload数据
}
