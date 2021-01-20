package com.oneape.octopus.commons.vo;

import com.oneape.octopus.commons.enums.FixServeGroupType;
import com.oneape.octopus.commons.value.TypeValueUtils;
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
public class TreeNodeVO implements Serializable {
    public static final String KEY_LEAF_SIZE = "leafSize";

    // 此项必须设置（其值在整个树范围内唯一）
    private String           key;
    // 默认根据此属性值进行筛选（其值在整个树范围内唯一）
    private String           value;
    // 树节点显示的内容
    private String           title;
    // 图标
    private String           icon;
    // 子节点
    private List<TreeNodeVO> children;
    // 是否是叶子节点
    private boolean             isLeaf   = true;
    // 是否禁用
    private boolean             disabled = false;
    // 节点额外属性值
    private Map<String, String> props    = new HashMap<>();

    public TreeNodeVO(String key, String title) {
        this.key = key;
        this.value = key;
        this.title = title;
    }

    public TreeNodeVO(FixServeGroupType fgt) {
        this.key = String.valueOf(fgt.getId());
        this.value = key;
        this.title = fgt.getGroupName();
    }

    public void addChildren(List<TreeNodeVO> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        if (children == null) {
            children = new ArrayList<>();
        }

        children.addAll(list);
    }

    public void addChild(TreeNodeVO child) {
        if (child == null) {
            return;
        }

        if (children == null) {
            children = new ArrayList<>();
        }
        this.isLeaf = false;
        children.add(child);
    }

    public void addProps(String pKey, String pValue) {
        props.put(pKey, pValue);
    }

    public void setLeafSize(Integer size) {
        props.put(KEY_LEAF_SIZE, TypeValueUtils.int2str(size, "0"));
    }

    public Integer getLeafSize() {
        String val = props.get(KEY_LEAF_SIZE);
        return TypeValueUtils.str2int(val, 0);
    }
}
