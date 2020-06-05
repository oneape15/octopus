package com.oneape.octopus.model.DO.system;

import com.oneape.octopus.model.DO.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class RoleDO extends BaseDO {
    /**
     * The role name
     */
    private String  name;
    /**
     * The role code
     */
    private String  code;
    /**
     * Role type: 0-normal; 1 - Default role; 3 - System role.
     */
    private Integer type;
    /**
     * description.
     */
    private String  comment;

}
