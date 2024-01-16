package com.techmarket.api.notification;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techmarket.api.model.Notification;
import com.techmarket.api.notification.form.BaseSendMsgForm;
import com.techmarket.api.notification.form.PostNotificationData;
import com.techmarket.api.repository.NotificationRepository;
import com.techmarket.api.service.RabbitMQService;
import com.techmarket.api.service.RabbitSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    @Autowired
    RabbitMQService rabbitMQService;

    @Value("${rabbitmq.app}")
    private String appName;

    @Value("${rabbitmq.notification.queue}")
    private String queueName;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    RabbitSender rabbitSender;
    @Autowired
    private NotificationRepository notificationRepository;

    public void sendMessage(String message, Integer kind, Long userId) {
        PostNotificationData data = new PostNotificationData();
        data.setMessage(message);
        data.setCmd(NotificationConstant.BACKEND_POST_NOTIFICATION_CMD);
        data.setKind(kind);
        data.setMessage(message);
        data.setUserId(userId);
        data.setApp(appName);
        handleSendMsg(data, NotificationConstant.BACKEND_POST_NOTIFICATION_CMD);
    }

    private <T> void handleSendMsg(T data, String cmd) {
        BaseSendMsgForm<T> form = new BaseSendMsgForm<>();
        form.setApp(NotificationConstant.BACKEND_APP);
        form.setCmd(cmd);
        form.setData(data);

        String msg;
        try {
            msg = objectMapper.writeValueAsString(form);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        // create queue if existed
        rabbitMQService.createQueueIfNotExist(queueName);
        
        // push msg
        rabbitSender.send(msg, queueName);
    }
    public Notification createNotification(Integer state , Integer kind){

        Notification notification = new Notification();
        notification.setState(state);
        notification.setKind(kind);
        notificationRepository.save(notification);
        return notification;
    }
    public Notification createNotificationWithRefId(Integer state , Integer kind, String refId){

        Notification notification = new Notification();
        notification.setKind(kind);
        notification.setRefId(refId);
        notification.setState(state);
        notificationRepository.save(notification);
        return notification;
    }

}
