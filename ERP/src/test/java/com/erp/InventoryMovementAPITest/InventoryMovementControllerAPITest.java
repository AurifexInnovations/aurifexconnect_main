package com.erp.InventoryMovementAPITest;

import com.erp.Dto.Request.InventoryMovementRequest;
import com.erp.Model.Branch;
import com.erp.Repository.Branch.BranchRepository;
import com.erp.Repository.InventoryMovement.InventoryMovementRepository;
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
public class InventoryMovementControllerAPITest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private InventoryMovementRepository inventoryMovementRepository;

    @Autowired
    private BranchRepository branchRepository;

    private static long branchId;

    @Test
    @Order(1)
    void testCreateBranch() {
        // Save branch first
        Branch branch = new Branch();
        branch.setBranchName("Movement Branch");
        branch.setLocation("Pune, Maharashtra");
        branchRepository.save(branch);
        branchId = branch.getBranchId();
    }

    @Test
    @Order(2)
    void testProcessInventoryMovement() throws Exception {
        InventoryMovementRequest movementRequest = new InventoryMovementRequest();
        movementRequest.setVoucherId(1);
        movementRequest.setLineItemId(1);
        movementRequest.setBranchId(branchId); // using saved branch ID
        movementRequest.setInventoryId(1);
        movementRequest.setLedgerId(1);
        movementRequest.setItemQuantity(5.5);

        mockMvc.perform(post("/inventory-movement/process")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(movementRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Inventory Movement processed successfully!"))
                .andExpect(jsonPath("$.data.itemQuantity").value(5.5));
    }

    @Test
    @Order(3)
    void cleanUp() {
        inventoryMovementRepository.deleteAll();
        branchRepository.deleteAll();
    }
}