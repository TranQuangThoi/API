package com.techmarket.api.controller;

import com.techmarket.api.constant.UserBaseConstant;
import com.techmarket.api.dto.ApiMessageDto;
import com.techmarket.api.dto.ErrorCode;
import com.techmarket.api.dto.ResponseListDto;
import com.techmarket.api.dto.review.MyReviewDto;
import com.techmarket.api.dto.review.ReviewDto;
import com.techmarket.api.exception.UnauthorizationException;
import com.techmarket.api.form.review.CreateReviewForm;
import com.techmarket.api.mapper.ReviewMapper;
import com.techmarket.api.model.*;
import com.techmarket.api.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/review")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ReviewController extends ABasicController{

    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private ReviewMapper reviewMapper;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductVariantRepository productVariantRepository;

    @GetMapping(value = "/get-by-product/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListDto<List<ReviewDto>>> getByProduct(@PathVariable("id") Long id,Pageable pageable) {
        ApiMessageDto<ResponseListDto<List<ReviewDto>>> apiMessageDto = new ApiMessageDto<>();
        ResponseListDto<List<ReviewDto>> responseListDto =new ResponseListDto<>();

        Product product = productRepository.findById(id).orElse(null);
        if (product==null)
        {
            apiMessageDto.setMessage("Product Not found");
            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.PRODUCT_ERROR_NOT_FOUND);
            return apiMessageDto;
        }
        Page<Review> reviewList;
        if (isSuperAdmin())
        {
             reviewList = reviewRepository.findAllByProductId(id ,pageable);
        }else {
            reviewList= reviewRepository.findAllByProductIdAndStatus(id,UserBaseConstant.STATUS_ACTIVE,pageable);
        }

        responseListDto.setContent(reviewMapper.fromEntityListToDtoList(reviewList.getContent()));
        responseListDto.setTotalPages(reviewList.getTotalPages());
        responseListDto.setTotalElements(reviewList.getTotalElements());

        apiMessageDto.setData(responseListDto);
        apiMessageDto.setMessage("get review success");
        return apiMessageDto;
    }
    @GetMapping(value = "/get-my-review", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListDto<List<MyReviewDto>>> getMyReview(Pageable pageable) {
        ApiMessageDto<ResponseListDto<List<MyReviewDto>>> apiMessageDto = new ApiMessageDto<>();
        ResponseListDto<List<MyReviewDto>> responseListDto =new ResponseListDto<>();

       Long accountId = getCurrentUser();
        Account account = accountRepository.findById(accountId).orElse(null);
        if (account==null)
        {
            apiMessageDto.setMessage("Account Not found");
            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.ACCOUNT_ERROR_NOT_FOUND);
            return apiMessageDto;
        }
        User user = userRepository.findByAccountId(accountId).orElse(null);
        if (user==null)
        {
            apiMessageDto.setMessage("User Not found");
            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.USER_ERROR_NOT_FOUND);
            return apiMessageDto;
        }
        Page<Review> reviewList = reviewRepository.findAllByUserId(user.getId(),pageable);
        responseListDto.setContent(reviewMapper.fromEntityToGetMyReviewDtoList(reviewList.getContent()));
        responseListDto.setTotalPages(reviewList.getTotalPages());
        responseListDto.setTotalElements(reviewList.getTotalElements());

        apiMessageDto.setData(responseListDto);
        apiMessageDto.setMessage("get review success");
        return apiMessageDto;
    }
    @DeleteMapping(value = "/delete/{id}")
//    @PreAuthorize("hasRole('PR_D')")
    public ApiMessageDto<String> deleteReview(@PathVariable("id") Long id)
    {
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        Review review = reviewRepository.findById(id).orElse(null);
        if (review==null)
        {
            apiMessageDto.setMessage("Review Not found");
            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.REVIEW_ERROR_NOT_FOUND);
            return apiMessageDto;
        }

        reviewRepository.delete(review);
        apiMessageDto.setMessage("Delete review success");
        return apiMessageDto;
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> create(@Valid @RequestBody CreateReviewForm createReviewForm, BindingResult bindingResult) {

        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        if (!isUser())
        {
            throw new UnauthorizationException("Please log in and purchase to be able to rate the product");
        }
        Long accountId = getCurrentUser();
        User user = userRepository.findByAccountId(accountId).orElse(null);
        if (user==null)
        {
            apiMessageDto.setResult(false);
            apiMessageDto.setMessage("Not found user");
            apiMessageDto.setCode(ErrorCode.USER_ERROR_NOT_FOUND);
            return apiMessageDto;
        }
        Product product = productRepository.findById(createReviewForm.getProductId()).orElse(null);
        if (product==null)
        {
            apiMessageDto.setResult(false);
            apiMessageDto.setMessage("Not found product");
            apiMessageDto.setCode(ErrorCode.PRODUCT_ERROR_NOT_FOUND);
            return apiMessageDto;
        }

        apiMessageDto.setMessage("review success");
        return apiMessageDto;
    }


}
