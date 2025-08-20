package com.erp.Controller.Contact;

import com.erp.Dto.Request.ContactRequest;
import com.erp.Dto.Request.Param;
import com.erp.Dto.Response.ContactResponse;
import com.erp.Service.Contact.ContactService;
import com.erp.Utility.ListResponseStructure;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.ResponseStructure;
import com.erp.Utility.SimpleErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/crm/contacts")
@Tag(name = "Contact Controller", description = "Collection of API Endpoints for managing CRM Contacts")
public class ContactController {

    private final ContactService contactService;

    @PostMapping("/create")
    @Operation(description = """
            API endpoint to create a new contact record.
            """,
            responses = {
                    @ApiResponse(responseCode = "201", description = "Contact created successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input data",
                            content = @Content(schema = @Schema(implementation = SimpleErrorResponse.class)))
            })
    public ResponseEntity<ResponseStructure<ContactResponse>> create(@Valid @RequestBody ContactRequest request) {
        ContactResponse response = contactService.create(request);
        return ResponseBuilder.success(HttpStatus.CREATED, "Contact created successfully", response);
    }

    @PutMapping("/update")
    @Operation(description = """
            API endpoint to update an existing contact record by ID.
            """,
            responses = {
                    @ApiResponse(responseCode = "200", description = "Contact updated successfully"),
                    @ApiResponse(responseCode = "404", description = "Contact not found",
                            content = @Content(schema = @Schema(implementation = SimpleErrorResponse.class)))
            })
    public ResponseEntity<ResponseStructure<ContactResponse>> update(@Valid @RequestBody ContactRequest request) {
        ContactResponse response = contactService.update(request.getId(), request);
        return ResponseBuilder.success(HttpStatus.OK, "Contact updated successfully", response);
    }

    @PostMapping("/record")
    @Operation(description = """
            API endpoint to fetch a contact record by its ID.
            """,
            responses = {
                    @ApiResponse(responseCode = "200", description = "Contact fetched successfully"),
                    @ApiResponse(responseCode = "404", description = "Contact not found",
                            content = @Content(schema = @Schema(implementation = SimpleErrorResponse.class)))
            })
    public ResponseEntity<ResponseStructure<ContactResponse>> getById(@Valid @RequestBody Param param) {
        ContactResponse response = contactService.getById(param.getId());
        return ResponseBuilder.success(HttpStatus.OK, "Contact fetched successfully", response);
    }

    @GetMapping("/all")
    @Operation(description = """
            API endpoint to fetch all contact records.
            """,
            responses = {
                    @ApiResponse(responseCode = "200", description = "Contacts fetched successfully"),
                    @ApiResponse(responseCode = "404", description = "No contacts found",
                            content = @Content(schema = @Schema(implementation = SimpleErrorResponse.class)))
            })
    public ResponseEntity<ListResponseStructure<ContactResponse>> getAllContacts() {
        List<ContactResponse> responses = contactService.getAll();
        return ResponseBuilder.success(HttpStatus.OK, "Contacts fetched successfully", responses);
    }

    @DeleteMapping("/delete")
    @Operation(description = """
            API endpoint to delete a contact by its ID.
            """,
            responses = {
                    @ApiResponse(responseCode = "200", description = "Contact deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Contact not found",
                            content = @Content(schema = @Schema(implementation = SimpleErrorResponse.class)))
            })
    public ResponseEntity<ResponseStructure<String>> delete(@Valid @RequestBody Param param) {
        contactService.delete(param.getId());
        return ResponseBuilder.success(HttpStatus.OK, "Contact deleted successfully", "Deleted successfully");
    }

    @DeleteMapping("/delete-all")
    @Operation(description = """
            API endpoint to delete all contact records.
            """,
            responses = {
                    @ApiResponse(responseCode = "200", description = "All contacts deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "No contacts found",
                            content = @Content(schema = @Schema(implementation = SimpleErrorResponse.class)))
            })
    public ResponseEntity<ResponseStructure<String>> deleteAllContacts() {
        contactService.deleteAll();
        return ResponseBuilder.success(HttpStatus.OK, "All contacts deleted successfully", "Deleted successfully");
    }
}