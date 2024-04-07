package com.techmarket.api.controller;

import com.techmarket.api.constant.UserBaseConstant;
import com.techmarket.api.dto.ApiMessageDto;
import com.techmarket.api.dto.ErrorCode;
import com.techmarket.api.dto.ResponseListDto;
import com.techmarket.api.dto.refunds.RefundsDto;
import com.techmarket.api.form.refunds.ChangeStateRefunds;
import com.techmarket.api.form.refunds.CreateRefundsForm;
import com.techmarket.api.form.review.CreateReviewForm;
import com.techmarket.api.mapper.RefundsMapper;
import com.techmarket.api.model.Order;
import com.techmarket.api.model.Refunds;
import com.techmarket.api.model.criteria.RefundsCriteria;
import com.techmarket.api.repository.OrderRepository;
import com.techmarket.api.repository.RefundsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/refunds")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class RefundsController extends ABasicController{

    @Autowired
    private RefundsRepository refundsRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private RefundsMapper refundsMapper;

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('RF_L')")
    public ApiMessageDto<ResponseListDto<List<RefundsDto>>> getList(@Valid RefundsCriteria refundsCriteria, Pageable pageable){

        ApiMessageDto<ResponseListDto<List<RefundsDto>>> apiMessageDto = new ApiMessageDto<>();
        ResponseListDto<List<RefundsDto>> responseListDto = new ResponseListDto<>();
        List<Refunds> list = refundsRepository.findAll(refundsCriteria.getCriteria());
        List<RefundsDto> refundsDtoList = refundsMapper.fromEntityToListRefundsDto(list);

        responseListDto.setContent(refundsDtoList);
        apiMessageDto.setData(responseListDto);
        apiMessageDto.setMessage("get list success");
        return apiMessageDto;
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('RF_V')")
    public ApiMessageDto<RefundsDto> getRefunds(@PathVariable("id") long id){

        ApiMessageDto<RefundsDto> apiMessageDto = new ApiMessageDto<>();
        Refunds refunds = refundsRepository.findById(id).orElse(null);
        if(refunds==null)
        {
            apiMessageDto.setResult(false);
            apiMessageDto.setMessage("Not found ");
            apiMessageDto.setCode(ErrorCode.REFUNDS_ERROR_NOT_FOUND);
            return apiMessageDto;
        }
        RefundsDto refundsDto = refundsMapper.fromEntityToRefundsDto(refunds);
        apiMessageDto.setData(refundsDto);
        apiMessageDto.setMessage("get success");
        return apiMessageDto;
    }

    @PutMapping(value = "/change-state",produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('RF_CT')")
    public ApiMessageDto<String> changeState(@Valid @RequestBody ChangeStateRefunds changeStateRefunds, BindingResult bindingResult)
    {
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        Refunds refunds = refundsRepository.findById(changeStateRefunds.getId()).orElse(null);
        if (refunds ==null)
        {
            apiMessageDto.setResult(false);
            apiMessageDto.setMessage("Not found ");
            apiMessageDto.setCode(ErrorCode.REFUNDS_ERROR_NOT_FOUND);
            return apiMessageDto;
        }
        if(refunds.getState().equals(UserBaseConstant.REFUNDS_KIND_PROCCESSED))
        {
            apiMessageDto.setResult(false);
            apiMessageDto.setMessage("completed cannot be changed ");
            apiMessageDto.setCode(ErrorCode.REFUNDS_ERROR_COMPLETED);
            return apiMessageDto;
        }
        refunds.setState(changeStateRefunds.getState());
        refundsRepository.save(refunds);
        apiMessageDto.setMessage("change state success");
        return apiMessageDto;
    }
    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> create(@Valid @RequestBody CreateRefundsForm createRefundsForm, BindingResult bindingResult) {

        ApiMessageDto apiMessageDto = new ApiMessageDto();

        Order order = orderRepository.findById(createRefundsForm.getOrderId()).orElse(null);
        if(order==null)
        {
            apiMessageDto.setResult(false);
            apiMessageDto.setMessage("order not found ");
            apiMessageDto.setCode(ErrorCode.ORDER_ERROR_NOT_FOUND);
            return apiMessageDto;
        }
        Refunds refundsExited = refundsRepository.findByOrderId(createRefundsForm.getOrderId());
        if (refundsExited !=null)
        {
            apiMessageDto.setResult(false);
            apiMessageDto.setMessage("already exist ");
            apiMessageDto.setCode(ErrorCode.REFUNDS_ERROR_EXITED);
            return apiMessageDto;
        }
        Refunds refunds = refundsMapper.fromCreateFormToEntity(createRefundsForm);
        refunds.setOrder(order);
        refunds.setState(UserBaseConstant.REFUNDS_KIND_NOT_YET_PROCCESS);
        refundsRepository.save(refunds);
        apiMessageDto.setMessage("create success");
        return apiMessageDto;

    }



}
