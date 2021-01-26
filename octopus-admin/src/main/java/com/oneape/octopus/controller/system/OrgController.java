package com.oneape.octopus.controller.system;

import com.oneape.octopus.commons.cause.StateCode;
import com.oneape.octopus.commons.vo.TreeNodeVO;
import com.oneape.octopus.controller.system.form.OrgForm;
import com.oneape.octopus.domain.system.OrganizationDO;
import com.oneape.octopus.model.vo.ApiResult;
import com.oneape.octopus.service.system.OrganizationService;
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

    @PostMapping("/save")
    public ApiResult<String> doSaveOrg(@RequestBody @Validated(value = OrgForm.SaveCheck.class) OrgForm form) {
        int status = orgService.save(form.toDO());
        if (status > 0) {
            return ApiResult.ofData("Save Org successfully.");
        }
        return ApiResult.ofError(StateCode.BizError, "Save Org fail.");
    }

    @PostMapping("/del/{orgId}")
    public ApiResult<String> doDelOrg(@PathVariable(name = "orgId") Long orgId) {
        int status = orgService.deleteById(orgId);
        if (status > 0) {
            return ApiResult.ofData("Deleted org successfully.");
        }
        return ApiResult.ofError(StateCode.BizError, "Deleted org fail.");
    }

    @GetMapping("/get/{orgId}")
    public ApiResult<OrganizationDO> getOrg(@PathVariable(name = "orgId") Long orgId) {
        OrganizationDO orgDo = orgService.findById(orgId);
        return ApiResult.ofData(orgDo);
    }

    /**
     * Building a organization tree.
     */
    @PostMapping("/tree")
    public ApiResult getOrgTree(@RequestBody OrgForm form) {
        List<TreeNodeVO> treeNodes = orgService.genTree(form.isAddChildrenSize(), form.isAddRootNode(), form.getDisabledKeys());
        return ApiResult.ofData(treeNodes);
    }


}
