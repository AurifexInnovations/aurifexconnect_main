package com.erp.Controller.LineItems;

import com.erp.Dto.Request.LineItemsRequest;
import com.erp.Dto.Response.LineItemsResponse;
import com.erp.Service.LineItems.LineItemService;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.ResponseStructure;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/")
public class LineItemsController {

    private final LineItemService lineItemService;

    @PostMapping("items")
    public ResponseEntity<ResponseStructure<LineItemsResponse>> createLineItems(@RequestBody LineItemsRequest request){
        LineItemsResponse lineItemsResponse = lineItemService.createLineItems(request);
        return ResponseBuilder.success(HttpStatus.CREATED,"Ledger LineItems Successfully", lineItemsResponse);
    }
}
