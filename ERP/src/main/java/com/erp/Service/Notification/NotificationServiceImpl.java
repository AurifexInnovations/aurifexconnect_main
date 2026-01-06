package com.erp.Service.Notification;

import com.erp.Model.Task;
import com.erp.Repository.TechnicianDevice.TechnicianDeviceRepository;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final TechnicianDeviceRepository technicianDeviceRepository;

    @Override
    public void sendTaskStartedNotification(Task task, Long technicianId) {
        List<String> tokens = technicianDeviceRepository.findTokensByTechnicianId(technicianId);

        if (tokens.isEmpty()) return;

        MulticastMessage message = MulticastMessage.builder()
                .setNotification(Notification.builder()
                        .setTitle("Task Started")
                        .setBody("Task: " + task.getTaskName() + " has started")
                        .build())
                .putData("taskId", task.getTaskId().toString())
                .putData("status", "IN_PROGRESS")
                .addAllTokens(tokens)
                .build();

        try {
            FirebaseMessaging.getInstance().sendMulticast(message);
        } catch (FirebaseMessagingException e) {
            throw new IllegalStateException("Failed to send FCM notification", e);
        }
    }
}
