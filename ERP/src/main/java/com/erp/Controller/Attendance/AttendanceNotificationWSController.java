package com.erp.Controller.Attendance;


import com.erp.Model.NotificationMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class AttendanceNotificationWSController {

    @MessageMapping("/attendance/notify")
    @SendTo("/topic/global")
    public NotificationMessage broadcastAttendanceNotification(NotificationMessage message) {
        System.out.println("📩 WebSocket Message received from client: " + message);
        return message;
    }
}
