package com.techmarket.api.mapper;

import com.techmarket.api.dto.cart.cartDetail.CartDetailDto;
import com.techmarket.api.model.CartDetail;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",uses = {CartMapper.class, ProductVariantMapper.class})
public interface CartDetailMapper {

    @Mapping(source = "quantity",target = "quantity" )
    @Mapping(source = "productVariant",target = "productVariantDto",qualifiedByName = "fromEntityToProVariantDtoAuto")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromEntityToCartDto")
    CartDetailDto fromEntityToDto(CartDetail cartDetail);

    @BeanMapping(ignoreByDefault = true)
    @IterableMapping(elementTargetType = CartDetailDto.class,qualifiedByName = "fromEntityToCartDto")
    List<CartDetailDto> fromEntityToListCartDetailDto(List<CartDetail>cartDetails);

}
