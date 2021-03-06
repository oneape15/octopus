package com.oneape.octopus.admin.controller.system;

import com.oneape.octopus.admin.config.I18nMsgConfig;
import com.oneape.octopus.admin.controller.system.form.OrgForm;
import com.oneape.octopus.admin.service.system.OrganizationService;
import com.oneape.octopus.commons.cause.StateCode;
import com.oneape.octopus.commons.dto.ApiResult;
import com.oneape.octopus.commons.dto.TreeNodeDTO;
import com.oneape.octopus.domain.system.OrganizationDO;
import com.oneape.octopus.domain.system.UserDO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


/**
 * The org option Controller.
 * Created by oneape<oneape15@163.com>
 * Created 2021-01-25 14:27.
 * Modify:
 */
@RestController
@RequestMapping("/org")
public class OrgController {

    @Resource
    private OrganizationService orgService;

    /**
     * Save the organization information.
     */
    @PostMapping("/save")
    public ApiResult<String> doSaveOrg(@RequestBody @Validated(value = OrgForm.SaveCheck.class) OrgForm form) {
        int status = orgService.save(form.toDO());
        if (status > 0) {
            return ApiResult.ofData(I18nMsgConfig.getMessage("org.save.success"));
        }
        return ApiResult.ofError(StateCode.BizError, I18nMsgConfig.getMessage("org.save.fail"));
    }

    /**
     * Deleted the organization information based on org id.
     */
    @PostMapping("/del/{orgId}")
    public ApiResult<String> doDelOrg(@PathVariable(name = "orgId") Long orgId) {
        int status = orgService.deleteById(orgId);
        if (status > 0) {
            return ApiResult.ofData(I18nMsgConfig.getMessage("org.del.success"));
        }
        return ApiResult.ofError(StateCode.BizError, I18nMsgConfig.getMessage("org.del.fail"));
    }

    /**
     * Query the organization information based on org id.
     */
    @GetMapping("/get/{orgId}")
    public ApiResult<OrganizationDO> getOrg(@PathVariable(name = "orgId") Long orgId) {
        OrganizationDO orgDo = orgService.findById(orgId);
        return ApiResult.ofData(orgDo);
    }

    /**
     * Query the list of users based on org id.
     */
    @GetMapping("/getUsers/{orgId}")
    public ApiResult<List<UserDO>> getUserByOrgId(@PathVariable(name = "orgId") Long orgId) {
        List<UserDO> userDOs = orgService.getUserListByOrgId(orgId);
        return ApiResult.ofData(userDOs);
    }

    /**
     * Building a organization tree.
     */
    @PostMapping("/tree")
    public ApiResult<List<TreeNodeDTO>> getOrgTree(@RequestBody OrgForm form) {
        List<TreeNodeDTO> treeNodes = orgService.genTree(form.isAddChildrenSize(), form.isAddRootNode(), form.getDisabledKeys());
        return ApiResult.ofData(treeNodes);
    }


}
