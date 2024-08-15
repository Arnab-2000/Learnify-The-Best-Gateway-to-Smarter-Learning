package com.learnify.service;


import com.learnify.constants.AppConstants;
import com.learnify.model.Contact;
import com.learnify.repository.ContactRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
//@SessionScope
public class ContactService {

    private int counter;
    @Autowired
    private ContactRepository contactRepository;

    public ContactService(){
        this.counter=0;
        System.out.println("Contact Service Bean Created !!");
    }
    public boolean saveMessageDetails(Contact contact){
        boolean isSaved = false;
        contact.setStatus(AppConstants.OPEN);
        contact.setCreatedAt(LocalDateTime.now());
        contact.setCreatedBy(AppConstants.ANONYMOUS);
        int result = contactRepository.SaveContactMsgtoDb(contact);
        if(result>0){
            isSaved = true;
        }
        return isSaved;
    }

//    public int getCounter() {
//        return counter;
//    }
//
//    public void setCounter(int counter) {
//        this.counter = counter;
//    }
}
