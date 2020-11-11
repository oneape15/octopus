package com.oneape.octopus.model.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 前端菜单对象
 */
@Data
@NoArgsConstructor
public class MenuVO implements Serializable {
    private String path;
    private String name;
    private String icon;
    private String redirect;

    private Map<String, String> props = new HashMap<>(); // 节点额外属性值
    private Boolean exact = true;
    private List<MenuVO> children = new ArrayList<>();

    public MenuVO(String name, String path, String icon) {
        this.name = name;
        this.path = path;
        this.icon = icon;
    }
}
