package com.codestepfish.vline.core.modbus;

import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ModbusProperties {

    @JSONField(serializeFeatures = JSONWriter.Feature.WriteEnumsUsingName)
    private ModbusProperties.Protocol protocol = Protocol.TCP;  // modbus 通信方式

    public enum Protocol {
        TCP,
        TCP_TLS,
        RTU_TCP,
        RTU_SERIAL_PORT
    }
}
