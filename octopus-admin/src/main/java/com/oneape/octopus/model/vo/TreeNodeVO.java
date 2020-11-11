package com.oneape.octopus.model.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 树型节点
 */
@Data
@NoArgsConstructor
public class TreeNodeVO implements Serializable {
    // 此项必须设置（其值在整个树范围内唯一）
    private String key;
    // 默认根据此属性值进行筛选（其值在整个树范围内唯一）
    private String value;
    // 树节点显示的内容
    private String title;
    // 图标
    private String icon;
    // 子节点
    private List<TreeNodeVO> children;
    // 是否是叶子节点
    private boolean isLeaf = false;
    // 是否禁用
    private boolean disabled = false;
    // 节点额外属性值
    private Map<String, String> props = new HashMap<>();

    public TreeNodeVO(String key, String title, String icon) {
        this.key = key;
        this.value = key;
        this.title = title;
        this.icon = icon;
    }
}
