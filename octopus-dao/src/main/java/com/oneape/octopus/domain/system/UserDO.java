package com.oneape.octopus.domain.system;

import com.oneape.octopus.commons.enums.EntityColumn;
import com.oneape.octopus.domain.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * User information table DO.
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserDO extends BaseDO {
    /**
     * login name
     */
    private           String  username;
    /**
     * The user nickname
     */
    private           String  nickname;
    /**
     * The user login password.
     */
    private transient String  password;
    /**
     * avatar
     */
    private           String  avatar;
    /**
     * cell-phone number
     */
    private           String  phone;
    /**
     * email address.
     */
    private           String  email;
    /**
     * The department Id of the user.
     */
    @EntityColumn(name = "dept_id")
    private           String  deptId;
    /**
     * Account status 0 - normal; 1 - lock.
     */
    private           Integer status;

    /**
     * User's gender. 0 - female; 1 - man
     */
    private Integer gender;

    public UserDO(String username) {
        this.username = username;
    }

    public UserDO(Long id) {
        setId(id);
    }
}
