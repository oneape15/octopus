package com.oneape.octopus.controller.system;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.oneape.octopus.commons.cause.StateCode;
import com.oneape.octopus.controller.SessionThreadLocal;
import com.oneape.octopus.controller.system.form.UserForm;
import com.oneape.octopus.domain.system.UserDO;
import com.oneape.octopus.dto.system.AppType;
import com.oneape.octopus.dto.system.UserDTO;
import com.oneape.octopus.dto.system.UserStatus;
import com.oneape.octopus.model.vo.ApiResult;
import com.oneape.octopus.service.system.AccountService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * The user option Controller.
 */
@RestController
@RequestMapping("/account")
public class AccountController {

    @Resource
    private AccountService accountService;

    /**
     * The user login option.
     */
    @PostMapping(value = "/login")
    public ApiResult<String> doLogin(@RequestBody @Validated(value = UserForm.LoginCheck.class) UserForm form) {
        String token = accountService.login(form.getUsername(), form.getPassword(), AppType.MANAGE);
        if (StringUtils.isBlank(token)) {
            return ApiResult.ofError(StateCode.LoginError);
        }
        return ApiResult.ofData(token);
    }

    /**
     * Get full user information.
     */
    @GetMapping(value = "/currentUser")
    public ApiResult<UserDTO> getCurrentUser() {
        Long userId = SessionThreadLocal.getUserId();
        UserDTO userDTO = accountService.getFullInformationById(userId);

        return ApiResult.ofData(userDTO);
    }

    /**
     * The user login out option.
     */
    @PostMapping(value = "/logout")
    public ApiResult doOutLogin() {
        Long userId = SessionThreadLocal.getUserId();
        int status = accountService.logout(userId, AppType.MANAGE);
        if (status > 0) {
            return ApiResult.ofMessage("login out success.");
        }

        return ApiResult.ofError(-1, "login out fail.");
    }

    /**
     * Paging for user information
     */
    @PostMapping("/list")
    public ApiResult<PageInfo<UserDO>> list(@RequestBody @Validated UserForm form) {
        PageHelper.startPage(form.getCurrent(), form.getPageSize());
        List<UserDO> list = accountService.find(form.toDO());
        return ApiResult.ofData(new PageInfo<>(list));
    }

    /**
     * Get all user information.
     */
    @GetMapping("/users")
    public ApiResult<List<UserDO>> getAllUsers() {
        return ApiResult.ofData(accountService.find(new UserDO()));
    }

    /**
     * Reset user password.
     */
    @PostMapping("/resetPwd")
    public ApiResult<String> resetPwd(@RequestBody @Validated(value = UserForm.KeyCheck.class) UserForm form) {
        int status = accountService.resetPwd(form.getId());
        if (status > 0) {
            return ApiResult.ofData("Reset user password successfully.");
        }
        return ApiResult.ofError(StateCode.BizError, "Reset user password fail.");
    }

    /**
     * Delete user.
     */
    @PostMapping("/del/{userId}")
    public ApiResult<String> removeUser(@PathVariable(name = "userId") Long userId) {
        int status = accountService.deleteById(userId);
        if (status > 0) {
            return ApiResult.ofData("Delete user successfully.");
        }
        return ApiResult.ofError(StateCode.BizError, "Delete user fail.");
    }

    /**
     * Save user
     */
    @PostMapping("/save")
    public ApiResult<String> saveUser(@RequestBody @Validated(value = UserForm.SaveCheck.class) UserForm form) {
        int status = accountService.save(form.toDO());
        if (status > 0) {
            return ApiResult.ofData("Add user successfully.");
        }
        return ApiResult.ofError(StateCode.BizError, "Add user fail.");
    }

    @PostMapping("/lockUser/{userId}")
    public ApiResult lockUser(@PathVariable(name = "userId") Long userId) {
        int status = accountService.changeUserStatus(userId, UserStatus.LOCK);
        if (status > 0) {
            return ApiResult.ofData("Successfully lock the user.");
        }
        return ApiResult.ofError(StateCode.BizError, "Failed to lock user.");
    }

    @PostMapping("/unlockUser/{userId}")
    public ApiResult unlockUser(@PathVariable(name = "userId") Long userId) {
        int status = accountService.changeUserStatus(userId, UserStatus.NORMAL);
        if (status > 0) {
            return ApiResult.ofData("Successfully unlock the user.");
        }
        return ApiResult.ofError(StateCode.BizError, "Failed to unlock user.");
    }

    @PostMapping("/saveUserRole")
    public ApiResult saveUserRole(@RequestBody @Validated(value = UserForm.KeyCheck.class) UserForm form) {
        int status = accountService.saveUserRole(form.getId(), form.getRoleIds());
        if (status > 0) {
            return ApiResult.ofData("Successfully unlock the user.");
        }
        return ApiResult.ofError(StateCode.BizError, "Failed to unlock user.");
    }

}
