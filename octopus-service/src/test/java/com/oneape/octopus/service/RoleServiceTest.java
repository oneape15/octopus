package com.oneape.octopus.service;

import com.oneape.octopus.Application;
import com.oneape.octopus.model.DO.system.RoleDO;
import com.oneape.octopus.service.system.RoleService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
public class RoleServiceTest {

    @Resource
    private RoleService roleService;

    @Test
    public void testRoleInsert() {
        RoleDO model = new RoleDO();
        model.setName("test1");
        model.setCode("xxxx");
        roleService.save(model);
    }
}
