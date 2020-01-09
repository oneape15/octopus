package com.oneape.octopus.model.VO;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 前端菜单对象
 */
@Data
public class MenuVO implements Serializable {
    private String path;
    private String name;
    private String icon;
    private String redirect;

    private Map<String, String> props = new HashMap<>(); // 节点额外属性值
    private Boolean exact = true;
    private List<MenuVO> children;
}
