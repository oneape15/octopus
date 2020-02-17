package com.oneape.octopus.controller.system;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.oneape.octopus.common.StateCode;
import com.oneape.octopus.common.UnauthorizedException;
import com.oneape.octopus.controller.system.form.UserForm;
import com.oneape.octopus.model.DO.system.UserDO;
import com.oneape.octopus.model.VO.ApiResult;
import com.oneape.octopus.model.VO.MenuVO;
import com.oneape.octopus.model.VO.UserVO;
import com.oneape.octopus.service.AccountService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 账号相关
 */
@RestController
@RequestMapping("/account")
public class AccountController {

    @Resource
    private AccountService accountService;

    /**
     * 用户登录
     */
    @RequestMapping(value = "/login", method = {RequestMethod.POST, RequestMethod.GET})
    public ApiResult<UserVO> doLogin(@RequestBody @Validated(value = UserForm.LoginCheck.class) UserForm form) {
        UserVO vo = accountService.login(form.getUsername(), form.getPassword());
        if (vo == null) {
            return ApiResult.ofError(StateCode.LoginError);
        }
        return ApiResult.ofData(vo);
    }

    /**
     * 注册用户
     */
    @PostMapping("/reg")
    public ApiResult<String> regUser(@RequestBody @Validated(value = UserForm.RegCheck.class) UserForm form) {
        int status = accountService.addUser(form.toVO());
        if (status <= 0) {
            return ApiResult.ofError(StateCode.RegError);
        }

        return ApiResult.ofMessage("注册成功");
    }

    /**
     * 获取列表
     */
    @PostMapping("/list")
    public ApiResult<PageInfo<UserVO>> list(@RequestBody @Validated UserForm form) {
        PageHelper.startPage(form.getCurrentPage(), form.getPageSize());
        List<UserVO> vos = accountService.find(form.toDO());
        return ApiResult.ofData(new PageInfo<>(vos));
    }

    /**
     * 获取当前用户信息
     */
    @PostMapping("/getCurrent")
    public ApiResult<UserVO> getCurrent() {
        UserVO vo = accountService.getCurrentUser();
        if (vo == null) {
            throw new UnauthorizedException();
        }
        return ApiResult.ofData(vo);
    }

    /**
     * 获取当前用户拥有的菜单列表
     */
    @PostMapping("/menuTree")
    public ApiResult<List<MenuVO>> getCurrentUserMenu() {
        List<MenuVO> menus = accountService.getCurrentMenus();
        return ApiResult.ofData(menus);
    }

    /**
     * 获取所有用户基本信息
     */
    @PostMapping("/users")
    public ApiResult<List<UserVO>> getAllUsers() {
        return ApiResult.ofData(accountService.find(new UserDO()));
    }

    /**
     * 重置密码
     */
    @PostMapping("/resetPwd")
    public ApiResult<String> resetPwd(@RequestBody @Validated(value = UserForm.KeyCheck.class) UserForm form) {
        int status = accountService.resetPwd(form.getUserId());
        if (status > 0) {
            return ApiResult.ofData("重置密码成功");
        }
        return ApiResult.ofError(StateCode.BizError, "重置密码失败");
    }

}
