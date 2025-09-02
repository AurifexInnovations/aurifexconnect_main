package com.erp.Service.Contact;


import com.erp.Dto.Request.ContactRequest;
import com.erp.Dto.Response.ContactResponse;

import java.util.List;

public interface ContactService {
    ContactResponse create(ContactRequest request);
    ContactResponse update(Long id, ContactRequest request);
    ContactResponse getById(Long id);
    List<ContactResponse> getAll();
    void delete(Long id);
    void deleteAll();
}
