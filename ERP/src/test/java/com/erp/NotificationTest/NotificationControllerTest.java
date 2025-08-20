package com.erp.NotificationTest;


import com.erp.Controller.Notification.NotificationController;
import com.erp.Model.NotificationMessage;
import com.erp.Service.Notification.NotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.time.Instant;
import java.util.List;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NotificationController.class)
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private NotificationService notificationService;

    private NotificationMessage testNotification;

    @BeforeEach
    void setUp() {
        testNotification = new NotificationMessage();
        testNotification.setId(1L);
        testNotification.setTitle("Test Notification");
        testNotification.setMessage("Hello World!");
        testNotification.setTimestamp(Instant.now().toEpochMilli());
        testNotification.setFrom("admin@erp.com");
        testNotification.setTo("user@erp.com");
        testNotification.setType("INFO");
    }

    @Test
    void testSendGlobalNotification() throws Exception {
        Mockito.doNothing().when(notificationService).sendNotification(any(NotificationMessage.class));

        mockMvc.perform(post("/api/notifications/send/global")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testNotification)))
                .andExpect(status().isOk())
                .andExpect(content().string("Global notification sent"));
    }

    @Test
    void testSendToUserNotification() throws Exception {
        Mockito.doNothing().when(notificationService).sendToUser(eq("user@erp.com"), any(NotificationMessage.class));

        mockMvc.perform(post("/api/notifications/send/user/user@erp.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testNotification)))
                .andExpect(status().isOk())
                .andExpect(content().string("Notification sent to user: user@erp.com"));
    }

    @Test
    void testSendToTopicNotification() throws Exception {
        Mockito.doNothing().when(notificationService).sendToTopic(eq("sales"), any(NotificationMessage.class));

        mockMvc.perform(post("/api/notifications/send/topic/sales")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testNotification)))
                .andExpect(status().isOk())
                .andExpect(content().string("Notification sent to topic: sales"));
    }

    @Test
    void testGetUserNotifications() throws Exception {
        Mockito.when(notificationService.getNotificationsForUser("user@erp.com"))
                .thenReturn(List.of(testNotification));

        mockMvc.perform(get("/api/notifications/user/user@erp.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title").value("Test Notification"))
                .andExpect(jsonPath("$[0].message").value("Hello World!"));
    }

    @Test
    void testGetAllNotifications() throws Exception {
        Mockito.when(notificationService.getAllNotifications())
                .thenReturn(List.of(testNotification));

        mockMvc.perform(get("/api/notifications"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title").value("Test Notification"))
                .andExpect(jsonPath("$[0].message").value("Hello World!"));
    }
}