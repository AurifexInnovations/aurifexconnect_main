package com.erp.Service.InventoryService;

import com.erp.Dto.Request.CommanParam;
import com.erp.Dto.Request.InventoryRequest;
import com.erp.Dto.Response.InventoryResponse;

import java.util.List;

public interface InventoryService {

    /**
     * Adds a new item to the inventory.
     * <p>
     * This method receives an `InventoryRequest` object containing the details of the item to be added (e.g., item name, quantity, price, etc.)
     * and processes the request to add the item to the inventory.
     * The method returns an `InventoryResponse` that includes the status of the operation and any relevant information about the added item.
     * </p>
     *
     * @param inventoryRequest The request object containing the details of the item to be added.
     *                         This includes information such as the item name, quantity, price, and other relevant fields.
     * @return InventoryResponse The response object containing the status of the addition operation and any details
     *                           of the added item (e.g., item ID, success message, or error information).
     *                           A successful response will typically include the details of the newly added item.
     */
    InventoryResponse addItem(InventoryRequest inventoryRequest);

    /**
     * Updates an existing item in the inventory.
     *
     * @param inventoryRequest The updated item details.
     * @param id The ID of the item to update.
     * @return InventoryResponse The updated item details or an error message if the update fails.
     */
    InventoryResponse updateItem(InventoryRequest inventoryRequest);

    /**
     * Retrieves an item from the inventory by its ID.
     *
     * @param id The ID of the item to retrieve.
     * @return InventoryResponse The item details, or an error message if not found.
     */
    List<InventoryResponse> findByItemIdOrName(CommanParam id);

    /**
     * Deletes an item from the inventory by its ID.
     *
     * @param itemId The ID of the item to delete.
     * @return InventoryResponse A success or failure message.
     */
    InventoryResponse deleteByItemId(InventoryRequest inventoryRequest);

    /**
     * Retrieves items from the inventory by their name.
     *
     * @param  The name of the items to search for.
     * @return List<InventoryResponse> A list of matching items, or an empty list if none found.
     */
//    List<InventoryResponse> findByItemName(String itemName);

    List<InventoryResponse> findByAll();

    List<String> fetchAllCategories();
}
