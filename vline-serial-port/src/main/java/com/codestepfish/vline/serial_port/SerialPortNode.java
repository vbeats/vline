package com.codestepfish.vline.serial_port;

import com.codestepfish.vline.core.Node;
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
public class SerialPortNode<T> extends Node<T> {
}
