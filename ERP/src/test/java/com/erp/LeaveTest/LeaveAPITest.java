package com.erp.LeaveTest;

import com.erp.Dto.Request.LeaveRequest;
import com.erp.Dto.Request.Param;
import com.erp.Enum.LeaveStatus;
import com.erp.Model.User;
import com.erp.Repository.User.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Rollback
public class LeaveAPITest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    private long userId;
    private long leaveId;

    @BeforeEach
    void setUp() throws Exception {
        User user = new User();
        user.setFirstName("Test");
        user.setLastName("User");
        user.setEmail("leaveuser@example.com");
        user.setPassword("test123");
        user.setPhoneNo(9999999999L);
        userRepository.save(user);
        userId = user.getId();

        LeaveRequest request = new LeaveRequest();
        request.setUserId(userId);
        request.setFirstName("Test");
        request.setLastName("User");
        request.setStartDate(LocalDate.now());
        request.setEndDate(LocalDate.now().plusDays(2));
        request.setLeaveType("PAID");
        request.setReason("Medical Leave");

        String response = mockMvc.perform(post("/leave/apply")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        leaveId = objectMapper.readTree(response).path("data").path("id").asLong();
    }

    @Test
    void testApplyLeave() throws Exception {
        LeaveRequest request = new LeaveRequest();
        request.setUserId(userId);
        request.setFirstName("Test");
        request.setLastName("User");
        request.setStartDate(LocalDate.now().plusDays(5));
        request.setEndDate(LocalDate.now().plusDays(7));
        request.setLeaveType("CASUAL");
        request.setReason("Family Function");

        mockMvc.perform(post("/leave/apply")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Leave request submitted"));
    }

    @Test
    void testGetLeaveByUserId() throws Exception {
        Param param = new Param();
        param.setUserId(userId);

        mockMvc.perform(post("/leave/getbyuserid")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(param)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Leave requests by user"));
    }

    @Test
    void testUpdateLeave() throws Exception {
        LeaveRequest request = new LeaveRequest();
        request.setId(leaveId);
        request.setUserId(userId);
        request.setStartDate(LocalDate.now().plusDays(2));
        request.setEndDate(LocalDate.now().plusDays(4));
        request.setLeaveType("EMERGENCY");
        request.setReason("Updated Reason");

        mockMvc.perform(put("/leave/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Leave request updated"));
    }

    @Test
    void testApproveLeave() throws Exception {
        LeaveRequest request = new LeaveRequest();
        request.setId(leaveId);
        request.setUserId(userId);
        request.setStatus(LeaveStatus.APPROVED);

        mockMvc.perform(put("/leave/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Leave status updated"));
    }

    @Test
    void testRejectLeave() throws Exception {
        LeaveRequest request = new LeaveRequest();
        request.setId(leaveId);
        request.setUserId(userId);
        request.setStatus(LeaveStatus.REJECTED);

        mockMvc.perform(put("/leave/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Leave status updated"));
    }

    @Test
    void testGetAllLeaveRequests() throws Exception {
        mockMvc.perform(post("/leave/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("All leave requests retrieved"));
    }

    @Test
    void testGetLeavesByStatus() throws Exception {
        LeaveRequest request = new LeaveRequest();
        request.setStatus(LeaveStatus.PENDING);

        mockMvc.perform(post("/leave/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Leave requests by status"));
    }

    @Test
    void testGetLeavesByDateRange() throws Exception {
        LeaveRequest request = new LeaveRequest();
        request.setStartDate(LocalDate.now().minusDays(1));
        request.setEndDate(LocalDate.now().plusDays(5));

        mockMvc.perform(post("/leave/date-range")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Leave requests by date range"));
    }

    @Test
    void testDeleteLeave() throws Exception {
        Param param = new Param();
        param.setId(leaveId);

        mockMvc.perform(post("/leave/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(param)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Leave deleted"));
    }
}
