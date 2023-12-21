package com.techmarket.api.dto.cart.cartDetail;

import com.techmarket.api.dto.ABasicAdminDto;
import com.techmarket.api.dto.cart.CartDto;
import com.techmarket.api.dto.productVariant.ProductVariantDto;
import lombok.Data;

@Data
public class CartDetailDto extends ABasicAdminDto {

    private ProductVariantDto productVariantDto;
    private CartDto cart;
    private Integer quantity;


}
