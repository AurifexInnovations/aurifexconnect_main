package com.erp.AttendanceAPITest;

import com.erp.Dto.Request.AttendanceRequest;
import com.erp.Dto.Request.Param;
import com.erp.Model.User;
import com.erp.Repository.User.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDate;
import java.time.LocalDateTime;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AllArgsConstructor
public class AttendanceAPITest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;

    private Long userId;

    @BeforeEach
    void setup() {
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("attendance@example.com");
        user.setPassword("password");
        user.setPhoneNo(9999999999L);
        userRepository.save(user);
        userId = user.getId();
    }

    @Test
    void testCheckIn() throws Exception {
        Param param = new Param();
        param.setUserId(userId);

        mockMvc.perform(post("/api/attendance/check-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(param)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Checked in successfully"));
    }
    @Test
    void testCheckOut() throws Exception {
        Param param = new Param();
        param.setUserId(userId);

        // Simulate check-in before check-out
        mockMvc.perform(post("/api/attendance/check-in")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(param)));

        mockMvc.perform(post("/api/attendance/check-out")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(param)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Checked out successfully"));
    }
    @Test
    void testUpdateAttendance() throws Exception {
        AttendanceRequest request = new AttendanceRequest();
        request.setUserId(userId);
        request.setDate(LocalDate.now());
        request.setCheckInTime(LocalDateTime.now().minusHours(8));
        request.setCheckOutTime(LocalDateTime.now());

        mockMvc.perform(put("/api/attendance/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Attendance updated successfully"));
    }
    @Test
    void testGetAttendanceById() throws Exception {
        Param param = new Param();
        param.setUserId(userId);

        mockMvc.perform(get("/api/attendance/getAttendance")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(param)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Attendance record found"));
    }
    @Test
    void testGetAllAttendances() throws Exception {
        mockMvc.perform(get("/api/attendance/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("All attendance records fetched"));
    }
    @Test
    void testGetByUserId() throws Exception {
        Param param = new Param();
        param.setUserId(userId);

        mockMvc.perform(post("/api/attendance/user/records")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(param)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Attendance records for user fetched"));
    }
    @Test
    void testMonthlyReport() throws Exception {
        AttendanceRequest request = new AttendanceRequest();
        request.setUserId(userId);
        request.setDate(LocalDate.now()); // Assuming controller filters by date/month

        mockMvc.perform(post("/api/attendance/user/monthly-report")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Monthly attendance report fetched"));
    }

    @Test
    void testDeleteAttendanceByUserIDandDate() throws Exception {
        AttendanceRequest request = new AttendanceRequest();
        request.setUserId(userId);
        request.setDate(LocalDate.now());

        mockMvc.perform(delete("/api/attendance/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Attendance deleted successfully"));
    }

    @Test
    void testDeleteAllAttendances() throws Exception {
        AttendanceRequest request = new AttendanceRequest();
        request.setUserId(userId);

        mockMvc.perform(delete("/api/attendance/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Attendances Deleted Successfully!"));
    }
}