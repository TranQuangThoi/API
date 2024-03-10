package com.techmarket.api.controller;

import com.techmarket.api.constant.UserBaseConstant;
import com.techmarket.api.dto.ApiMessageDto;
import com.techmarket.api.dto.ResponseListDto;
import com.techmarket.api.dto.revenue.RevenueDto;
import com.techmarket.api.dto.revenue.RevenueOfYearDto;
import com.techmarket.api.dto.revenue.RevenueProductDto;
import com.techmarket.api.model.Product;
import com.techmarket.api.repository.OrderDetailRepository;
import com.techmarket.api.repository.OrderRepository;
import com.techmarket.api.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@RestController
@RequestMapping("/v1/revenue")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class RevenueStatisticsController {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private ProductRepository productRepository;

    @GetMapping(value = "/get-revenue", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('REV_V')")
    public ApiMessageDto<RevenueDto> getRevenue(@RequestParam(required = false) Date startDate ,@RequestParam(required = false) Date endDate ) {

        ApiMessageDto<RevenueDto> apiMessageDto= new ApiMessageDto<>();
        RevenueDto revenueDto ;
        if (startDate!=null && endDate !=null)
        {
            revenueDto = orderRepository.countAndSumRevenueByDate(UserBaseConstant.ORDER_STATE_COMPLETED,startDate,endDate);
//            Long amountUser = orderRepository.countUserBoughtItem(UserBaseConstant.ORDER_STATE_COMPLETED,startDate,endDate);
//            revenueDto.setAmountUser(amountUser);
            apiMessageDto.setData(revenueDto);
            apiMessageDto.setMessage("get revenue success");
            return apiMessageDto;
        }
        revenueDto = orderRepository.countAndSumRevenueTotal(UserBaseConstant.ORDER_STATE_COMPLETED);
        apiMessageDto.setData(revenueDto);
        apiMessageDto.setMessage("get revenue success");
        return apiMessageDto;
    }


    @GetMapping(value = "/get-revenue-month", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('REV_GRM')")
    public ApiMessageDto<List<RevenueOfYearDto>> getRevenueMonth(@RequestParam Integer year ) {

        ApiMessageDto<List<RevenueOfYearDto>> apiMessageDto= new ApiMessageDto<>();
        List<RevenueOfYearDto> list= orderRepository.calculateYearRevenue(UserBaseConstant.ORDER_STATE_COMPLETED,year);
        apiMessageDto.setData(list);
        apiMessageDto.setMessage("get revenue success");
        return apiMessageDto;
    }

    @GetMapping(value = "/get-revenue-of-each-product", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('REV_GRP')")
    public ApiMessageDto<ResponseListDto<List<RevenueProductDto>>> getRevenueOfEachProduct() {

        ApiMessageDto<ResponseListDto<List<RevenueProductDto>>> apiMessageDto = new ApiMessageDto<>();
        ResponseListDto<List<RevenueProductDto>> responseListDto = new ResponseListDto<>();

        Pageable pageable = PageRequest.of(0,10);
        Page<Object[]> page = orderDetailRepository.calculatePriceProduct(UserBaseConstant.ORDER_STATE_COMPLETED, pageable);
        List<RevenueProductDto> productDtoList = new ArrayList<>();

        for (Object[] row : page.getContent()) {
            RevenueProductDto revenueProductDto = new RevenueProductDto();
            Long productId = (Long) row[0];
            Double totalPrice = (Double) row[1];
            Product product = productRepository.findById(productId).orElse(null);

            if (product != null) {
                revenueProductDto.setProductName(product.getName());
                revenueProductDto.setTotalRevenue(totalPrice);
                revenueProductDto.setAmount(product.getSoldAmount());
                productDtoList.add(revenueProductDto);
            }
        }

        responseListDto.setContent(productDtoList);
        responseListDto.setTotalPages(page.getTotalPages());
        responseListDto.setTotalElements(page.getTotalElements());

        apiMessageDto.setData(responseListDto);
        apiMessageDto.setMessage("get revenue success");

        return apiMessageDto;
    }







}
