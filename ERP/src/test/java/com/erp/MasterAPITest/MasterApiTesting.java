package com.erp.MasterAPITest;

import com.erp.Dto.Request.MasterRequest;
import com.erp.Enum.VoucherType;
import com.erp.Model.BankAccount;
import com.erp.Model.Ledger;
import com.erp.Model.Master;
import com.erp.Repository.BankAccount.BankAccountRepository;
import com.erp.Repository.Ledger.LedgerRepository;
import com.erp.Repository.Master.MasterRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)  // For ordering tests
public class MasterApiTesting {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MasterRepository masterRepository;

    @Autowired
    private LedgerRepository ledgerRepository;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private static long ledgerId;
    private static long bankAccountId;
    private static long masterId;

    @BeforeEach
    void setup() {
        if (ledgerRepository.count() == 0) {
            Ledger ledger = new Ledger();
            ledger.setName("Test Ledger");
            ledger = ledgerRepository.save(ledger);
            ledgerId = ledger.getLedgerId();

        }

        if (bankAccountRepository.count() == 0) {
            BankAccount bankAccount = new BankAccount();
            bankAccount.setAccountNumber("123456789");
            bankAccount.setCurrentBalance(10000.0);
            bankAccount = bankAccountRepository.save(bankAccount);
            bankAccountId = bankAccount.getBankAccountId();
        }
    }

    @Test
    @Order(1)
    void testCreateMaster() throws Exception {
        MasterRequest request = new MasterRequest();
        request.setName("Test Master");
        request.setVoucherType(VoucherType.SALES);
        request.setAmount(5000.0);
        request.setDescription("Testing Create Master");
        request.setLedgerId(ledgerId);
        request.setBankAccountId(bankAccountId);

        String response = mockMvc.perform(post("/master")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Master Created Successfully"))
                .andReturn().getResponse().getContentAsString();

        // Extract MasterId from response if needed (you can parse it if required)
        Master master = masterRepository.findAll().get(0);
        masterId = master.getMasterId();
    }

    @Test
    @Order(2)
    void testFindByMasterId() throws Exception {
        MasterRequest request = new MasterRequest();
        request.setFindMasterId(masterId); // use the id generated in previous test

        mockMvc.perform(get("/master")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Master Found Successfully"));
    }

    @Test
    @Order(3)
    void testGetSalesVsPurchaseComparisonSummary() throws Exception {
        Map<String, String> request = new HashMap<>();
        request.put("type", "day");

        mockMvc.perform(post("/master/sales-vs-purchase-comparison-summary")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Sales vs Purchase comparison summary fetched successfully"));
    }

    @Test
    @Order(4)
    void testGetInsightsSummary() throws Exception {
        Map<String, String> request = new HashMap<>();
        request.put("type", "month");
        request.put("startDate", "2024-01-01");
        request.put("endDate", "2024-12-31");

        mockMvc.perform(post("/master/insights-summary")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Insights summary fetched successfully"));
    }
}
