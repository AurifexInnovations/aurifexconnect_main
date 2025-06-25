package com.erp.Service.InventoryService;

import com.erp.Dto.Request.CommanParam;
import com.erp.Dto.Request.InventoryRequest;
import com.erp.Dto.Response.InventoryResponse;
import com.erp.Dto.Response.StockValueResponse;

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
     * Updates an existing inventory item.
     * <p>
     * Accepts an {@link InventoryRequest} containing updated details for an existing inventory item.
     * </p>
     *
     * @param inventoryRequest The request object containing updated inventory item details.
     * @return InventoryResponse containing the updated item details.
     */
    InventoryResponse updateItem(InventoryRequest inventoryRequest);

    /**
     * Retrieves inventory items by their ID or name.
     * <p>
     * Accepts a {@link CommanParam} containing either the item ID or name to search for.
     * Returns a list of matching {@link InventoryResponse} objects.
     * </p>
     *
     * @param id The common parameter containing either item ID or name.
     * @return List of matching InventoryResponse objects.
     */
    List<InventoryResponse> findByItemIdOrName(CommanParam id);

    /**
     * Deletes an inventory item by its ID.
     * <p>
     * Accepts an {@link InventoryRequest} containing the item ID to delete.
     * Returns an {@link InventoryResponse} indicating success or failure.
     * </p>
     *
     * @param inventoryRequest The request object containing the item ID to delete.
     * @return InventoryResponse with delete operation result.
     */
    InventoryResponse deleteByItemId(InventoryRequest inventoryRequest);

    /**
     * Retrieves items from the inventory by their name.
     *
     * @param  The name of the items to search for.
     * @return List<InventoryResponse> A list of matching items, or an empty list if none found.
     */
//    List<InventoryResponse> findByItemName(String itemName);

    /**
     * Retrieves all inventory items.
     * <p>
     * Returns a list of all existing {@link InventoryResponse} objects in the system.
     * </p>
     *
     * @return List of all InventoryResponse objects.
     */
    List<InventoryResponse> findByAll();

    /**
     * Fetches all available inventory categories.
     * <p>
     * Returns a list of unique category names as strings.
     * </p>
     *
     * @return List of category names.
     */
    List<String> fetchAllCategories();

    /**
     * Retrieves stock value details for all inventory items.
     * <p>
     * Calculates stock value as (itemQuantity * itemCost) for each item and returns a list of {@link StockValueResponse}.
     * </p>
     *
     * @return List of StockValueResponse containing item name and its stock value.
     */
    List<StockValueResponse> getStockValueList();
}
