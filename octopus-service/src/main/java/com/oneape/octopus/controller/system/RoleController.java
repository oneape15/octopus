package com.oneape.octopus.controller.system;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.oneape.octopus.controller.system.form.RoleForm;
import com.oneape.octopus.model.VO.ApiResult;
import com.oneape.octopus.model.VO.RoleVO;
import com.oneape.octopus.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @PostMapping("/add")
    public ApiResult<String> doAddRole(@RequestBody @Validated(value = RoleForm.AddCheck.class) RoleForm form) {
        int status = roleService.insert(form.toDO());
        return ApiResult.ofData(status > 0 ? "添加角色成功" : "添加角色失败");
    }

    @PostMapping("/edit")
    public ApiResult<String> doEditRole(@RequestBody @Validated(value = RoleForm.EditCheck.class) RoleForm form) {
        int status = roleService.edit(form.toDO());
        return ApiResult.ofData(status > 0 ? "修改角色成功" : "修改角色失败");
    }

    @PostMapping("/del")
    public ApiResult<String> doDelRole(@RequestBody @Validated(value = RoleForm.KeyCheck.class) RoleForm form) {
        int status = roleService.deleteById(form.toDO());
        return ApiResult.ofData(status > 0 ? "删除角色成功" : "删除角色失败");
    }

    @PostMapping("/list")
    public ApiResult<PageInfo> doList(@RequestBody @Validated RoleForm form) {
        PageHelper.startPage(form.getCurrentPage(), form.getPageSize());
        List<RoleVO> vos = roleService.find(form.toDO());
        PageInfo<RoleVO> pageInfo = new PageInfo<>(vos);

        return ApiResult.ofData(pageInfo);
    }
}
