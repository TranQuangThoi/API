package com.techmarket.api.mapper;

import com.techmarket.api.dto.order.OrderDto;
import com.techmarket.api.form.order.CreateOrderForm;
import com.techmarket.api.form.order.UpdateOrder;
import com.techmarket.api.model.Order;

import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(source = "note", target = "note")
    @Mapping(source = "paymentMethod", target = "paymentMethod")
    @Mapping(source = "address",target = "address")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "receiver", target = "receiver")
    @Mapping(source = "district",target = "district")
    @Mapping(source = "ward", target = "ward")
    @Mapping(source = "province",target = "province")
    @Named("toProductEntity")
    @BeanMapping(ignoreByDefault = true)
    Order fromCreateOrderToEntity(CreateOrderForm createOrderForm);

    @Mapping(source = "modifiedDate", target = "modifiedDate")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "id", target = "id")
    @Mapping(source = "note", target = "note")
    @Mapping(source = "paymentMethod", target = "paymentMethod")
    @Mapping(source = "address",target = "address")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "receiver", target = "receiver")
    @Mapping(source = "district",target = "district")
    @Mapping(source = "ward", target = "ward")
    @Mapping(source = "province",target = "province")
    @Mapping(source = "totalMoney",target = "totalMoney")
    @Mapping(source = "user.id",target = "userId")
    @Mapping(source = "isPaid",target = "isPaid")
    @Mapping(source = "isDelivery",target = "isDelivery")
    @Mapping(source = "status",target = "status")
    @Mapping(source = "expectedDeliveryDate",target = "expectedDeliveryDate")
    @Named("fromOrderToDto")
    @BeanMapping(ignoreByDefault = true)
    OrderDto fromOrderToDto(Order order);

    @BeanMapping(ignoreByDefault = true)
    @IterableMapping(elementTargetType = OrderDto.class,qualifiedByName = "fromOrderToDto")
    List<OrderDto> fromEntityToListOrderDto(List<Order> orders);

    @Mapping(source = "isPaid",target = "isPaid")
    @Mapping(source = "isDelivery",target = "isDelivery")
    @Mapping(source = "expectedDeliveryDate",target = "expectedDeliveryDate")
    @Mapping(source = "status",target = "status")
    @BeanMapping(ignoreByDefault = true)
    void fromUpdateToOrderEntity(UpdateOrder updateOrder, @MappingTarget Order order);


}
