package com.techmarket.api.notification.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ChangeStateNotification {
    @NotNull(message = "id cant not be null")
    @ApiModelProperty( name="id",required = true)
    private Long id;
}
