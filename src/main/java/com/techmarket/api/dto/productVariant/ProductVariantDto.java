package com.techmarket.api.dto.productVariant;

import com.techmarket.api.dto.ABasicAdminDto;
import com.techmarket.api.dto.product.ProductDto;
import lombok.Data;

@Data
public class ProductVariantDto extends ABasicAdminDto {

    private Double price;
    private String color;
    private String image;
    private Integer totalStock;
    private Integer status;
}
