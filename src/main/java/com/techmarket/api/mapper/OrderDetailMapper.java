package com.techmarket.api.mapper;

import com.techmarket.api.dto.orderDetail.OrderDetailDto;
import com.techmarket.api.model.OrderDetail;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderDetailMapper {


    @Mapping(source = "id", target = "id")
    @Mapping(source = "productVariantId", target = "productVariantId")
    @Mapping(source = "name",target = "name")
    @Mapping(source = "color", target = "color")
    @Mapping(source = "amount", target = "amount")
    @Mapping(source = "price", target = "price")
    @Named("fromEntityToOrderDetailDto")
    @BeanMapping(ignoreByDefault = true)
    OrderDetailDto fromEntityToOrderDetailDto(OrderDetail orderDetail);

    @BeanMapping(ignoreByDefault = true)
    @IterableMapping(elementTargetType = OrderDetailDto.class,qualifiedByName = "fromEntityToOrderDetailDto")
    List<OrderDetailDto> fromEntityToListOrderDetailDto(List<OrderDetail> orderDetail);
}
