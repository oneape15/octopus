package com.oneape.octopus.controller.system;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.oneape.octopus.common.StateCode;
import com.oneape.octopus.model.enums.Archive;
import com.oneape.octopus.controller.system.form.RoleForm;
import com.oneape.octopus.model.DO.system.RoleDO;
import com.oneape.octopus.model.VO.ApiResult;
import com.oneape.octopus.model.VO.RoleVO;
import com.oneape.octopus.service.system.RoleService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * The system role management.
 */
@RestController
@RequestMapping("/role")
public class RoleController {
    @Resource
    private RoleService roleService;

    @PostMapping("/add")
    public ApiResult<String> doAddRole(@RequestBody @Validated(value = RoleForm.AddCheck.class) RoleForm form) {
        int status = roleService.insert(form.toDO());
        if (status > 0) {
            return ApiResult.ofData("Add role successfully.");
        }
        return ApiResult.ofError(StateCode.BizError, "Add role fail.");
    }

    @PostMapping("/edit")
    public ApiResult<String> doEditRole(@RequestBody @Validated(value = RoleForm.EditCheck.class) RoleForm form) {
        int status = roleService.edit(form.toDO());
        if (status > 0) {
            return ApiResult.ofData("Edit role successfully.");
        }
        return ApiResult.ofError(StateCode.BizError, "Edit role fail.");
    }

    @PostMapping("/del")
    public ApiResult<String> doDelRole(@RequestBody @Validated(value = RoleForm.KeyCheck.class) RoleForm form) {
        int status = roleService.deleteById(form.toDO());
        if (status > 0) {
            return ApiResult.ofData("Deleted role successfully.");
        }
        return ApiResult.ofError(StateCode.BizError, "Deleted role fail.");
    }

    /**
     * Gets the specified role resource permission.
     */
    @PostMapping("/getByRoleId")
    public ApiResult getResByRoleId(@RequestBody @Validated(value = RoleForm.KeyCheck.class) RoleForm form) {
        Map<Long, List<Integer>> mask = roleService.getRoleRes(Arrays.asList(form.getId()));
        return ApiResult.ofData(mask);
    }

    /**
     * Paging query role.
     */
    @PostMapping("/list")
    public ApiResult<PageInfo<RoleVO>> doList(@RequestBody @Validated RoleForm form) {
        PageHelper.startPage(form.getCurrentPage(), form.getPageSize());
        RoleDO rdo = form.toDO();
        rdo.setArchive(Archive.NORMAL.value());
        List<RoleVO> vos = roleService.find(rdo);
        return ApiResult.ofData(new PageInfo<>(vos));
    }

    /**
     * Get all role.
     */
    @PostMapping("/all")
    public ApiResult<List<RoleVO>> getAllRoles() {
        return ApiResult.ofData(roleService.find(new RoleDO()));
    }
}
