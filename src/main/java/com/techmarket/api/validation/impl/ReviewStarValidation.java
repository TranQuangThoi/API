package com.techmarket.api.validation.impl;

import com.techmarket.api.constant.UserBaseConstant;
import com.techmarket.api.validation.ReviewStar;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class ReviewStarValidation implements ConstraintValidator<ReviewStar,Integer> {
    private boolean allowNull;

    @Override
    public void initialize(ReviewStar constraintAnnotation) { allowNull = constraintAnnotation.allowNull(); }

    @Override
    public boolean isValid(Integer reviewStar, ConstraintValidatorContext constraintValidatorContext) {
        if(reviewStar == null && allowNull) {
            return true;
        }
        if(!Objects.equals(reviewStar, UserBaseConstant.REVIEW_ONE_STAR) &&
                !Objects.equals(reviewStar, UserBaseConstant.REVIEW_TWO_STAR) &&
                !Objects.equals(reviewStar, UserBaseConstant.REVIEW_THREE_STAR) &&
                !Objects.equals(reviewStar, UserBaseConstant.REVIEW_FOUR_STAR) &&
                !Objects.equals(reviewStar, UserBaseConstant.REVIEW_FIVE_STAR)) {
            return false;
        }
        return true;
    }
}
