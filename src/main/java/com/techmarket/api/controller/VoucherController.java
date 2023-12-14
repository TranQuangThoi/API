package com.techmarket.api.controller;

import com.techmarket.api.constant.UserBaseConstant;
import com.techmarket.api.dto.ApiMessageDto;
import com.techmarket.api.dto.ErrorCode;
import com.techmarket.api.dto.ResponseListDto;
import com.techmarket.api.dto.product.ProductDto;
import com.techmarket.api.dto.voucher.VoucherDto;
import com.techmarket.api.exception.UnauthorizationException;
import com.techmarket.api.form.product.CreateProductForm;
import com.techmarket.api.form.product.UpdateProductForm;
import com.techmarket.api.form.voucher.CreateVoucherForm;
import com.techmarket.api.form.voucher.UpdateVoucherForm;
import com.techmarket.api.mapper.VoucherMapper;
import com.techmarket.api.model.Product;
import com.techmarket.api.model.ProductVariant;
import com.techmarket.api.model.Voucher;
import com.techmarket.api.model.criteria.ProductCriteria;
import com.techmarket.api.model.criteria.VourcherCriteria;
import com.techmarket.api.repository.VoucherRepository;
import com.techmarket.api.schedule.VoucherSchedule;
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
@RequestMapping("/v1/voucher")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class VoucherController extends ABasicController{

    @Autowired
    private VoucherRepository voucherRepository;
    @Autowired
    private VoucherMapper voucherMapper;
    @Autowired
    private VoucherSchedule voucherSchedule;

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('VC_L')")
    public ApiMessageDto<ResponseListDto<List<VoucherDto>>> getList(@Valid VourcherCriteria vourcherCriteria, Pageable pageable) {

        ApiMessageDto<ResponseListDto<List<VoucherDto>>> apiMessageDto = new ApiMessageDto<>();
        ResponseListDto<List<VoucherDto>> responseListDto = new ResponseListDto<>();

        Page<Voucher> vouchers = voucherRepository.findAll(vourcherCriteria.getSpecification(),pageable);
        responseListDto.setContent(voucherMapper.fromEntityListToDtoList(vouchers.getContent()));
        responseListDto.setTotalPages(vouchers.getTotalPages());
        responseListDto.setTotalElements(vouchers.getTotalElements());

        apiMessageDto.setData(responseListDto);
        apiMessageDto.setMessage("get list voucher success");

        return apiMessageDto;
    }
    @GetMapping(value = "/get-autoComplete", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListDto<List<VoucherDto>>> getAutoComplete(@Valid VourcherCriteria vourcherCriteria, Pageable pageable) {

        ApiMessageDto<ResponseListDto<List<VoucherDto>>> apiMessageDto = new ApiMessageDto<>();
        ResponseListDto<List<VoucherDto>> responseListDto = new ResponseListDto<>();
        vourcherCriteria.setStatus(UserBaseConstant.STATUS_ACTIVE);
        Page<Voucher> vouchers = voucherRepository.findAll(vourcherCriteria.getSpecification(),pageable);
        responseListDto.setContent(voucherMapper.fromEntityListToDtoList(vouchers.getContent()));
        responseListDto.setTotalPages(vouchers.getTotalPages());
        responseListDto.setTotalElements(vouchers.getTotalElements());

        apiMessageDto.setData(responseListDto);
        apiMessageDto.setMessage("get list voucher success");

        return apiMessageDto;
    }
    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('VC_V')")
    public ApiMessageDto<VoucherDto> getVoucher(@PathVariable Long id) {

        ApiMessageDto<VoucherDto> apiMessageDto = new ApiMessageDto<>();
        Voucher voucher = voucherRepository.findById(id).orElse(null);
        if (voucher==null)
        {
            apiMessageDto.setResult(false);
            apiMessageDto.setMessage("Not found voucher");
            apiMessageDto.setCode(ErrorCode.VOUCHER_ERROR_NOT_FOUND);
            return apiMessageDto;
        }
        apiMessageDto.setData(voucherMapper.fromEntityToDto(voucher));
        apiMessageDto.setMessage("get list voucher success");

        return apiMessageDto;
    }
    @PutMapping(value = "/update",produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('VC_U')")
    public ApiMessageDto<String> updateVoucher(@Valid @RequestBody UpdateVoucherForm updateVoucherForm, BindingResult bindingResult){
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();

        Voucher voucher = voucherRepository.findById(updateVoucherForm.getId()).orElse(null);
        if (voucher==null)
        {
            apiMessageDto.setResult(false);
            apiMessageDto.setMessage("Not found voucher");
            apiMessageDto.setCode(ErrorCode.VOUCHER_ERROR_NOT_FOUND);
            return apiMessageDto;
        }
        voucherMapper.fromUpdateFormToEntityVoucher(updateVoucherForm,voucher);
        voucherRepository.save(voucher);

        apiMessageDto.setMessage("update voucher success");
        return apiMessageDto;
    }

    @PostMapping(value = "/create",produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('VC_C')")
    public ApiMessageDto<String> createVoucher(@Valid @RequestBody CreateVoucherForm createVoucherForm, BindingResult bindingResult)
    {
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        voucherRepository.save(voucherMapper.fromCreateFormToEntity(createVoucherForm));
        apiMessageDto.setMessage("create voucher success");
        return apiMessageDto;
    }
    @PostMapping("/check-expired")
    public ApiMessageDto<String> checkExpriredVoucher(){
        if(!isEmployee() && !isShop()){
            throw new UnauthorizationException("Not allowed check");
        }
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        voucherSchedule.checkAndUpdateVoucherStatus();
        apiMessageDto.setMessage("Set status locked successfully");
        return apiMessageDto;
    }


}
