package com.techmarket.api.dto.product;

import com.techmarket.api.dto.ABasicAdminDto;
import com.techmarket.api.dto.productVariant.ProductVariantDto;
import lombok.Data;

import java.util.List;

@Data
public class ProductRelatedDto  extends ABasicAdminDto {

    private String name;
    private Double price;
    private String description;
    private String image;
    private Integer totalInStock;
    private Double saleOff;
    private List<ProductVariantDto> listProductVariant;
}
