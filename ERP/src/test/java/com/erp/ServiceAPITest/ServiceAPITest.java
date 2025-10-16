package com.erp.ServiceAPITest;

import com.erp.Dto.Request.ServiceRequest;
import com.erp.Enum.ServiceStatus;
import com.erp.Model.Service;
import com.erp.Repository.Service.ServiceRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
@SpringBootTest
public class ServiceAPITest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ServiceRepository serviceRepository;

    private static long serviceId;

    @Test
    @Order(1)
    void testAddService() throws Exception {
        ServiceRequest serviceRequest = new ServiceRequest();
        serviceRequest.setServiceName("Screen Repair");
        serviceRequest.setCategories("Repair");
        serviceRequest.setServiceStatus(ServiceStatus.AVAILABLE);

        // Save directly for ID retrieval
        Service service = new Service();
        service.setServiceName(serviceRequest.getServiceName());
        service.setCategories(serviceRequest.getCategories());
        service.setServiceStatus(serviceRequest.getServiceStatus());
        serviceRepository.save(service);
        serviceId = service.getServiceId();

        mockMvc.perform(post("/service")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(serviceRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(2)
    void testGetServiceById() throws Exception {
        mockMvc.perform(get("/service/" + serviceId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.serviceName").value("Screen Repair"))
                .andExpect(jsonPath("$.data.category").value("Repair"))
                .andExpect(jsonPath("$.data.serviceStatus").value(ServiceStatus.AVAILABLE.toString()));
    }

    @Test
    @Order(3)
    void testUpdateServiceById() throws Exception {
        ServiceRequest updateRequest = new ServiceRequest();
        updateRequest.setServiceName("Display Replacement");
        updateRequest.setCategories("Repair");
        updateRequest.setServiceStatus(ServiceStatus.AVAILABLE);

        mockMvc.perform(put("/service/" + serviceId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/service/" + serviceId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.serviceName").value("Display Replacement"));
    }

    @Test
    @Order(4)
    void testGetServiceByName() throws Exception {
        mockMvc.perform(get("/service/by-name/Display Replacement"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].serviceName").value("Display Replacement"));
    }

    @Test
    @Order(5)
    void testGetAllServices() throws Exception {
        mockMvc.perform(get("/service/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    @Order(6)
    void testGetServicesByStatus() throws Exception {
        mockMvc.perform(get("/service/by-status/" + ServiceStatus.AVAILABLE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    @Order(7)
    void testGetServicesByCategory() throws Exception {
        mockMvc.perform(get("/service/by-category/Repair"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    @Order(8)
    void testFetchCategoriesDropdown() throws Exception {
        mockMvc.perform(get("/service/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    @Order(9)
    void testDeleteServiceById() throws Exception {
        mockMvc.perform(delete("/service/" + serviceId))
                .andExpect(status().isOk());

        mockMvc.perform(get("/service/" + serviceId))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(10)
    void cleanUp() {
        serviceRepository.deleteAll();
    }
}
