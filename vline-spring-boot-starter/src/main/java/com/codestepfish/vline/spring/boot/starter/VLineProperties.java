package com.codestepfish.vline.spring.boot.starter;

import com.codestepfish.vline.core.Node;
import com.codestepfish.vline.core.VLine;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
@ConfigurationProperties(prefix = VLine.PREFIX)
public class VLineProperties {

    private List<Node> nodes;

    private Map<String, List<String>> struct; // 拓扑结构

    /**
     * 当前node的 下级/上级节点 名
     *
     * @param nodeName
     * @return
     */
    public List<String> nextNodes(String nodeName) {
        List<String> ns = new ArrayList<>();

        struct.entrySet().forEach(entry -> {
            if (nodeName.equals(entry.getKey())) {
                ns.addAll(entry.getValue());
            }

            if (entry.getValue().contains(nodeName)) {
                ns.add(entry.getKey());
            }
        });

        return ns;
    }
}
