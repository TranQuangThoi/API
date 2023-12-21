package com.techmarket.api.mapper;

import com.techmarket.api.dto.cart.CartDto;
import com.techmarket.api.model.Cart;
import org.mapstruct.*;


@Mapper(componentModel = "spring",uses = {AccountMapper.class, UserMapper.class})
public interface CartMapper {

    @Mapping(source = "user",target = "user" ,qualifiedByName = "fromUserToUserDtoAutoComplete")
    @Mapping(source = "totalProduct",target = "totalProduct")
    @Named("fromEntityToCartDto")
    CartDto fromEntityToDto(Cart cart);


}
