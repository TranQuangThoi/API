package com.techmarket.api.mapper;

import com.techmarket.api.model.Notification;
import com.techmarket.api.notification.dto.NotificationDto;
import org.mapstruct.*;

import java.util.List;


@Mapper(componentModel = "spring")
public interface NotificationMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "modifiedDate", target = "modifiedDate")
    @Mapping(source = "idUser", target = "idUser")
    @Mapping(source = "refId", target = "refId")
    @Mapping(source = "kind", target = "kind")
    @Mapping(source = "msg", target = "msg")
    @Mapping(source = "state", target = "state")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromEntityToNotiDto")
    NotificationDto fromEntityToNotificatonDto(Notification notification);

    @IterableMapping(elementTargetType = NotificationDto.class, qualifiedByName = "fromEntityToNotiDto")
    List<NotificationDto> fromEntityToNotiListDto(List<Notification> notifications);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "idUser", target = "idUser")
    @Mapping(source = "kind", target = "kind")
    @Mapping(source = "refId", target = "refId")
    @Mapping(source = "msg", target = "msg")
    @Mapping(source = "modifiedDate", target = "modifiedDate")
    @Mapping(source = "state", target = "state")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromEntityToNotiDtoAuto")
    NotificationDto fromEntityToNotificatonDtoAuto(Notification notification);
    @IterableMapping(elementTargetType = NotificationDto.class, qualifiedByName = "fromEntityToNotiDtoAuto")
    List<NotificationDto> fromEntityToNotiListDtoAuto(List<Notification> notifications);



}
