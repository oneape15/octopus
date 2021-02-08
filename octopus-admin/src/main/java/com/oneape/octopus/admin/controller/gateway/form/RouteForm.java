package com.oneape.octopus.admin.controller.gateway.form;

import com.oneape.octopus.admin.controller.BaseForm;
import com.oneape.octopus.domain.gateway.GatewayRouteDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2021-02-07 15:26.
 * Modify:
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RouteForm extends BaseForm implements Serializable {
    private Long    id;
    @NotNull(message = "{}", groups = {AddCheck.class, EditCheck.class})
    private String  routeId;
    private String  uri;
    private String  predicates;
    private String  filters;
    private Integer order;
    private String  metadata;

    public interface AddCheck {
    }

    public interface EditCheck {

    }

    public GatewayRouteDO toDO() {
        GatewayRouteDO rdo = new GatewayRouteDO();
        BeanUtils.copyProperties(this, rdo);
        return rdo;
    }
}
