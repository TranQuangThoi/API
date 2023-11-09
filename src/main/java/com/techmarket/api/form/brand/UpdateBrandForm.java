package com.techmarket.api.form.brand;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@ApiModel
public class UpdateBrandForm {
    @NotNull(message = "id cant not be null")
    @ApiModelProperty(name = "id", required = true)
    private Long id;
    @NotEmpty(message = "name cant not be null")
    @ApiModelProperty(name = "name", required = true)
    private String name;
    @ApiModelProperty(name = "image")
    private String image;
    @NotNull(message = "status cant not be null")
    @ApiModelProperty(name = "status",required = true)
    private int status ;

}
