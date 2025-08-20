package com.erp.Mapper.Contact;

import com.erp.Dto.Request.ContactRequest;
import com.erp.Dto.Response.ContactResponse;
import com.erp.Model.Contact;
import com.erp.Model.Lead;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ContactMapper {

    ContactResponse mapToResponse(Contact contact);

    List<ContactResponse> mapToResponseList(List<Contact> contacts);

    Contact mapToEntity(ContactRequest request);

    void updateEntityFromRequest(ContactRequest request, @MappingTarget Contact contact);

    // Create Contact from Lead (Lead → Contact conversion)
    Contact mapFromLead(Lead lead);
}
