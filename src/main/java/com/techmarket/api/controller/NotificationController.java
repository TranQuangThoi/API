package com.techmarket.api.controller;

import com.techmarket.api.constant.UserBaseConstant;
import com.techmarket.api.dto.ApiMessageDto;
import com.techmarket.api.dto.ErrorCode;
import com.techmarket.api.dto.ResponseListDto;
import com.techmarket.api.mapper.NotificationMapper;
import com.techmarket.api.model.Notification;
import com.techmarket.api.model.User;
import com.techmarket.api.model.criteria.NotificationCriteria;
import com.techmarket.api.notification.dto.MynotificationDto;
import com.techmarket.api.notification.dto.NotificationDto;
import com.techmarket.api.notification.form.ChangeStateNotification;
import com.techmarket.api.repository.NotificationRepository;
import com.techmarket.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/notification")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class NotificationController extends ABasicController{

    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private NotificationMapper notificationMapper;
    @Autowired
    private UserRepository userRepository;

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('NT_D')")
    public ApiMessageDto<String> delete(@PathVariable("id") Long id) {
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        Notification notification = notificationRepository.findById(id).orElse(null);
        if (notification == null) {
            apiMessageDto.setResult(false);
            apiMessageDto.setMessage("Notification not found");
            apiMessageDto.setCode(ErrorCode.NOTIFICATION_ERROR_NOT_FOUND);
            return apiMessageDto;
        }
        notificationRepository.delete(notification);
        apiMessageDto.setMessage("delete notification success");
        return apiMessageDto;
    }

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('NT_L')")
    public ApiMessageDto<ResponseListDto<List<NotificationDto>>> list(NotificationCriteria notificationCriteria, Pageable pageable) {

        ApiMessageDto<ResponseListDto<List<NotificationDto>>> apiMessageDto = new ApiMessageDto<>();
        ResponseListDto<List<NotificationDto>> responseListDto = new ResponseListDto<>();
        Page<Notification> listNotification = notificationRepository.findAll(notificationCriteria.getCriteria(), pageable);
        responseListDto.setContent(notificationMapper.fromEntityToNotiListDto(listNotification.getContent()));
        responseListDto.setTotalPages(listNotification.getTotalPages());
        responseListDto.setTotalElements(listNotification.getTotalElements());

        apiMessageDto.setData(responseListDto);
        apiMessageDto.setMessage("get list sucess");
        return apiMessageDto;
    }
    @PutMapping(value = "/read-all", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('NT_RA')")
    public ApiMessageDto<String> readAll() {

        Long accountId = getCurrentUser();
        User user = userRepository.findByAccountId(accountId).orElse(null);
        List<Notification> notifications = notificationRepository.findAllByIdUserAndState(user.getId(), UserBaseConstant.NOTIFICATION_STATE_SENT);
        notifications.forEach(noti -> noti.setState(UserBaseConstant.NOTIFICATION_STATE_READ));
        notificationRepository.saveAll(notifications);
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setMessage("Has moved to the fully read state");
        return apiMessageDto;
    }
    @DeleteMapping(value = "/delete-all", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('NT_DA')")
    public ApiMessageDto<String> deleteAll() {
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        Long accountId = getCurrentUser();
        User user = userRepository.findByAccountId(accountId).orElse(null);
        notificationRepository.deleteAllByIdUser(user.getId());
        apiMessageDto.setMessage("Delete all success");
        return apiMessageDto;
    }

    @GetMapping(value = "/my-notification", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<MynotificationDto> getMyNotification(@PageableDefault(sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable , @RequestParam(value = "state", required = false) Integer state) {

        ApiMessageDto<MynotificationDto> apiMessageDto = new ApiMessageDto<>();
        Long accountId = getCurrentUser();
        User user = userRepository.findByAccountId(accountId).orElse(null);
        Long userId = user.getId();
        MynotificationDto mynotificationDto = new MynotificationDto();
        Page<Notification> notificationPage ;
        if (state!=null){
            notificationPage = notificationRepository.findAllByIdUserAndState(userId,state,pageable);
        }else {
            notificationPage = notificationRepository.findAllByIdUser(userId,pageable);

        }
        mynotificationDto.setContent(notificationMapper.fromEntityToNotiListDto(notificationPage.getContent()));
        mynotificationDto.setTotalPages(notificationPage.getTotalPages());
        mynotificationDto.setTotalElements(notificationPage.getTotalElements());
        mynotificationDto.setTotalUnread(notificationRepository.countByIdUserAndState(userId, UserBaseConstant.NOTIFICATION_STATE_SENT));
        apiMessageDto.setData(mynotificationDto);
        apiMessageDto.setMessage("get my notification success");
        return apiMessageDto;
    }
    @PutMapping(value = "/change-state", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('NT_CS')")
    public ApiMessageDto<String> changeState(@Valid @RequestBody ChangeStateNotification changeStateNotification, BindingResult bindingResult) {
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        Notification notification = notificationRepository.findById(changeStateNotification.getId()).orElse(null);
        if (notification==null)
        {
            apiMessageDto.setResult(false);
            apiMessageDto.setMessage("notification not found");
            apiMessageDto.setCode(ErrorCode.NOTIFICATION_ERROR_NOT_FOUND);
            return apiMessageDto;
        }
        if (notification.getState().equals(UserBaseConstant.NOTIFICATION_STATE_SENT))
        {
            notification.setState(UserBaseConstant.NOTIFICATION_STATE_READ);
        }

        notificationRepository.save(notification);
        apiMessageDto.setMessage("change to state read success");
        return apiMessageDto;
    }
    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('NT_V')")
    public ApiMessageDto<NotificationDto> getNotification(@PathVariable("id") Long id) {
        ApiMessageDto<NotificationDto> apiMessageDto = new ApiMessageDto<>();
        Notification notification = notificationRepository.findById(id).orElse(null);
        if (notification == null) {
            apiMessageDto.setResult(false);
            apiMessageDto.setMessage("Notification not found");
            apiMessageDto.setCode(ErrorCode.NOTIFICATION_ERROR_NOT_FOUND);
            return apiMessageDto;
        }
        apiMessageDto.setData(notificationMapper.fromEntityToNotificatonDto(notification));
        apiMessageDto.setMessage("get notification success");
        return apiMessageDto;
    }
}
