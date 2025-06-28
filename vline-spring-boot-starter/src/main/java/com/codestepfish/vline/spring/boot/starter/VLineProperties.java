package com.codestepfish.vline.spring.boot.starter;

import com.codestepfish.vline.core.Node;
import com.codestepfish.vline.core.VLine;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
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

        if (CollectionUtils.isEmpty(struct)) {
            return Collections.emptyList();
        }
        List<String> ns = new ArrayList<>();

        struct.forEach((key, value) -> {
            if (nodeName.equals(key)) {
                ns.addAll(value);
            }

            if (value.contains(nodeName)) {
                ns.add(key);
            }
        });

        return ns;
    }
}
