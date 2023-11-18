package com.techmarket.api.controller;

import com.techmarket.api.constant.UserBaseConstant;
import com.techmarket.api.dto.ApiMessageDto;
import com.techmarket.api.dto.ErrorCode;
import com.techmarket.api.dto.ResponseListDto;
import com.techmarket.api.dto.product.RateProductDto;
import com.techmarket.api.dto.review.AmountReviewDto;
import com.techmarket.api.dto.review.MyReviewDto;
import com.techmarket.api.dto.review.ReviewDto;
import com.techmarket.api.exception.UnauthorizationException;
import com.techmarket.api.form.review.CreateReviewForm;
import com.techmarket.api.form.review.UpdateReviewForm;
import com.techmarket.api.mapper.ProductMapper;
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
import java.util.ArrayList;
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
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private ProductMapper productMapper;

    @GetMapping(value = "/get-by-product/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('RV_V')")
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
        Page<Review> reviewList = reviewRepository.findAllByProductId(id ,pageable);
        responseListDto.setContent(reviewMapper.fromEntityListToDtoList(reviewList.getContent()));
        responseListDto.setTotalPages(reviewList.getTotalPages());
        responseListDto.setTotalElements(reviewList.getTotalElements());

        apiMessageDto.setData(responseListDto);
        apiMessageDto.setMessage("get review success");
        return apiMessageDto;
    }
    @GetMapping(value = "/get-by-product-public/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListDto<List<ReviewDto>>> getByProductPublic(@PathVariable("id") Long id,Pageable pageable) {
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
        Page<Review> reviewList= reviewRepository.findAllByProductIdAndStatus(id,UserBaseConstant.STATUS_ACTIVE,pageable);

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

        String tokenExist = getCurrentToken();
        if (tokenExist==null)
        {
            throw new UnauthorizationException("Please log in and purchase to be able to rate the product");
        }
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
    @PreAuthorize("hasRole('RV_D')")
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
        String tokenExist = getCurrentToken();
        if (tokenExist==null)
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

        List<OrderDetail> orderDetailList = orderDetailRepository.findUnpaidOrderDetailsByUserIdAndProductId(UserBaseConstant.ORDER_STATE_COMPLETED,user.getId(),createReviewForm.getProductId());
        if (orderDetailList.size()==0)
        {
            apiMessageDto.setResult(false);
            apiMessageDto.setMessage("You can not rate this product");
            apiMessageDto.setCode(ErrorCode.REVIEW_ERROR_CAN_NOT_RATE);
            return apiMessageDto;
        }
        Review review = reviewMapper.fromCreateFormToEntity(createReviewForm);
        review.setProduct(product);
        review.setUser(user);
        reviewRepository.save(review);

        for (OrderDetail item : orderDetailList)
        {
            item.setIsReviewed(true);
            orderDetailRepository.save(item);
        }
        product.setTotalReview(product.getTotalReview()+1);
        productRepository.save(product);
        apiMessageDto.setMessage("review success");
        return apiMessageDto;
    }

    @GetMapping(value = "/get-unrated-product", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListDto<List<RateProductDto>>> getUnratedProduct() {
        ApiMessageDto<ResponseListDto<List<RateProductDto>>> apiMessageDto= new ApiMessageDto<>();
        ResponseListDto<List<RateProductDto>> responseListDto = new ResponseListDto<>();
        String tokenExist = getCurrentToken();
        if (tokenExist==null)
        {
            throw new UnauthorizationException("Please log in and purchase to be able to rate the product");
        }
        Long accountId = getCurrentUser();
        User user = userRepository.findByAccountId(accountId).orElse(null);
        if (user==null)
        {
            apiMessageDto.setResult(false);
            apiMessageDto.setMessage("user not found");
            apiMessageDto.setCode(ErrorCode.USER_ERROR_NOT_FOUND);
            return apiMessageDto;
        }
        List<Long> productId = orderDetailRepository.findProductIdUnrated(UserBaseConstant.ORDER_STATE_COMPLETED,user.getId());
        List<Product> productList = new ArrayList<>();
        if (productId.size()!=0)
        {
            for (Long item : productId)
            {
                Product product = productRepository.findById(item).orElse(null);
                if (product==null)
                {
                    apiMessageDto.setResult(false);
                    apiMessageDto.setMessage("Product not found");
                    apiMessageDto.setCode(ErrorCode.PRODUCT_ERROR_NOT_FOUND);
                    return apiMessageDto;
                }
                productList.add(product);

            }
        }
        responseListDto.setContent(productMapper.toProductRateListDto(productList));
        apiMessageDto.setData(responseListDto);
        return apiMessageDto;
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('RV_U')")
    public ApiMessageDto<String> updateReview(@Valid @RequestBody UpdateReviewForm updateReviewForm , BindingResult bindingResult) {
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();

       Review review = reviewRepository.findById(updateReviewForm.getId()).orElse(null);
        if (review ==null)
        {
            apiMessageDto.setResult(false);
            apiMessageDto.setMessage("Review not found");
            apiMessageDto.setCode(ErrorCode.REVIEW_ERROR_NOT_FOUND);
            return apiMessageDto;
        }

        reviewMapper.fromUpdateFormToEntityReview(updateReviewForm,review);
        reviewRepository.save(review);
        apiMessageDto.setMessage("update status success");
        return apiMessageDto;
    }

    @GetMapping(value = "/star/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<AmountReviewDto> listReviews(@PathVariable Long productId) {

        ApiMessageDto<AmountReviewDto> apiMessageDto = new ApiMessageDto<>();

        Product product = productRepository.findById(productId).orElse(null);
        if (product==null)
        {
            apiMessageDto.setResult(false);
            apiMessageDto.setMessage("Product Not found");
            apiMessageDto.setCode(ErrorCode.PRODUCT_ERROR_NOT_FOUND);
            return apiMessageDto;
        }
        Double avgStar = reviewRepository.avgStartOfProduct(productId);
        Long countReview = reviewRepository.countReviewOfProduct(productId);
        AmountReviewDto amountReviewDto = new AmountReviewDto();
        amountReviewDto.setAmount(countReview);
        amountReviewDto.setStar(avgStar);

        apiMessageDto.setData(amountReviewDto);
        return apiMessageDto;
    }


}
