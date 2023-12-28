package com.techmarket.api.mapper;

import com.techmarket.api.dto.product.ProductDto;
import com.techmarket.api.dto.product.RateProductDto;
import com.techmarket.api.form.product.CreateProductForm;
import com.techmarket.api.form.product.UpdateProductForm;
import com.techmarket.api.model.Product;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",  uses = {CategoryMapper.class, BrandMapper.class})
public interface ProductMapper {

    @Mapping(source = "name", target = "name")
    @Mapping(source = "image", target = "image")
    @Mapping(source = "status",target = "status")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "saleOff",target = "saleOff")
    @Named("toProductEntity")
    @BeanMapping(ignoreByDefault = true)
    Product fromCreateProductToEntity(CreateProductForm createProductForm);

    @Mapping(source = "modifiedDate", target = "modifiedDate")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "image", target = "image")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "saleOff",target = "saleOff")
    @Mapping(source = "soldAmount",target = "soldAmount")
    @Mapping(source = "totalReview",target = "totalReview")
    @Mapping(source = "totalInStock",target = "totalInStock")
    @Mapping(source = "avgStart",target = "avgStart")
    @Mapping(source = "brand", target = "brandDto",qualifiedByName="fromEntityToBrandDtoAuto")
    @Mapping(source = "category",target = "categoryDto",qualifiedByName="fromCategoryToCompleteDto")
    @Named("fromEntityToProductDto")
    @BeanMapping(ignoreByDefault = true)
    ProductDto fromEntityToProductDto(Product product);

    @BeanMapping(ignoreByDefault = true)
    @IterableMapping(elementTargetType = ProductDto.class,qualifiedByName = "fromEntityToProductDto")
    List<ProductDto> fromEntityToListProductDto(List<Product> products);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "image", target = "image")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "saleOff",target = "saleOff")
    @Mapping(source = "soldAmount",target = "soldAmount")
    @Mapping(source = "totalReview",target = "totalReview")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "totalInStock",target = "totalInStock")
    @Mapping(source = "avgStart",target = "avgStart")
    @Mapping(source = "brand", target = "brandDto",qualifiedByName="fromEntityToBrandDtoAuto")
    @Mapping(source = "category",target = "categoryDto",qualifiedByName="fromCategoryToCompleteDto")
    @Named("fromEntityToProductAutoDto")
    @BeanMapping(ignoreByDefault = true)
    ProductDto fromEntityToProductAutoDto(Product product);

    @BeanMapping(ignoreByDefault = true)
    @IterableMapping(elementTargetType = ProductDto.class,qualifiedByName = "fromEntityToProductAutoDto")
    List<ProductDto> fromEntityToListProductAutoDto(List<Product> products);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "image", target = "image")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "saleOff",target = "saleOff")
    @BeanMapping(ignoreByDefault = true)
    void fromUpdateToEntityProduct(UpdateProductForm updateProductForm, @MappingTarget Product product);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "image", target = "image")
    @Named("ToProductRateDto")
    @BeanMapping(ignoreByDefault = true)
    RateProductDto toProductRateDto(Product product);

    @BeanMapping(ignoreByDefault = true)
    @IterableMapping(elementTargetType = RateProductDto.class,qualifiedByName = "ToProductRateDto")
    List<RateProductDto> toProductRateListDto(List<Product> products);

}
