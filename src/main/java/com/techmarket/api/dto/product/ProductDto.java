package com.techmarket.api.dto.product;

import com.techmarket.api.dto.ABasicAdminDto;
import com.techmarket.api.dto.brand.BrandDto;
import com.techmarket.api.dto.category.CategoryDto;
import lombok.Data;

@Data
public class ProductDto extends ABasicAdminDto{

    private String name;
    private Double price;
    private String description;
    private String image;
    private Integer totalInStock;
    private Integer totalReview;
    private Double saleOff;
    private Integer soldAmount;
    private BrandDto brandDto;
    private CategoryDto categoryDto;
}
