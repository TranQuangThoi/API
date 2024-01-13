package com.techmarket.api.schedule;

import com.techmarket.api.repository.NotificationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NotificationScheduler {
    @Autowired
    private NotificationRepository notificationRepository;
    private boolean isTaskExecutedToday = false;


    @Scheduled(cron = "0 0 0 * * *", zone = "UTC")
    public void scheduledTask() {
        if (!isTaskExecutedToday) {
            deleteNotificationsCreatedAfter15Days();
            isTaskExecutedToday = true;
        }
    }

    private void deleteNotificationsCreatedAfter15Days() {
        notificationRepository.deleteNotificationsCreatedAfterDays(15);
    }
}