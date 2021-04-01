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

    // The parent node key.
    private String pKey;
    // The node key,This must be set（Its value is unique across the entire tree）
    private String key;
    // The default filter is based on this property value（Its value is unique across the entire tree）
    private String value;
    // The content displayed by the tree node.
    private String title;
    // Icons displayed on tree nodes.
    private String icon;
    // The node children.
    private List<TreeNodeDTO> children;
    // Leaf node marker
    private boolean isLeaf = true;
    // disabled tag.
    private boolean disabled = false;
    // Node additional attribute values.
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
