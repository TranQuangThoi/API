package com.techmarket.api.dto.cart;

import com.techmarket.api.dto.cart.cartDetail.CartDetailDto;
import com.techmarket.api.dto.user.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDto {

    private List<CartDetailDto> cartDetailDtos;
    private Integer totalProduct;
    private UserDto user;


}
