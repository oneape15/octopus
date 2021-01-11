package com.oneape.octopus.controller.system;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.oneape.octopus.commons.cause.StateCode;
import com.oneape.octopus.controller.system.form.RoleForm;
import com.oneape.octopus.domain.system.RoleDO;
import com.oneape.octopus.model.vo.ApiResult;
import com.oneape.octopus.commons.enums.Archive;
import com.oneape.octopus.service.system.RoleService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The system role management.
 */
@RestController
@RequestMapping("/role")
public class RoleController {
    @Resource
    private RoleService roleService;

    @PostMapping("/save")
    public ApiResult<String> doSaveRole(@RequestBody @Validated(value = RoleForm.AddCheck.class) RoleForm form) {
        int status = roleService.save(form.toDO());
        if (status > 0) {
            return ApiResult.ofData("Save role successfully.");
        }
        return ApiResult.ofError(StateCode.BizError, "Save role fail.");
    }


    @PostMapping("/del")
    public ApiResult<String> doDelRole(@RequestBody @Validated(value = RoleForm.KeyCheck.class) RoleForm form) {
        int status = roleService.deleteById(form.getId());
        if (status > 0) {
            return ApiResult.ofData("Deleted role successfully.");
        }
        return ApiResult.ofError(StateCode.BizError, "Deleted role fail.");
    }

    /**
     * Gets the specified role resource permission.
     */
    @PostMapping("/permission/{roleId}")
    public ApiResult getResByRoleId(@PathVariable(name = "roleId") Long roleId) {
        Map<Long, Set<Integer>> mask = roleService.getRoleRes(Arrays.asList(roleId));
        return ApiResult.ofData(mask);
    }

    /**
     * Paging query role.
     */
    @PostMapping("/list")
    public ApiResult<PageInfo<RoleDO>> doList(@RequestBody @Validated RoleForm form) {
        PageHelper.startPage(form.getCurrent(), form.getPageSize());
        RoleDO rdo = form.toDO();
        rdo.setArchive(Archive.NORMAL.value());
        List<RoleDO> list = roleService.find(rdo);
        return ApiResult.ofData(new PageInfo<>(list));
    }

    /**
     * Get all role.
     */
    @PostMapping("/all")
    public ApiResult<List<RoleDO>> getAllRoles() {
        return ApiResult.ofData(roleService.find(new RoleDO()));
    }


    /**
     * Save the data permission information owned by the role
     *
     * @param form RoleForm
     */
    @PostMapping("/saveSchemaPermission")
    public ApiResult saveSchemaPermission(@RequestBody @Validated(value = RoleForm.KeyCheck.class) RoleForm form) {
        int status = roleService.batchSaveRoleRlSchema(form.getId(), form.getSchemaDOList());
        if (status > 0) {
            return ApiResult.ofData("Save role and data table information  successfully.");
        }
        return ApiResult.ofError(StateCode.BizError, "Save role and data table information fail.");
    }
}
