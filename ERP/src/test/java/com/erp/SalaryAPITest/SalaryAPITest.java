package com.erp.SalaryAPITest;

import com.erp.Dto.Request.Param;
import com.erp.Dto.Request.SalaryRequest;
import com.erp.Model.Salary;
import com.erp.Model.User;
import com.erp.Repository.Salary.SalaryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.time.YearMonth;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SalaryAPITest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SalaryRepository salaryRepository;

    private static SalaryRequest salaryRequest;
    private static final long userId = 1L;
    private static final YearMonth salaryMonth = YearMonth.of(2024, 12);

    @BeforeAll
    static void initRequest() {
        salaryRequest = new SalaryRequest();
        salaryRequest.setUserId(userId);
        salaryRequest.setMonth(salaryMonth);
        salaryRequest.setBaseSalary(50000L);
        salaryRequest.setBonus(5000L);
        salaryRequest.setDeductions(2000L);
        salaryRequest.setPaidDays(26);
        salaryRequest.setRemarks("Integration Test");
    }

    @Test
    @Order(1)
    void testCreateSalaryManually() {
        Salary salary = new Salary();
        User user = new User();
        user.setId(userId);
        salary.setUser(user);
        salary.setMonth(salaryMonth);
        salary.setBaseSalary(50000L);
        salary.setBonus(5000L);
        salary.setDeductions(2000L);
        salary.setPaidDays(26);
        salary.setAmountStatus(null);
        salary.setRemarks("Integration Test");
        salaryRepository.save(salary);
    }

    @Test
    @Order(2)
    void testGetSalariesByUser() throws Exception {
        Param param = new Param();
        param.setUserId(userId);

        mockMvc.perform(post("/salary/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(param)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Salary records fetched"));
    }

    @Test
    @Order(3)
    void testGetUserSalaryByMonth() throws Exception {
        mockMvc.perform(post("/salary/user/salary-month")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(salaryRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Salary record fetched"));
    }

    @Test
    @Order(4)
    void testMarkSalaryAsPaid() throws Exception {
        mockMvc.perform(put("/salary/pay")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(salaryRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Salary marked as paid"));
    }

    @Test
    @Order(5)
    void testUpdateSalary() throws Exception {
        salaryRequest.setBonus(8000L); // update bonus

        mockMvc.perform(put("/salary/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(salaryRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Salary updated successfully"));
    }

    @Test
    @Order(6)
    void testGetAllSalaries() throws Exception {
        mockMvc.perform(get("/salary?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("All salaries fetched"));
    }

    @Test
    @Order(7)
    void testGetSalariesByMonth() throws Exception {
        mockMvc.perform(post("/salary/month")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(salaryRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Salaries for month fetched"));
    }

    @Test
    @Order(8)
    void testDeleteSalaryByUserAndMonth() throws Exception {
        mockMvc.perform(delete("/salary/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(salaryRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Salary record deleted successfully!"));
    }

    @Test
    @Order(9)
    void cleanUp() {
        salaryRepository.deleteAll();
    }
}
