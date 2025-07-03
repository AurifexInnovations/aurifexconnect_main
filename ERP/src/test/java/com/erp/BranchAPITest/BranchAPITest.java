package com.erp.BranchAPITest;

import com.erp.Dto.Request.BranchRequest;
import com.erp.Model.Branch;
import com.erp.Repository.Branch.BranchRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static com.erp.Enum.BranchStatus.ACTIVE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
@SpringBootTest
public class BranchAPITest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BranchRepository branchRepository;

    private static long branchId;

    @Test
    @Order(1)
    void testCreateBranch() throws Exception {
        BranchRequest branchRequest = new BranchRequest();
        branchRequest.setBranchName("Main Office");
        branchRequest.setLocation("Bengaluru, Karnataka");
        branchRequest.setContactInfo("+91-9876543210");
        branchRequest.setBranchStatus(ACTIVE);

        // Saving directly for ID retrieval
        Branch branch = new Branch();
        branch.setBranchName(branchRequest.getBranchName());
        branch.setLocation(branchRequest.getLocation());
        branch.setContactInfo(branchRequest.getContactInfo());
        branch.setBranchStatus(branchRequest.getBranchStatus());
        branchRepository.save(branch);
        branchId = branch.getBranchId();

        mockMvc.perform(post("/branch")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(branchRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(2)
    void testFindBranchById() throws Exception {
        mockMvc.perform(get("/branch/" + branchId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.branchName").value("Main Office"))
                .andExpect(jsonPath("$.data.location").value("Bengaluru, Karnataka"))
                .andExpect(jsonPath("$.data.contactInfo").value("+91-9876543210"))
                .andExpect(jsonPath("$.data.branchStatus").value(ACTIVE.toString()));
    }

    @Test
    @Order(3)
    void testUpdateBranchById() throws Exception {
        BranchRequest updateRequest = new BranchRequest();
        updateRequest.setBranchName("Updated Office");
        updateRequest.setLocation("Mumbai, Maharashtra");
        updateRequest.setContactInfo("+91-9876543220");
        updateRequest.setBranchStatus(ACTIVE);

        // Correct URL
        mockMvc.perform(put("/" + branchId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk());

        // Verify update
        mockMvc.perform(get("/branch/" + branchId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.branchName").value("Updated Office"))
                .andExpect(jsonPath("$.data.location").value("Mumbai, Maharashtra"))
                .andExpect(jsonPath("$.data.contactInfo").value("+91-9876543220"))
                .andExpect(jsonPath("$.data.branchStatus").value(ACTIVE.toString()));
    }

    @Test
    @Order(4)
    void testFindBranchByName() throws Exception {
        mockMvc.perform(get("/?branchName=Updated Office"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].branchName").value("Updated Office"))
                .andExpect(jsonPath("$.data[0].location").value("Mumbai, Maharashtra"))
                .andExpect(jsonPath("$.data[0].contactInfo").value("+91-9876543220"))
                .andExpect(jsonPath("$.data[0].branchStatus").value(ACTIVE.toString()));
    }

    @Test
    @Order(5)
    void testGetAllBranches() throws Exception {
        mockMvc.perform(get("/branch/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].branchName").value("Updated Office"))
                .andExpect(jsonPath("$.data[0].location").value("Mumbai, Maharashtra"))
                .andExpect(jsonPath("$.data[0].contactInfo").value("+91-9876543220"))
                .andExpect(jsonPath("$.data[0].branchStatus").value(ACTIVE.toString()));
    }

    @Test
    @Order(6)
    void testGetBranchesByItem() throws Exception {
        String itemName = "Laptop";

        mockMvc.perform(get("/branch/by-item/" + itemName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    @Order(7)
    void testDeleteBranchById() throws Exception {
        mockMvc.perform(delete("/branch/" + branchId))
                .andExpect(status().isOk());

        mockMvc.perform(get("/branch/" + branchId))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(8)
    void cleanUp() {
        branchRepository.deleteAll();
    }
}
