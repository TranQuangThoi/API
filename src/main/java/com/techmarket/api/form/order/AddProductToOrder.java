package com.techmarket.api.form.order;

import com.techmarket.api.model.ProductVariant;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AddProductToOrder {
    @NotNull(message = "productVariantId can not be null")
    @ApiModelProperty(name = "productVariantId", required = true)
    private Long productVariantId;
    @NotNull(message = "quantity can not be null")
    @ApiModelProperty(name = "quantity", required = true)
    private Integer quantity;
    @NotNull(message = "price can not be null")
    @ApiModelProperty(name = "price", required = true)
    private Double price;
    @NotNull(message = "productName can not be null")
    @ApiModelProperty(name = "productName", required = true)
    private String productName;
    @NotNull(message = "color can not be null")
    @ApiModelProperty(name = "color", required = true)
    private String color;
}
