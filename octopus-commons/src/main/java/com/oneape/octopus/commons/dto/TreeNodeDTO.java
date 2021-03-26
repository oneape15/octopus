package com.oneape.octopus.commons.dto;

import com.oneape.octopus.commons.enums.FixServeGroupType;
import com.oneape.octopus.commons.value.DataUtils;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * octopus tree node.
 * Created by oneape<oneape15@163.com>
 * Created 2020-12-30 09:43.
 * Modify:
 */
@Data
@NoArgsConstructor
public class TreeNodeDTO implements Serializable {
    public static final String KEY_LEAF_SIZE = "leafSize";

    // 此项必须设置（其值在整个树范围内唯一）
    private String key;
    // 默认根据此属性值进行筛选（其值在整个树范围内唯一）
    private String value;
    // 树节点显示的内容
    private String title;
    // 图标
    private String icon;
    // 子节点
    private List<TreeNodeDTO> children;
    // 是否是叶子节点
    private boolean isLeaf = true;
    // 是否禁用
    private boolean disabled = false;
    // 节点额外属性值
    private Map<String, String> props = new HashMap<>();

    public TreeNodeDTO(String key, String title) {
        this.key = key;
        this.value = key;
        this.title = title;
    }

    public TreeNodeDTO(FixServeGroupType fgt) {
        this.key = String.valueOf(fgt.getId());
        this.value = key;
        this.title = fgt.getGroupName();
    }

    public void addChildren(List<TreeNodeDTO> list) {
        if (CollectionUtils.isEmpty(list)) return;

        if (children == null) children = new ArrayList<>();

        children.addAll(list);
    }

    public void addChild(TreeNodeDTO child) {
        if (child == null) return;

        if (children == null) children = new ArrayList<>();

        this.isLeaf = false;
        children.add(child);
    }

    public void addProps(String pKey, String pValue) {
        props.put(pKey, pValue);
    }

    public void setLeafSize(Integer size) {
        props.put(KEY_LEAF_SIZE, DataUtils.int2String(size, "0"));
    }

    public Integer getLeafSize() {
        String val = props.get(KEY_LEAF_SIZE);
        return DataUtils.str2Integer(val, 0);
    }
}
