package com.erp.Controller.PurchaseOrder;

import com.erp.Dto.Request.PurchaseOrderRequest;
import com.erp.Dto.Response.PurchaseOrderResponse;
import com.erp.Service.PurchaseOrder.PurchaseOrderService;
import com.erp.Utility.ListResponseStructure;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.ResponseStructure;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/purchase-orders")
@RequiredArgsConstructor
public class PurchaseOrderController {

    private final PurchaseOrderService service;

    @PostMapping
    public ResponseEntity<ResponseStructure<PurchaseOrderResponse>> create(@RequestBody PurchaseOrderRequest request){
        return ResponseBuilder.success(HttpStatus.CREATED, "Purchase Order created", service.create(request));
    }

    @GetMapping
    public ResponseEntity<ListResponseStructure<PurchaseOrderResponse>> getAll(){
        return ResponseBuilder.success(HttpStatus.OK, "All Purchase Orders", service.getAll());
    }

    @GetMapping("/by-id")
    public ResponseEntity<ResponseStructure<PurchaseOrderResponse>> getById(@RequestParam Long id){
        return ResponseBuilder.success(HttpStatus.OK, "Purchase Order fetched", service.getById(id));
    }

    @PutMapping
    public ResponseEntity<ResponseStructure<PurchaseOrderResponse>> update(@RequestParam Long id, @RequestBody PurchaseOrderRequest request){
        return ResponseBuilder.success(HttpStatus.OK, "Purchase Order updated", service.update(id, request));
    }

    @DeleteMapping
    public ResponseEntity<ResponseStructure<String>> delete(@RequestParam Long id){
        service.delete(id);
        return ResponseBuilder.success(HttpStatus.OK, "Purchase Order deleted", "Deleted");
    }
}
