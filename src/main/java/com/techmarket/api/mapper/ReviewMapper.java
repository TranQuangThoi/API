package com.techmarket.api.mapper;

import com.techmarket.api.dto.review.MyReviewDto;
import com.techmarket.api.dto.review.ReviewDto;
import com.techmarket.api.form.review.CreateReviewForm;
import com.techmarket.api.form.review.UpdateReviewForm;
import com.techmarket.api.model.Review;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",uses = {AccountMapper.class, UserMapper.class,ProductMapper.class})
public interface ReviewMapper {

    @Mapping(source = "star", target = "star")
    @Mapping(source = "message", target = "message")
    @BeanMapping(ignoreByDefault = true)
    @Named("adminCreateMapping")
    Review fromCreateFormToEntity(CreateReviewForm createForm);

    @Mapping(source = "status", target = "status")
    @BeanMapping(ignoreByDefault = true)
    @Named("adminUpdateMapping")
    void fromUpdateFormToEntityReview(UpdateReviewForm updateForm, @MappingTarget Review review);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "star", target = "star")
    @Mapping(source = "message", target = "message")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "user", target = "userDto", qualifiedByName = "fromUserToUserDtoAutoComplete")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromEntityToReviewDto")
    ReviewDto fromEntityToReviewDto(Review review);

    @IterableMapping(elementTargetType = ReviewDto.class, qualifiedByName = "fromEntityToReviewDto")
    List<ReviewDto> fromEntityListToDtoList(List<Review> reviews);


    @Mapping(source = "id", target = "id")
    @Mapping(source = "star", target = "star")
    @Mapping(source = "message", target = "message")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "product.name",target = "productName")
    @Mapping(source = "product.id",target = "productId")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromEntityToGetMyReviewDto")
    MyReviewDto fromEntityToGetMyDto(Review review);
    @IterableMapping(elementTargetType = MyReviewDto.class, qualifiedByName = "fromEntityToGetMyReviewDto")
    List<MyReviewDto> fromEntityToGetMyReviewDtoList(List<Review> reviews);

}
