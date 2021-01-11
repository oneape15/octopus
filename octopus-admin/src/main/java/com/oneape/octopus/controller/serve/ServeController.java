package com.oneape.octopus.controller.serve;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.oneape.octopus.commons.cause.StateCode;
import com.oneape.octopus.commons.enums.ReportParamType;
import com.oneape.octopus.commons.enums.ServeType;
import com.oneape.octopus.commons.enums.VisualType;
import com.oneape.octopus.commons.value.Pair;
import com.oneape.octopus.commons.vo.TreeNodeVO;
import com.oneape.octopus.controller.serve.form.ServeForm;
import com.oneape.octopus.controller.serve.form.ServeGroupForm;
import com.oneape.octopus.domain.serve.ServeInfoDO;
import com.oneape.octopus.model.vo.ApiResult;
import com.oneape.octopus.service.serve.ServeGroupService;
import com.oneape.octopus.service.serve.ServeInfoService;
import org.apache.ibatis.annotations.Param;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/serve")
public class ServeController {

    @Resource
    private ServeInfoService  serveInfoService;
    @Resource
    private ServeGroupService serveGroupService;

    /**
     * Save serve information.
     */
    @PostMapping("/save")
    public ApiResult<String> doSaveServe(@RequestBody @Validated(value = ServeForm.AddCheck.class) ServeForm form) {
        ServeInfoDO infoDO = form.toDO();
        int status = serveInfoService.save(infoDO, form.getGroupId());
        if (status > 0) {
            return ApiResult.ofData("Saved serve information successfully.");
        }
        return ApiResult.ofError(StateCode.BizError, "Saved serve information fail.");
    }

    /**
     * Deleted serve information.
     */
    @PostMapping("/del/{serveId}")
    public ApiResult<String> doDelServe(@PathVariable(name = "serverId") Long serveId) {
        int status = serveInfoService.deleteById(serveId);
        if (status > 0) {
            return ApiResult.ofData("Deleted serve information successfully.");
        }
        return ApiResult.ofError(StateCode.BizError, "Deleted serve information fail.");
    }

    /**
     * Copy a new service with the specified service as the template.
     */
    @PostMapping("/copy/{serveId}")
    public ApiResult<String> doCopyServe(@PathVariable(name = "serverId") Long serveId) {
        int status = serveInfoService.copyById(serveId, null);
        if (status > 0) {
            return ApiResult.ofData("Deleted serve information successfully.");
        }
        return ApiResult.ofError(StateCode.BizError, "Deleted serve information fail.");
    }

    /**
     * Copy a brand new service to the template according to the specified version.
     */
    @PostMapping("/copy/{serveId}/{versionCode}")
    public ApiResult<String> doCopyServeByVersionCode(@PathVariable(name = "serverId") Long serveId,
                                                      @PathVariable(name = "versionCode") String versionCode) {
        int status = serveInfoService.copyById(serveId, versionCode);
        if (status > 0) {
            return ApiResult.ofData("Deleted serve information successfully.");
        }
        return ApiResult.ofError(StateCode.BizError, "Deleted serve information fail.");
    }

    /**
     * Paging query serve information.
     */
    @PostMapping("/list")
    public ApiResult<PageInfo<ServeInfoDO>> doList(@RequestBody @Validated ServeForm form) {
        PageHelper.startPage(form.getCurrent(), form.getPageSize());
        List<ServeInfoDO> vos = serveInfoService.find(form.toDO());
        return ApiResult.ofData(new PageInfo<>(vos));
    }

    /**
     * Publishing service.
     *
     * @param serveId Long
     */
    @PostMapping("/publish/{serveId}")
    public ApiResult publishServe(@PathVariable(name = "serveId") Long serveId) {
        int status = serveInfoService.publishServe(serveId);
        if (status > 0) {
            return ApiResult.ofData("Publish serve successfully.");
        }
        return ApiResult.ofError(StateCode.BizError, "Publish serve fail.");
    }

    /**
     * Rolls back the specified version of the service.
     *
     * @param serveId Long
     * @param verCode String
     */
    public ApiResult rollbackServe(@PathVariable(name = "serveId") Long serveId, @PathVariable(name = "verCode") String verCode) {
        int status = serveInfoService.rollbackServe(serveId, verCode);
        if (status > 0) {
            return ApiResult.ofData("Rolls back serve successfully.");
        }
        return ApiResult.ofError(StateCode.BizError, "Rolls back serve fail.");
    }

