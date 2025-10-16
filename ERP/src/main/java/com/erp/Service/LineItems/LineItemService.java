package com.erp.Service.LineItems;

import com.erp.Dto.Request.LineItemsRequest;
import com.erp.Dto.Response.LineItemsResponse;

public interface LineItemService {

    LineItemsResponse createLineItems(LineItemsRequest request);

//    LineItemsResponse updateLineItem(long lineItemId, double newQuantity, long ledgerId);

//    void deleteLineItem(long lineItemId, long ledgerId);
}
