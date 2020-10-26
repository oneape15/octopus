package com.oneape.octopus.model.VO;

import com.oneape.octopus.model.domain.system.ResourceDO;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.List;

@Data
public class ResourceVO implements Serializable {
    // 主键
    private Long id;
    // 父节点Id
    private Long parentId;
    // 资源名称
    private String name;
    // 资源图标
    private String icon;
    // 0 - 菜单; 1 - 资源项
    private Integer type;
    // 资源路径
    private String path;
    // 权限编码
    private String authCode;
    // 排序Id
    private Long sortId;
    // 描述
    private String comment;
    // 子节点
    private List<ResourceVO> children;

    public static ResourceVO ofDO(ResourceDO rdo) {
        if (rdo == null) {
            return null;
        }
        ResourceVO vo = new ResourceVO();
        BeanUtils.copyProperties(rdo, vo);
        return vo;
    }

    public ResourceDO toDO() {
        ResourceDO udo = new ResourceDO();
        BeanUtils.copyProperties(this, udo);
        return udo;
    }
}
