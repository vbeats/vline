package com.codestepfish.vlineex.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@ToString
@Builder
public class DataItem implements Serializable {

    @Serial
    private static final long serialVersionUID = -8483293232201877270L;

    private Long id;
}
