package com.oneape.octopus.service;

import com.oneape.octopus.Application;
import com.oneape.octopus.model.DO.RoleDO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
public class RoleServiceTest {

    @Autowired
    private RoleService roleService;

    @Test
    public void testRoleInsert() {
        RoleDO model = new RoleDO();
        model.setName("test1");
        model.setCode("xxxx");
        roleService.insert(model);
    }
}
