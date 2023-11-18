package com.techmarket.api.controller;

import com.techmarket.api.constant.UserBaseConstant;
import com.techmarket.api.dto.ApiMessageDto;
import com.techmarket.api.dto.revenue.RevenueDto;
import com.techmarket.api.dto.revenue.RevenueOfYearDto;
import com.techmarket.api.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;


@RestController
@RequestMapping("/v1/revenue")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class RevenueStatisticsController {

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping(value = "/get-revenue", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('REV_V')")
    public ApiMessageDto<RevenueDto> getRevenue(@RequestParam(required = false) Date startDate ,@RequestParam(required = false) Date endDate ) {

        ApiMessageDto<RevenueDto> apiMessageDto= new ApiMessageDto<>();
        RevenueDto revenueDto ;
        if (startDate!=null && endDate !=null)
        {
            revenueDto = orderRepository.countAndSumRevenueByDate(UserBaseConstant.ORDER_STATE_COMPLETED,startDate,endDate);
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
        Double revenue =0.0;
        Integer amountOrder=0;

        List<RevenueOfYearDto> list= orderRepository.calculateYearRevenue(UserBaseConstant.ORDER_STATE_COMPLETED,year);

        apiMessageDto.setData(list);
        apiMessageDto.setMessage("get revenue success");
        return apiMessageDto;
    }






}
