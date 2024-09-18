package com.learnify.service;


import com.learnify.constants.AppConstants;
import com.learnify.model.Contact;
import com.learnify.repository.ContactRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ContactService {

    @Autowired
    private ContactRepository contactRepository;

    public ContactService(){
        System.out.println("Contact Service Bean Created !!");
    }
    public boolean saveMessageDetails(Contact contact){
        boolean isSaved = false;
        contact.setStatus(AppConstants.OPEN);
        Contact savedContact = contactRepository.save(contact);
        if(null != savedContact && savedContact.getContactId()>0){
            isSaved = true;
        }
        return isSaved;
    }
    public boolean updateMessageStatus(int contactId){
        boolean isUpdated = false;
        Optional<Contact> contact = contactRepository.findById(contactId);
        contact.ifPresent(contact1 -> {
            contact1.setStatus(AppConstants.CLOSE);
        });
        Contact updatedContact = contactRepository.save(contact.get());
        if(null != updatedContact && updatedContact.getUpdatedBy() != null){
            isUpdated = true;
        }
        return isUpdated;
    }

    public Page<Contact> findMsgWithOpenStatus(int pageNum, String sortField, String sortDir) {

        int pageSize = 5;
        Pageable pageable = PageRequest.of(pageNum-1, pageSize,
                sortDir.equals("asc")? Sort.by(sortField).ascending(): Sort.by(sortField).descending());

        return this.contactRepository.findByStatus(AppConstants.OPEN, pageable);
    }
}
