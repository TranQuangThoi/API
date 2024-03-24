package com.techmarket.api.mapper;

import com.techmarket.api.dto.refunds.RefundsDto;
import com.techmarket.api.form.refunds.CreateRefundsForm;
import com.techmarket.api.model.Refunds;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RefundsMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "order.id", target = "orderId")
    @Mapping(source = "state", target = "state")
    @Mapping(source = "cash", target = "cash")
    @Mapping(source = "bank", target = "bank")
    @Mapping(source = "accountNumber", target = "accountNumber")
    @Mapping(source = "fee", target = "fee")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "name", target = "name")
    @Named("fromEntityToRefundsDto")
    RefundsDto fromEntityToRefundsDto(Refunds refunds);

    @BeanMapping(ignoreByDefault = true)
    @IterableMapping(elementTargetType = RefundsDto.class,qualifiedByName = "fromEntityToRefundsDto")
    List<RefundsDto> fromEntityToListRefundsDto(List<Refunds> refundsList);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "cash", target = "cash")
    @Mapping(source = "bank", target = "bank")
    @Mapping(source = "accountNumber", target = "accountNumber")
    @Mapping(source = "fee", target = "fee")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "name", target = "name")
    @Named("fromEntityToRefundsDto")
    Refunds fromCreateFormToEntity(CreateRefundsForm refunds);
}
