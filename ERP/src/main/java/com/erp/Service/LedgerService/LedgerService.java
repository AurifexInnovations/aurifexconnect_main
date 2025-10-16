package com.erp.Service.LedgerService;

import com.erp.Dto.Request.CommanParam;
import com.erp.Dto.Request.LedgerRequest;
import com.erp.Dto.Response.LedgerResponse;

import java.util.List;

public interface LedgerService {

    /**
     * Creates a new customer record.
     * <p>
     * This method takes a {@link LedgerRequest} containing details like name, email, phone, and address,
     * and processes it to create a new customer.
     * </p>
     *
     * @param ledgerRequest The request object containing the customer’s details.
     * @return LedgerResponse A response containing information about the newly created customer.
     */
    LedgerResponse createLedger(LedgerRequest ledgerRequest);

    /**
     * Updates an existing customer’s information.
     *
     * @param ledgerRequest The updated customer details.
     * @return LedgerResponse The updated customer information or an error response if the update fails.
     */
    LedgerResponse updateLedgerInfo(LedgerRequest ledgerRequest);

    /**
     * Deletes a customer by their ID.
     *
     * @param customerId The ID of the customer to delete.
     * @return LedgerResponse A response confirming successful deletion or indicating failure.
     */
    LedgerResponse deleteByLedgerId(LedgerRequest InventoryId);

    /**
     * Retrieves all customer records from the database.
     *
     * @return List<LedgerResponse> A list of all customers in the system.
     */
    List<LedgerResponse> getAllLedger();

    List<LedgerResponse> getLedgerByIdOrName(CommanParam param);
}
