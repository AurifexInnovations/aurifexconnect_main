package com.erp.Controller.crm;

import com.erp.Dto.Request.CustomerRequestDto;
import com.erp.Dto.Response.CustomerResponseDto;
import com.erp.Service.crm.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contacts")
@AllArgsConstructor
@Tag(name = "Customer API", description = "Operations on customers")
public class CustomerController {
    private final CustomerService service;

    @GetMapping
    public Page<CustomerResponseDto> getCustomers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return service.findByCriteria(name, email, pageable);
    }

    @Operation(summary = "Create contact")
    @PostMapping("/create")
    public ResponseEntity<CustomerResponseDto> create(
            @RequestBody CustomerRequestDto dto) {
        return new ResponseEntity<>(service.create(dto), HttpStatus.CREATED);
    }

    @Operation(summary = "Fetch contact by ID")
    @GetMapping("/record/{id}")
    public ResponseEntity<CustomerResponseDto> fetchById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @Operation(summary = "Fetch all contacts")
    @GetMapping("/all")
    public ResponseEntity<List<CustomerResponseDto>> fetchAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Delete contact by ID")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete all contacts")
    @DeleteMapping("/delete-all")
    public ResponseEntity<Void> deleteAll() {
        service.deleteAll();
        return ResponseEntity.noContent().build();
    }
}
