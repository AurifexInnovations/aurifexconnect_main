package com.erp.SalarySlipAPITest;

import com.erp.Controller.SalarySlip.SalarySlipController;
import com.erp.Dto.Request.SalaryRequest;
import com.erp.Dto.Response.SalaryResponse;
import com.erp.Dto.Response.UserResponse;
import com.erp.Enum.AmountStatus;
import com.erp.Service.SalaryService.SalaryService;
import com.erp.Service.SalarySlip.SalarySlipGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.time.YearMonth;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(controllers = SalarySlipController.class)
@Import(SalarySlipAPITest.MockedBeans.class)
@ImportAutoConfiguration(exclude = {
        HibernateJpaAutoConfiguration.class,
        JpaRepositoriesAutoConfiguration.class,
        DataSourceAutoConfiguration.class
})
class SalarySlipAPITest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SalaryService salaryService;

    @Autowired
    private SalarySlipGenerator salarySlipGenerator;

    @Autowired
    private ObjectMapper objectMapper; // For automatic JSON conversion

    @TestConfiguration
    static class MockedBeans {
        @Bean
        public SalaryService salaryService() {
            return Mockito.mock(SalaryService.class);
        }

        @Bean
        public SalarySlipGenerator salarySlipGenerator() {
            return Mockito.mock(SalarySlipGenerator.class);
        }

        @Bean
        public ObjectMapper objectMapper() {
            return new ObjectMapper();
        }
    }

    @Test
    void testGenerateSalaryView() throws Exception {
        // Arrange
        SalaryRequest request = new SalaryRequest();
        request.setUserId(1L);
        request.setMonth(YearMonth.of(2024, 5));
        request.setBaseSalary(50000L);
        request.setBonus(5000L);
        request.setDeductions(2000L);
        request.setWorkingDays(22);
        request.setPaidDays(20);
        request.setRemarks("May Salary");

        SalaryResponse response = new SalaryResponse();
        response.setId(1L);
        UserResponse userResponse = new UserResponse();
        userResponse.setId(1L);
        userResponse.setFirstName("John");
        userResponse.setLastName("Doe");
        response.setUser(userResponse);
        response.setMonth(request.getMonth());
        response.setBaseSalary(request.getBaseSalary());
        response.setBonus(request.getBonus());
        response.setDeductions(request.getDeductions());
        response.setWorkingDays(request.getWorkingDays());
        response.setPaidDays(request.getPaidDays());
        response.setNetSalary(53000L); // Corrected calculation: 50000 + 5000 - 2000 = 53000
        response.setRemarks("May Salary");
        response.setAmountStatus(AmountStatus.PENDING);

        Mockito.when(salaryService.generateSalaryForMonth(any(SalaryRequest.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/salary/generate") // ensure this endpoint exists in your SalarySlipController
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(view().name("salary-slip")) // Make sure your controller returns "salary-slip" view
                .andExpect(model().attributeExists("salary"))
                .andDo(print());
    }

    @Test
    void testGenerateSalarySlipPdf() throws Exception {
        // Arrange
        SalaryRequest request = new SalaryRequest();
        request.setUserId(1L);
        request.setMonth(YearMonth.of(2024, 5));

        byte[] dummyPdf = "PDF-DATA".getBytes();
        Mockito.when(salarySlipGenerator.generateSlipPdf(any(SalaryRequest.class))).thenReturn(dummyPdf);

        // Act & Assert
        mockMvc.perform(post("/salary/slip") // ensure this endpoint exists in your SalarySlipController
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment; filename=SalarySlip-1.pdf"))
                .andExpect(content().contentType(MediaType.APPLICATION_PDF))
                .andExpect(content().bytes(dummyPdf))
                .andDo(print());
    }
}
