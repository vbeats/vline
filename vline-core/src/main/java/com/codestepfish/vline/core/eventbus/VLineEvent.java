package com.codestepfish.vline.core.eventbus;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class VLineEvent<T> implements Serializable {
    private String key;  // node name , event bus key

    private T msg;  // msg内容
}
