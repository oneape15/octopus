package com.oneape.octopus.controller.system;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.oneape.octopus.common.StateCode;
import com.oneape.octopus.controller.system.form.RoleForm;
import com.oneape.octopus.model.DO.system.RoleDO;
import com.oneape.octopus.model.VO.ApiResult;
import com.oneape.octopus.model.VO.RoleVO;
import com.oneape.octopus.service.RoleService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/role")
public class RoleController {
    @Resource
    private RoleService roleService;

    @PostMapping("/add")
    public ApiResult<String> doAddRole(@RequestBody @Validated(value = RoleForm.AddCheck.class) RoleForm form) {
        int status = roleService.insert(form.toDO());
        if (status > 0) {
            return ApiResult.ofData("添加角色成功");
        }
        return ApiResult.ofError(StateCode.BizError, "添加角色失败");
    }

    @PostMapping("/edit")
    public ApiResult<String> doEditRole(@RequestBody @Validated(value = RoleForm.EditCheck.class) RoleForm form) {
        int status = roleService.edit(form.toDO());
        if (status > 0) {
            return ApiResult.ofData("修改角色成功");
        }
        return ApiResult.ofError(StateCode.BizError, "修改角色失败");
    }

    @PostMapping("/del")
    public ApiResult<String> doDelRole(@RequestBody @Validated(value = RoleForm.KeyCheck.class) RoleForm form) {
        int status = roleService.deleteById(form.toDO());
        if (status > 0) {
            return ApiResult.ofData("删除角色成功");
        }
        return ApiResult.ofError(StateCode.BizError, "删除角色失败");
    }

    /**
     * 分页查询
     */
    @PostMapping("/list")
    public ApiResult<PageInfo<RoleVO>> doList(@RequestBody @Validated RoleForm form) {
        PageHelper.startPage(form.getCurrentPage(), form.getPageSize());
        List<RoleVO> vos = roleService.find(form.toDO());
        return ApiResult.ofData(new PageInfo<>(vos));
    }

    /**
     * 获取所有角色
     */
    @PostMapping("/all")
    public ApiResult<List<RoleVO>> getAllRoles() {
        return ApiResult.ofData(roleService.find(new RoleDO()));
    }
}
