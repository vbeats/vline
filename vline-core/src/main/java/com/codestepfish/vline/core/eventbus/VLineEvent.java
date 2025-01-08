package com.codestepfish.vline.core.eventbus;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class VLineEvent<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = -1342729915234628231L;

    private String key;  // node name , event bus key

    private T msg;  // msg内容
}
