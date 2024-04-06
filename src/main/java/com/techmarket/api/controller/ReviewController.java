package com.techmarket.api.controller;

import com.techmarket.api.constant.UserBaseConstant;
import com.techmarket.api.dto.ApiMessageDto;
import com.techmarket.api.dto.ErrorCode;
import com.techmarket.api.dto.ResponseListDto;
import com.techmarket.api.dto.product.RateProductDto;
import com.techmarket.api.dto.review.*;
import com.techmarket.api.exception.UnauthorizationException;
import com.techmarket.api.form.review.CreateReviewForm;
import com.techmarket.api.form.review.FeedbackForm;
import com.techmarket.api.form.review.UpdateReviewForm;
import com.techmarket.api.mapper.ProductMapper;
import com.techmarket.api.mapper.ReviewMapper;
import com.techmarket.api.model.*;
import com.techmarket.api.model.criteria.ReviewCriteria;
import com.techmarket.api.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
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
    @Autowired
    private ProductVariantRepository productVariantRepository;

    @GetMapping(value = "/get-by-product", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListDto<List<ReviewDto>>> getByProduct(@Valid ReviewCriteria reviewCriteria,Pageable pageable) {
        ApiMessageDto<ResponseListDto<List<ReviewDto>>> apiMessageDto = new ApiMessageDto<>();
        ResponseListDto<List<ReviewDto>> responseListDto =new ResponseListDto<>();

        Page<Review> reviewList= reviewRepository.findAll(reviewCriteria.getCriteria(),pageable);
        responseListDto.setContent(reviewMapper.fromEntityListToDtoList(reviewList.getContent()));
        responseListDto.setTotalPages(reviewList.getTotalPages());
        responseListDto.setTotalElements(reviewList.getTotalElements());

        apiMessageDto.setData(responseListDto);
        apiMessageDto.setMessage("get review success");
        return apiMessageDto;
    }
    @GetMapping(value = "/get-by-product-public", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListDto<List<ReviewDto>>> getByProductPublic(@Valid ReviewCriteria reviewCriteria,Pageable pageable) {
        ApiMessageDto<ResponseListDto<List<ReviewDto>>> apiMessageDto = new ApiMessageDto<>();
        ResponseListDto<List<ReviewDto>> responseListDto =new ResponseListDto<>();
        reviewCriteria.setStatus(UserBaseConstant.STATUS_ACTIVE);
        Page<Review> reviewList= reviewRepository.findAll(reviewCriteria.getCriteria(),pageable);

        responseListDto.setContent(reviewMapper.fromEntityListToDtoList(reviewList.getContent()));
        responseListDto.setTotalPages(reviewList.getTotalPages());
        responseListDto.setTotalElements(reviewList.getTotalElements());

        apiMessageDto.setData(responseListDto);
        apiMessageDto.setMessage("get review success");
        return apiMessageDto;
    }
    @GetMapping(value = "/get-my-review", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<List<MyReviewDto>> getMyReview(@Valid ReviewCriteria reviewCriteria,@RequestParam(required = false) Long orderId) {
        ApiMessageDto<List<MyReviewDto>> apiMessageDto = new ApiMessageDto<>();
        List<MyReviewDto> list = new ArrayList<>();

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
       reviewCriteria.setUserId(user.getId());
        if(orderId!=null)
        {
            List<Review> reviewList = reviewRepository.getReviewByUserAndOrderId(user.getId(),orderId);
            list= reviewMapper.fromEntityToGetMyReviewDtoList(reviewList);
        }else
        {
            List<Review> reviewList = reviewRepository.findAll(reviewCriteria.getCriteria());
            list= reviewMapper.fromEntityToGetMyReviewDtoList(reviewList);
        }
        for (MyReviewDto item : list)
        {
            OrderDetail orderDetail = orderDetailRepository.findById(item.getOrderDetail()).orElse(null);
            if (orderDetail==null)
            {
                apiMessageDto.setMessage("orderDetail Not found");
                apiMessageDto.setResult(false);
                apiMessageDto.setCode(ErrorCode.ORDER_ERROR_NOT_FOUND);
                return apiMessageDto;
            }
            item.setPrice(orderDetail.getPrice());
            item.setColor(orderDetail.getColor());
        }
        apiMessageDto.setData(list);
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
        OrderDetail orderDetail = orderDetailRepository.findById(createReviewForm.getOrderDetailId()).orElse(null);
        if (orderDetail==null)
        {
            apiMessageDto.setResult(false);
            apiMessageDto.setMessage("Not found orderDetail");
            apiMessageDto.setCode(ErrorCode.ORDER_ERROR_NOT_FOUND);
            return apiMessageDto;
        }
        Product product = productRepository.findById(orderDetail.getProduct_Id()).orElse(null);
        if (product==null)
        {
            apiMessageDto.setResult(false);
            apiMessageDto.setMessage("Not found product");
            apiMessageDto.setCode(ErrorCode.PRODUCT_ERROR_NOT_FOUND);
            return apiMessageDto;
        }
        if (!orderDetailRepository.checkReviewProduct(UserBaseConstant.ORDER_STATE_COMPLETED,user.getId(),createReviewForm.getOrderDetailId()))
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
        orderDetail.setIsReviewed(true);
        orderDetailRepository.save(orderDetail);
        product.setTotalReview(product.getTotalReview()+1);
        product.setAvgStart(reviewRepository.avgStartOfProduct(product.getId()));
        productRepository.save(product);
        apiMessageDto.setMessage("review success");
        return apiMessageDto;
    }

    @GetMapping(value = "/get-unrated-product", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListDto<List<UnRatedDto>>> getUnratedProduct(@RequestParam(required = false) Long orderId) {
        ApiMessageDto<ResponseListDto<List<UnRatedDto>>> apiMessageDto= new ApiMessageDto<>();
        ResponseListDto<List<UnRatedDto>> responseListDto = new ResponseListDto<>();
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
        List<Object[]> id = orderDetailRepository.findProductIdUnrated(UserBaseConstant.ORDER_STATE_COMPLETED,user.getId(),orderId);
        List<Product> productList = new ArrayList<>();
        List<UnRatedDto> unRatedDtoList = new ArrayList<>();
        for(Object[] row : id)
        {
            UnRatedDto unRatedDto = new UnRatedDto();
            Long productId = (long) row[0];
            Long OrderId = (Long) row[1];
            Product product = productRepository.findById(productId).orElse(null);
            if (product==null)
            {
                apiMessageDto.setResult(false);
                apiMessageDto.setMessage("Product not found");
                apiMessageDto.setCode(ErrorCode.PRODUCT_ERROR_NOT_FOUND);
                return apiMessageDto;
            }

            unRatedDto.setRateProductDto(productMapper.toProductRateDto(product));
            unRatedDto.setOrderId(OrderId);
            unRatedDtoList.add(unRatedDto);
        }
        responseListDto.setContent(unRatedDtoList);
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
    public ApiMessageDto<AmountReviewDto> AvgStart(@PathVariable Long productId) {

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

    @GetMapping(value = "/star/count-for-each/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListDto<List<CountForEachStart>>> countForEachStart(@PathVariable Long productId) {
        ApiMessageDto<ResponseListDto<List<CountForEachStart>>> apiMessageDto = new ApiMessageDto<>();
        ResponseListDto<List<CountForEachStart>> responseListDto = new ResponseListDto<>();

        List<CountForEachStart> amountReviewDtos = reviewRepository.groupByStar(productId);
        for (Integer star: Arrays.asList(UserBaseConstant.REVIEW_STARS)){
            boolean found = false;
            for (CountForEachStart amountReviewDto : amountReviewDtos) {
                if (amountReviewDto.getStar().equals(star)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                CountForEachStart newAmountReviewDto = new CountForEachStart(star, 0L);
                amountReviewDtos.add(newAmountReviewDto);
            }
        }

        responseListDto.setContent(amountReviewDtos);
        apiMessageDto.setData(responseListDto);
        apiMessageDto.setMessage("Get list amount review success ");
        return apiMessageDto;
    }

    @PostMapping(value = "/feed-back", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> feedBack(@Valid @RequestBody FeedbackForm feedbackForm, BindingResult bindingResult) {
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        Review review = reviewRepository.findById(feedbackForm.getReviewId()).orElse(null);
        if (review==null)
        {
            apiMessageDto.setMessage("not found review");
            apiMessageDto.setResult(false);
            return apiMessageDto;
        }
        Review feedback = new Review();
        feedback.setMessage(feedbackForm.getMessage());
        feedback.setParentId(review);
        reviewRepository.save(feedback);
        apiMessageDto.setMessage("feedback success");
        return apiMessageDto;

    }


}
