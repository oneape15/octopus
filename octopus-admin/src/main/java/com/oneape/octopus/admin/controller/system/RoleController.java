package com.oneape.octopus.admin.controller.system;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Preconditions;
import com.oneape.octopus.admin.config.I18nMsgConfig;
import com.oneape.octopus.admin.controller.system.form.RoleForm;
import com.oneape.octopus.admin.service.system.AccountService;
import com.oneape.octopus.admin.service.system.RoleService;
import com.oneape.octopus.commons.cause.StateCode;
import com.oneape.octopus.commons.dto.ApiResult;
import com.oneape.octopus.commons.enums.Archive;
import com.oneape.octopus.domain.system.RoleDO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Collections;
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
    @Resource
    private AccountService accountService;

    @PostMapping("/save")
    public ApiResult<String> doSaveRole(@RequestBody @Validated(value = RoleForm.SaveCheck.class) RoleForm form) {
        int status = roleService.save(form.toDO());
        if (status > 0) {
            return ApiResult.ofData(I18nMsgConfig.getMessage("role.save.success"));
        }
        return ApiResult.ofError(StateCode.BizError, I18nMsgConfig.getMessage("role.save.fail"));
    }

    @PostMapping("/del/{roleId}")
    public ApiResult<String> doDelRole(@PathVariable(name = "roleId") Long roleId) {
        int status = roleService.deleteById(roleId);
        if (status > 0) {
            return ApiResult.ofData(I18nMsgConfig.getMessage("role.del.success"));
        }
        return ApiResult.ofError(StateCode.BizError, I18nMsgConfig.getMessage("role.del.fail"));
    }

    /**
     * Gets the specified role resource permission.
     */
    @PostMapping("/permission/{roleId}")
    public ApiResult<Map<Long, Set<Integer>>> getResByRoleId(@PathVariable(name = "roleId") Long roleId) {
        Map<Long, Set<Integer>> mask = roleService.getRoleRes(Collections.singletonList(roleId));
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
    @GetMapping("/all")
    public ApiResult<List<RoleDO>> getAllRoles() {
        return ApiResult.ofData(roleService.find(new RoleDO()));
    }


    /**
     * get the role list of the user
     *
     * @param userId Long
     * @return List
     */
    @GetMapping("/getByUserId/{userId}")
    public ApiResult<List<RoleDO>> getByUserId(@PathVariable(name = "userId") Long userId) {
        Preconditions.checkNotNull(accountService.findById(userId), I18nMsgConfig.getMessage("account.user.null"));
        List<RoleDO> list = roleService.findRoleByUserId(userId);

        return ApiResult.ofData(list);
    }

    /**
     * Save the data permission information owned by the role
     *
     * @param form RoleForm
     */
    @PostMapping("/saveSchemaPermission")
    public ApiResult<String> saveSchemaPermission(@RequestBody @Validated(value = RoleForm.KeyCheck.class) RoleForm form) {
        int status = roleService.batchSaveRoleRlSchema(form.getId(), form.getSchemaDOList());
        if (status > 0) {
            return ApiResult.ofData(I18nMsgConfig.getMessage("role.saveSchema.success"));
        }
        return ApiResult.ofError(StateCode.BizError, I18nMsgConfig.getMessage("role.saveSchema.fail"));
    }
}
