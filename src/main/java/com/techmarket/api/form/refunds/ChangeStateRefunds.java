package com.techmarket.api.form.refunds;

import com.techmarket.api.validation.RefundsState;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel
public class ChangeStateRefunds {

    @NotNull(message = "id cant not be null")
    @ApiModelProperty(name = "id", required = true)
    private Long id;

    @ApiModelProperty(name = "state",required = true)
    @RefundsState
    private Integer state;
}
