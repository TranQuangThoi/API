package com.techmarket.api.form.productVariant;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@ApiModel
public class UpdateProductVariantForm {

    @NotNull(message = "Id cant not be null")
    @ApiModelProperty(name = "id",required = true)
    private Long id;
    @ApiModelProperty(name = "price")
    private Double price;
    @NotEmpty(message = "color cant not be empty")
    @ApiModelProperty(name = "color", required = true)
    private String color;
    @ApiModelProperty(name = "image")
    private String image;
    @ApiModelProperty(name = "totalStock")
    private Integer totalStock;
    @NotNull(message = "status cant not be null")
    @ApiModelProperty(name = "status",required = true)
    private Integer status;
}
