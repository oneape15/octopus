package com.oneape.octopus.controller.system;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.oneape.octopus.commons.cause.StateCode;
import com.oneape.octopus.commons.cause.UnauthorizedException;
import com.oneape.octopus.controller.SessionThreadLocal;
import com.oneape.octopus.controller.system.form.UserForm;
import com.oneape.octopus.domain.system.UserDO;
import com.oneape.octopus.dto.system.UserDTO;
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
        String token = accountService.login(form.getUsername(), form.getPassword());
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
    @PostMapping(value = "/outLogin")
    public ApiResult doOutLogin(@RequestBody @Validated(value = UserForm.LoginCheck.class) UserForm form) {
        int status = accountService.outLogin(form.getId());
        if (status > 0) {
            return ApiResult.ofMessage("login out success.");
        }

        return ApiResult.ofError(-1, "login out fail.");
    }

    /**
     * New registered user information
     */
    @PostMapping("/reg")
    public ApiResult<String> regUser(@RequestBody @Validated(value = UserForm.RegCheck.class) UserForm form) {
        int status = accountService.addUser(form.toDO());
        if (status <= 0) {
            return ApiResult.ofError(StateCode.RegError);
        }

        return ApiResult.ofMessage("registered success.");
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
     * Get current user information.
     */
    @PostMapping("/getCurrent")
    public ApiResult<UserDTO> getCurrent() {
        UserDTO vo = accountService.getCurrentUser();
        if (vo == null) {
            throw new UnauthorizedException();
        }
        return ApiResult.ofData(vo);
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
    @PostMapping("/delUser")
    public ApiResult<String> removeUser(@RequestBody @Validated(value = UserForm.DelCheck.class) UserForm form) {
        int status = accountService.removeUsers(form.getUserIds());
        if (status > 0) {
            return ApiResult.ofData("Delete user successfully.");
        }
        return ApiResult.ofError(StateCode.BizError, "Delete user fail.");
    }

    /**
     * Add user.
     */
    @PostMapping("/addUser")
    public ApiResult<String> addUser(@RequestBody @Validated(value = UserForm.AddCheck.class) UserForm form) {
        int status = accountService.addUser(form.toDO());
        if (status > 0) {
            return ApiResult.ofData("Add user successfully.");
        }
        return ApiResult.ofError(StateCode.BizError, "Add user fail.");
    }

    /**
     * Modified user information.
     */
    @PostMapping("/updateUser")
    public ApiResult<String> updateUser(@RequestBody @Validated(value = UserForm.AddCheck.class) UserForm form) {
        int status = accountService.save(form.toDO());
        if (status > 0) {
            return ApiResult.ofData("Modified user information successfully.");
        }
        return ApiResult.ofError(StateCode.BizError, "Modified user information fail.");
    }

    @PostMapping("/lockUser/{userId}")
    public ApiResult lockUser(@PathVariable(name = "userId") Long userId) {
        int status = accountService.changeUserStatus(userId, 1);
        if (status > 0) {
            return ApiResult.ofData("Successfully lock the user.");
        }
        return ApiResult.ofError(StateCode.BizError, "Failed to lock user.");
    }

    @PostMapping("/unlockUser/{userId}")
    public ApiResult unlockUser(@PathVariable(name = "userId") Long userId) {
        int status = accountService.changeUserStatus(userId, 0);
        if (status > 0) {
            return ApiResult.ofData("Successfully unlock the user.");
        }
        return ApiResult.ofError(StateCode.BizError, "Failed to unlock user.");
    }

}
