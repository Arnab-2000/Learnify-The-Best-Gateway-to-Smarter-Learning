package com.learnify.rest;

import com.learnify.constants.AppConstants;
import com.learnify.model.Contact;
import com.learnify.model.Response;
import com.learnify.repository.ContactRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/api/contact")
@CrossOrigin(origins="*")
public class ContactRestController {

    @Autowired
    private ContactRepository contactRepository;

    @GetMapping("/getMessagesByStatus")
    public List<Contact> getMessagesByStatus(@RequestParam(name = "status") String status){
        return this.contactRepository.findByStatus(status);
    }

    @GetMapping("/getAllMsgsByStatus")
    public List<Contact> getAllMsgsByStatus(@RequestBody Contact contact){
        if(null != contact && null != contact.getStatus()){
            return this.contactRepository.findByStatus(contact.getStatus());
        }
        return List.of();
    }

    @PostMapping("/saveMsg")
    public ResponseEntity<Response> saveMessage(@Valid @RequestBody Contact contact){
        Response response = new Response();
        this.contactRepository.save(contact);
        response.setStatusCode("200 Ok");
        response.setStatusMessage("Contact Saved SuccessFully !!");
        return ResponseEntity.status(HttpStatus.CREATED).
                header("isMsgSaved", "true").
                body(response);
    }

    @DeleteMapping("/deleteMessage")
    public ResponseEntity<Response> deleteMessage(RequestEntity<Contact> request){
        HttpHeaders headers = request.getHeaders();
        headers.forEach((key, value)->{
            log.info("Header is "+ key+ " "+ value.stream().collect(Collectors.joining("|")));
        });

        Contact contact = request.getBody();
        this.contactRepository.deleteById(contact.getContactId());
        Response response = new Response();
        response.setStatusCode("200");
        response.setStatusMessage("Message Deleted SuccessFully !!");
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @PatchMapping("/closeMsg")
    public ResponseEntity<Response> closeMsg(@RequestBody Contact contactReq){
        Optional<Contact> contact = this.contactRepository.findById(contactReq.getContactId());
        Response response = new Response();
        if(contact.isPresent()){
            contact.get().setStatus(AppConstants.CLOSE);
            this.contactRepository.save(contact.get());
            response.setStatusCode("200");
            response.setStatusMessage("Message Closed SuccessFully !!");
            return ResponseEntity.status(HttpStatus.OK).body(
                    response
            );
        }
        response.setStatusCode("400");
        response.setStatusMessage("Invalid Contact ID Received");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                response
        );
    }
}
