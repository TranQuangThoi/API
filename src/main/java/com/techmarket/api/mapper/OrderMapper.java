package com.techmarket.api.mapper;

import com.techmarket.api.dto.order.OrderDto;
import com.techmarket.api.form.order.CreateOrderForUser;
import com.techmarket.api.form.order.CreateOrderForm;
import com.techmarket.api.form.order.UpdateMyOrderForm;
import com.techmarket.api.form.order.UpdateOrder;
import com.techmarket.api.model.Address;
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
    @Mapping(source = "email",target = "email")
    @Named("toProductEntity")
    @BeanMapping(ignoreByDefault = true)
    Order fromCreateOrderToEntity(CreateOrderForm createOrderForm);

    @Mapping(source = "note", target = "note")
    @Mapping(source = "paymentMethod", target = "paymentMethod")
    @Mapping(source = "receiver", target = "receiver")
    @Mapping(source = "email",target = "email")
    @BeanMapping(ignoreByDefault = true)
    Order fromCreateOrderforUserToEntity(CreateOrderForUser createOrderForUser);


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
    @Mapping(source = "state",target = "state")
    @Mapping(source = "expectedDeliveryDate",target = "expectedDeliveryDate")
    @Mapping(source = "voucherId",target = "voucherId")
    @Mapping(source = "orderCode",target = "orderCode")
    @Named("fromOrderToDto")
    @BeanMapping(ignoreByDefault = true)
    OrderDto fromOrderToDto(Order order);

    @BeanMapping(ignoreByDefault = true)
    @IterableMapping(elementTargetType = OrderDto.class,qualifiedByName = "fromOrderToDto")
    List<OrderDto> fromEntityToListOrderDto(List<Order> orders);

    @Mapping(source = "expectedDeliveryDate",target = "expectedDeliveryDate")
    @Mapping(source = "state",target = "state")
    @Mapping(source = "isPaid",target = "isPaid")
    @BeanMapping(ignoreByDefault = true)
    void fromUpdateToOrderEntity(UpdateOrder updateOrder, @MappingTarget Order order);


    @Mapping(source = "note", target = "note")
    @Mapping(source = "address",target = "address")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "receiver", target = "receiver")
    @Mapping(source = "district",target = "district")
    @Mapping(source = "ward", target = "ward")
    @Mapping(source = "province",target = "province")
    @BeanMapping(ignoreByDefault = true)
    void fromUpdateMyOrderToEntity(UpdateMyOrderForm updateMyOrderForm, @MappingTarget Order order);

}
