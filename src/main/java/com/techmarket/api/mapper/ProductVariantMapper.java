package com.techmarket.api.mapper;

import com.techmarket.api.dto.product.ProductDto;
import com.techmarket.api.dto.productVariant.ProductVariantDto;
import com.techmarket.api.form.product.UpdateProductForm;
import com.techmarket.api.form.productVariant.CreateProductVariantForm;
import com.techmarket.api.form.productVariant.UpdateProductVariantForm;
import com.techmarket.api.model.Product;
import com.techmarket.api.model.ProductVariant;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",  uses = {ProductMapper.class})
public interface ProductVariantMapper {
    @Mapping(source = "price", target = "price")
    @Mapping(source = "color", target = "color")
    @Mapping(source = "image", target = "image")
    @Mapping(source = "status",target = "status")
    @Mapping(source = "totalStock",target = "totalStock")
    @Named("toProductVariantEntity")
    @BeanMapping(ignoreByDefault = true)
    ProductVariant fromCreateProVariantToEntity(CreateProductVariantForm createProductVariantForm);

    @Mapping(source = "modifiedDate", target = "modifiedDate")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "id", target = "id")
    @Mapping(source = "image", target = "image")
    @Mapping(source = "color", target = "color")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "totalStock", target = "totalStock")
    @Mapping(source = "price",target = "price")
    @Mapping(source = "product", target = "productDto",qualifiedByName="fromEntityToProductAutoDto")
    @Named("fromEntityToProVariantDto")
    @BeanMapping(ignoreByDefault = true)
    ProductVariantDto fromEntityToProVariantDto(ProductVariant productVariant);

    @BeanMapping(ignoreByDefault = true)
    @IterableMapping(elementTargetType = ProductVariantDto.class,qualifiedByName = "fromEntityToProVariantDto")
    List<ProductVariantDto> fromEntityToListProVariantDto(List<ProductVariant> productVariants);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "image", target = "image")
    @Mapping(source = "color", target = "color")
    @Mapping(source = "totalStock", target = "totalStock")
    @Mapping(source = "price",target = "price")
    @Mapping(source = "product", target = "productDto",qualifiedByName="fromEntityToProductAutoDto")
    @Named("fromEntityToProVariantDtoAuto")
    @BeanMapping(ignoreByDefault = true)
    ProductVariantDto fromEntityToProVariantDtoAuto(ProductVariant productVariant);

    @BeanMapping(ignoreByDefault = true)
    @IterableMapping(elementTargetType = ProductVariantDto.class,qualifiedByName = "fromEntityToProVariantDtoAuto")
    List<ProductVariantDto> fromEntityToListProVariantAutoDto(List<ProductVariant> productVariants);

    @Mapping(source = "image", target = "image")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "color", target = "color")
    @BeanMapping(ignoreByDefault = true)
    void fromUpdateToEntityProViant(UpdateProductVariantForm updateProductVariantForm, @MappingTarget ProductVariant productVariant);

}
