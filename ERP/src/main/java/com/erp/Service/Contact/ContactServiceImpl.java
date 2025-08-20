package com.erp.Service.Contact;

import com.erp.Dto.Request.ContactRequest;
import com.erp.Dto.Response.ContactResponse;
import com.erp.Mapper.Contact.ContactMapper;
import com.erp.Model.Contact;
import com.erp.Repository.Contact.ContactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements ContactService {

    private final ContactRepository contactRepo;
    private final ContactMapper contactMapper;

    @Override
    public ContactResponse create(ContactRequest request) {
        Contact contact = contactMapper.mapToEntity(request);
        Contact savedContact = contactRepo.save(contact);
        return contactMapper.mapToResponse(savedContact);
    }

    @Override
    public ContactResponse update(Long id, ContactRequest request) {
        Contact contact = contactRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Contact not found"));
        contactMapper.updateEntityFromRequest(request, contact);
        Contact updatedContact = contactRepo.save(contact);
        return contactMapper.mapToResponse(updatedContact);
    }

    @Override
    public ContactResponse getById(Long id) {
        Contact contact = contactRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Contact not found"));
        return contactMapper.mapToResponse(contact);
    }

    @Override
    public List<ContactResponse> getAll() {
        List<Contact> contacts = contactRepo.findAll();
        return contactMapper.mapToResponseList(contacts);
    }

    @Override
    public void delete(Long id) {
        if (!contactRepo.existsById(id)) {
            throw new RuntimeException("Contact not found");
        }
        contactRepo.deleteById(id);
    }

    @Override
    public void deleteAll() {
        contactRepo.deleteAll();
    }
}