    /**
     * Move the service to the new grouping directory.
     *
     * @param serveId Long
     * @param groupId Long
     */
    @PostMapping("/move/{serveId}/{groupId}")
    public ApiResult moveServe(@PathVariable(name = "serveId") Long serveId, @PathVariable(name = "groupId") Long groupId) {
        int status = serveInfoService.moveServe(serveId, groupId);
        if (status > 0) {
            return ApiResult.ofData("Move successfully.");
        }
        return ApiResult.ofError(StateCode.BizError, "Move fail.");
    }

    /**
     * Paging query serve information.
     */
    @GetMapping("/get/{serveId}")
    public ApiResult<ServeInfoDO> getById(@PathVariable(name = "serveId") Long serveId) {
        ServeInfoDO vo = serveInfoService.findById(serveId);
        return ApiResult.ofData(vo);
    }

    /**
     * Get serve type.
     */
    @GetMapping("/serveTypes")
    public ApiResult<List<Pair<String, String>>> getReportTypes() {
        List<Pair<String, String>> types = new ArrayList<>();
        ServeType.getList().forEach(rt -> types.add(new Pair<>(rt.getCode(), rt.getDesc())));
        return ApiResult.ofData(types);
    }


    /**
     * Get serve visual type.
     */
    @GetMapping("/visualTypes")
    public ApiResult<List<Pair<Integer, String>>> getReportVisualTypes() {
        List<Pair<Integer, String>> types = new ArrayList<>();
        VisualType.getList().forEach(vt -> types.add(new Pair<>(vt.getCode(), vt.getDesc())));
        return ApiResult.ofData(types);
    }

    /**
     * Get serve param type.
     */
    @GetMapping("/paramTypes")
    public ApiResult<List<Pair<Integer, String>>> getReportParamTypes() {
        List<Pair<Integer, String>> pairs = new ArrayList<>();
        ReportParamType.getList().forEach(rpt -> pairs.add(new Pair<>(rpt.getCode(), rpt.getDesc())));
        return ApiResult.ofData(pairs);
    }

    /**
     * Save serve group information.
     *
     * @param from ServeGroupForm
     */
    @PostMapping("/group/save")
    public ApiResult saveGroup(@RequestBody @Validated(value = ServeGroupForm.AddCheck.class) ServeGroupForm from) {
        int status = serveGroupService.save(from.toDO());
        if (status > 0) {
            return ApiResult.ofData("Saved serve group information successfully.");
        }
        return ApiResult.ofError(StateCode.BizError, "Saved serve group information fail.");
    }

    /**
     * Moving the group to new parent.
     *
     * @param groupId     Long
     * @param newParentId Long
     */
    @PostMapping("/group/move/{groupId}/{newParentId}")
    public ApiResult moveGroup(@PathVariable(name = "groupId") Long groupId, @PathVariable(name = "newParentId") Long newParentId) {
        int status = serveGroupService.moveGroup(groupId, newParentId);
        if (status > 0) {
            return ApiResult.ofData("Move serve group successfully.");
        }
        return ApiResult.ofError(StateCode.BizError, "Move serve group fail.");
    }

    /**
     * Deleted serve group information.
     */
    @PostMapping("/group/del/{groupId}")
    public ApiResult<String> doDelServeGroup(@PathVariable(name = "groupId") Long groupId) {
        int status = serveGroupService.deleteById(groupId);
        if (status > 0) {
            return ApiResult.ofData("Deleted serve group information successfully.");
        }
        return ApiResult.ofError(StateCode.BizError, "Deleted serve group information fail.");
    }

    /**
     * Building a grouping tree.
     */
    @PostMapping("/group/tree")
    public ApiResult getGroupTree(@RequestBody @Validated(value = ServeGroupForm.TreeCheck.class) ServeGroupForm from) {

        List<TreeNodeVO> treeNodes = serveGroupService.genServeGroupTree(
                ServeType.getByCode(from.getServeType()),
                from.isAddChildrenSize(),
                from.isAddRootNode(),
                from.isAddArchiveNode(),
                from.isAddPersonalNode()
        );

        return ApiResult.ofData(treeNodes);
    }


}