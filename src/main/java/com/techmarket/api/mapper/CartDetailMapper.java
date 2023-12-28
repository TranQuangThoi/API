package com.techmarket.api.mapper;

import com.techmarket.api.dto.cart.cartDetail.CartDetailDto;
import com.techmarket.api.form.cart.cartDetail.UpdateCartDetailForm;
import com.techmarket.api.model.CartDetail;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",uses = {CartMapper.class, ProductVariantMapper.class})
public interface CartDetailMapper {
    @Mapping(source = "id",target = "cartDetailId")
    @Mapping(source = "productVariant.product.name",target = "productName")
    @Mapping(source = "quantity",target = "quantity" )
    @Mapping(source = "productVariant.id",target = "productVariantId")
    @Mapping(source = "productVariant.price",target = "price")
    @Mapping(source = "productVariant.color",target = "color")
    @Mapping(source = "productVariant.image",target = "image")
    @Mapping(source = "productVariant.totalStock",target = "totalStock")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromEntityToCartDto")
    CartDetailDto fromEntityToDto(CartDetail cartDetail);

    @BeanMapping(ignoreByDefault = true)
    @IterableMapping(elementTargetType = CartDetailDto.class,qualifiedByName = "fromEntityToCartDto")
    List<CartDetailDto> fromEntityToListCartDetailDto(List<CartDetail>cartDetails);
    @Mapping(source = "quantity",target = "quantity" )
    @BeanMapping(ignoreByDefault = true)
    void updateCartDetail(UpdateCartDetailForm updateCartDetailForm , @MappingTarget CartDetail cartDetail);


}
