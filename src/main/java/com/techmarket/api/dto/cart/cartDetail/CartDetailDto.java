package com.techmarket.api.dto.cart.cartDetail;

import com.techmarket.api.dto.ABasicAdminDto;
import lombok.Data;

@Data
public class CartDetailDto extends ABasicAdminDto {

    private Long productVariantId;
    private Double price;
    private String color;
    private String image;
    private Integer totalStock;
    private Integer quantity;
    private String productName;
    private Long cartDetailId;
    private Double TotalPriceSell;


}
