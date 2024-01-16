package com.techmarket.api.mapper;

import com.techmarket.api.dto.review.ReviewDto;
import com.techmarket.api.dto.voucher.VoucherDto;
import com.techmarket.api.form.voucher.CreateVoucherForm;
import com.techmarket.api.form.voucher.UpdateVoucherForm;
import com.techmarket.api.model.Review;
import com.techmarket.api.model.Voucher;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VoucherMapper {

    @Mapping(source = "title", target = "title")
    @Mapping(source = "content", target = "content")
    @Mapping(source = "percent", target = "percent")
    @Mapping(source = "expired", target = "expired")
    @Mapping(source = "kind", target = "kind")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "amount",target = "amount")
    @BeanMapping(ignoreByDefault = true)
    @Named("createVoucher")
    Voucher fromCreateFormToEntity(CreateVoucherForm createForm);
    @Mapping(source = "id", target = "id")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "content", target = "content")
    @Mapping(source = "percent", target = "percent")
    @Mapping(source = "expired", target = "expired")
    @Mapping(source = "kind", target = "kind")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "modifiedDate", target = "modifiedDate")
    @Mapping(source = "amount",target = "amount")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromEntityToDto")
    VoucherDto fromEntityToDto(Voucher voucher);
    @IterableMapping(elementTargetType = VoucherDto.class, qualifiedByName = "fromEntityToDto")
    List<VoucherDto> fromEntityListToDtoList(List<Voucher> vouchers);

    @Mapping(source = "title", target = "title")
    @Mapping(source = "content", target = "content")
    @Mapping(source = "percent", target = "percent")
    @Mapping(source = "expired", target = "expired")
    @Mapping(source = "kind", target = "kind")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "amount",target = "amount")
    @BeanMapping(ignoreByDefault = true)
    @Named("adminUpdateMapping")
    void fromUpdateFormToEntityVoucher(UpdateVoucherForm updateForm, @MappingTarget Voucher voucher);


}
