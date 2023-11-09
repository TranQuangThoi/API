package com.techmarket.api.mapper;

import com.techmarket.api.dto.brand.BrandDto;
import com.techmarket.api.form.brand.CreateBrandForm;
import com.techmarket.api.form.brand.UpdateBrandForm;
import com.techmarket.api.model.Brand;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BrandMapper {

    @Mapping(source = "name", target = "name")
    @Mapping(source = "image", target = "image")
    @Mapping(source = "status",target = "status")
    @Named("toBrandEntity")
    @BeanMapping(ignoreByDefault = true)
    Brand fromCreateBrandToEntity(CreateBrandForm createBrandForm);
    @Mapping(source = "modifiedDate", target = "modifiedDate")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "image", target = "image")
    @Mapping(source = "status", target = "status")
    @Named("fromEntityToBrandDto")
    @BeanMapping(ignoreByDefault = true)
    BrandDto fromEntityToBrandDto(Brand brand);

    @BeanMapping(ignoreByDefault = true)
    @IterableMapping(elementTargetType = BrandDto.class,qualifiedByName = "fromEntityToBrandDto")
    List<BrandDto> fromEntityToListBrandDto(List<Brand> brands);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "image", target = "image")
    @Named("fromEntityToBrandDtoAuto")
    @BeanMapping(ignoreByDefault = true)
    BrandDto fromEntityToBrandDtoAuto(Brand brand);

    @BeanMapping(ignoreByDefault = true)
    @IterableMapping(elementTargetType = BrandDto.class,qualifiedByName = "fromEntityToBrandDtoAuto")
    List<BrandDto> fromEntityToListBrandDtoAuto(List<Brand> brands);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "image", target = "image")
    @Mapping(source = "status", target = "status")
    void fromUpdateToEntityBrand(UpdateBrandForm updateBrandForm,@MappingTarget Brand brand);

}
