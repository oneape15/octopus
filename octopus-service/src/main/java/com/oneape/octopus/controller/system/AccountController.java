package com.oneape.octopus.controller.system;

import com.oneape.octopus.common.StateCode;
import com.oneape.octopus.controller.system.form.UserForm;
import com.oneape.octopus.model.VO.ApiResult;
import com.oneape.octopus.model.VO.UserVO;
import com.oneape.octopus.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    /**
     * 用户登录
     *
     * @param form UserForm
     * @return ApiResult
     */
    @PostMapping("/login")
    public ApiResult<UserVO> doLogin(@RequestBody @Validated(value = UserForm.LoginCheck.class) UserForm form) {
        UserVO vo = accountService.login(form.getUsername(), form.getPassword());
        if (vo == null) {
            return ApiResult.ofError(StateCode.LoginError);
        }
        return ApiResult.ofData(vo);
    }

    /**
     * 注册用户
     *
     * @param form UserForm
     * @return ApiResult
     */
    @PostMapping("/reg")
    public ApiResult<String> regUser(@RequestBody @Validated(value = UserForm.RegCheck.class) UserForm form) {
        int status = accountService.addUser(form.toVO());
        if (status <= 0) {
            return ApiResult.ofError(StateCode.RegError);
        }

        return ApiResult.ofMessage("注册成功");
    }

}
