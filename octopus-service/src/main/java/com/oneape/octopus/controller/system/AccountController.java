package com.oneape.octopus.controller.system;

import com.oneape.octopus.model.DO.UserDO;
import com.oneape.octopus.model.VO.ApiResult;
import com.oneape.octopus.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @RequestMapping("/getByToken")
    public ApiResult<UserDO> getByToken() {
        UserDO userDO = accountService.getByUsername("user");

        return ApiResult.ofData(userDO);
    }

}
