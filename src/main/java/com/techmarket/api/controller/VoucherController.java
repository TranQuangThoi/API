package com.techmarket.api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import com.techmarket.api.model.*;
import com.techmarket.api.model.criteria.ProductCriteria;
import com.techmarket.api.model.criteria.VourcherCriteria;
import com.techmarket.api.notification.NotificationService;
import com.techmarket.api.notification.dto.VoucherNotificationMessage;
import com.techmarket.api.repository.NotificationRepository;
import com.techmarket.api.repository.UserRepository;
import com.techmarket.api.repository.VoucherRepository;
import com.techmarket.api.schedule.VoucherSchedule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/v1/voucher")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class VoucherController extends ABasicController{

    @Autowired
    private VoucherRepository voucherRepository;
    @Autowired
    private VoucherMapper voucherMapper;
    @Autowired
    private VoucherSchedule voucherSchedule;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private NotificationRepository notificationRepository;

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
        Voucher voucher = voucherMapper.fromCreateFormToEntity(createVoucherForm);
        voucherRepository.save(voucher);
        if (createVoucherForm.getStatus().equals(UserBaseConstant.STATUS_ACTIVE))
        {
            createNotificationAndSendMessage(UserBaseConstant.NOTIFICATION_STATE_SENT,voucher,UserBaseConstant.NOTIFICATION_KIND_NOTIFY_VOUCHER);
        }
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

    @GetMapping(value = "/get-my-voucher", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<List<VoucherDto>> getMyVoucher() {
        ApiMessageDto<List<VoucherDto>> apiMessageDto = new ApiMessageDto<>();
        Long accountId = getCurrentUser();
        User user = userRepository.findByAccountId(accountId).orElse(null);
        if (user==null)
        {
            apiMessageDto.setResult(false);
            apiMessageDto.setMessage("Staff are not allowed to use this voucher ");
            apiMessageDto.setCode(ErrorCode.USER_ERROR_NOT_FOUND);
            return apiMessageDto;
        }
        List<Voucher> listMyVocher = voucherRepository.findByKinds(UserBaseConstant.STATUS_ACTIVE , Arrays.asList(UserBaseConstant.VOUCHER_KIND_ALL,user.getMemberShip()));
        List<VoucherDto> voucherDtoList = voucherMapper.fromEntityListToDtoList(listMyVocher);

        apiMessageDto.setData(voucherDtoList);
        apiMessageDto.setMessage("get list voucher success");
        return apiMessageDto;
    }
    @GetMapping(value = "/get-voucher-for-guest", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<List<VoucherDto>> getForMyGuest() {
        ApiMessageDto<List<VoucherDto>> apiMessageDto = new ApiMessageDto<>();
        List<Voucher> listMyVocher = voucherRepository.findByKinds(UserBaseConstant.STATUS_ACTIVE , Arrays.asList(UserBaseConstant.VOUCHER_KIND_ALL));
        List<VoucherDto> voucherDtoList = voucherMapper.fromEntityListToDtoList(listMyVocher);

        apiMessageDto.setData(voucherDtoList);
        apiMessageDto.setMessage("get list voucher success");
        return apiMessageDto;
    }
    public String convertObjectToJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            return null;
        }
    }
    private String getJsonMessage(Voucher voucher, Notification notification){
        VoucherNotificationMessage voucherNotificationMessage = new VoucherNotificationMessage();
        voucherNotificationMessage.setVoucherId(voucher.getId());
        voucherNotificationMessage.setNotificationId(notification.getId());
        voucherNotificationMessage.setAmount(voucher.getAmount());
        voucherNotificationMessage.setExpired(voucher.getExpired());
        voucherNotificationMessage.setUserId(notification.getIdUser());
        voucherNotificationMessage.setTitle(voucher.getTitle());
        voucherNotificationMessage.setPercent(voucher.getPercent());
        voucherNotificationMessage.setContent(voucher.getContent());
        return convertObjectToJson(voucherNotificationMessage);
    }
    private Notification createNotification(Integer notificationState, Integer notificationKind, Voucher voucher, Long userId) {
        Notification notification = notificationService.createNotification(notificationState, notificationKind);
        String jsonMessage = getJsonMessage(voucher, notification);
        notification.setIdUser(userId);
        notification.setMsg(jsonMessage);
        return notification;
    }
    private Integer mapVoucherKindToUserKind(Integer membership) {
        switch (membership) {
            case 0:
                return UserBaseConstant.USER_KIND_NEW_MEMBERSHIP;
            case 1:
                return UserBaseConstant.USER_KIND_SILVER_MEMBERSHIP;
            case 2:
                return UserBaseConstant.USER_KIND_GOLD_MEMBERSHIP;
            case 3:
                return UserBaseConstant.USER_KIND_DIAMOND_MEMEBERSHIP;
            case 4:
                return UserBaseConstant.USER_KIND_VIP_MEMEBERSHIP;
            default:
                return null;
        }
    }
    private void createNotificationAndSendMessage(Integer notificationState, Voucher voucher, Integer notificationKind) {
        Long accountId = getCurrentUser();
        List<Notification> notifications = new ArrayList<>();
        if (voucher.getKind().equals(UserBaseConstant.VOUCHER_KIND_ALL))
        {
            List<User> userList = userRepository.findAll();
            for (User user : userList)
            {
                Notification shopNotification = createNotification(notificationState,notificationKind,voucher,user.getId());
                notifications.add(shopNotification);
            }
        }else
        {
            List<User> userList = userRepository.findAllByMemberShip(mapVoucherKindToUserKind(voucher.getKind()));
            for (User user : userList)
            {
                Notification shopNotification = createNotification(notificationState,notificationKind,voucher,user.getId());
                notifications.add(shopNotification);
            }
        }
        notificationRepository.saveAll(notifications);
//        for (Notification notification: notifications){
//            notificationService.sendMessage(notification.getMsg(), notificationKind, notification.getIdUser());
//        }
    }
}
