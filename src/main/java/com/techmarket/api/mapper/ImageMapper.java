package com.techmarket.api.mapper;

import com.techmarket.api.dto.brand.BrandDto;
import com.techmarket.api.dto.images.ImagesDto;
import com.techmarket.api.model.Brand;
import com.techmarket.api.model.Images;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ImageMapper {


    @Mapping(source = "id", target = "id")
    @Mapping(source = "link", target = "link")
    @Named("fromEntityToImageDto")
    @BeanMapping(ignoreByDefault = true)
    ImagesDto fromEntityToImageDto(Images Images);

    @BeanMapping(ignoreByDefault = true)
    @IterableMapping(elementTargetType = ImagesDto.class,qualifiedByName = "fromEntityToImageDto")
    List<ImagesDto> fromEntityToListImageDto(List<Images> images);
}
