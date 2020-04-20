package baki.api.controller;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import baki.api.model.CustomerGroup;
import baki.api.repository.CustomerGroupRepository;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CustomerGroupController {

    private final CustomerGroupRepository customerGroupRepository;
     
    // create
    @PreAuthorize("hasAuthority('CUSTOMER_WRITE')")    
    @PostMapping("/customergroup")   
    public ResponseEntity<?> createcg(@RequestBody  CustomerGroup cg) { 
        return new ResponseEntity<>(customerGroupRepository.save(cg), HttpStatus.CREATED);           
    } 

    // update by id
    @PreAuthorize("hasAuthority('CUSTOMER_WRITE')")
    @PutMapping("/customergroup/{id}")
    public ResponseEntity<?> updatecg(@RequestBody CustomerGroup cgupd, @PathVariable Long id) {
            Optional<CustomerGroup> cgopt = customerGroupRepository.findById(id);                 
            CustomerGroup cg = cgopt.get();
            cg.setName(cgupd.getName());
            return new ResponseEntity<>(customerGroupRepository.save(cg), HttpStatus.OK);  
    }

    // delete by id
    @PreAuthorize("hasAuthority('CUSTOMER_WRITE')")
    @DeleteMapping("/customergroup/{id}")
    public ResponseEntity<?> deletecg(@PathVariable Long id) {
        Optional<CustomerGroup> cgopt = customerGroupRepository.findById(id);
        CustomerGroup cg = cgopt.get();
        cg.setDeleted(true);
        return new ResponseEntity<>(customerGroupRepository.save(cg), HttpStatus.OK);
    }

    // undelete by id
    @PreAuthorize("hasAuthority('CUSTOMER_WRITE')")
    @PutMapping("/customergroup/undelete/{id}")
    public ResponseEntity<?> undeletecg(@PathVariable Long id){
        Optional<CustomerGroup> cgopt = customerGroupRepository.findByIdDeleted(id);
        CustomerGroup cg = cgopt.get();
        cg.setDeleted(false);
        return new ResponseEntity<>(customerGroupRepository.save(cg) ,HttpStatus.OK);
    }


    // find all
    @PreAuthorize("hasAuthority('CUSTOMER_READ')")
    @GetMapping("/customergroup")
    public ResponseEntity<?> listAll() {
        return new ResponseEntity<>(customerGroupRepository.findAll(), HttpStatus.OK);
    }

    // find by id
    @PreAuthorize("hasAuthority('CUSTOMER_READ')")
    @GetMapping("/customergroup/{id}")
    public ResponseEntity<?> listById(@PathVariable Long id) {
        return new ResponseEntity<>(customerGroupRepository.findById(id), HttpStatus.OK);
    }

    // find all - sa obrisanima
    @PreAuthorize("hasAuthority('CUSTOMER_READ')")
    @GetMapping("/customergroupwithdeleted")
    public ResponseEntity<?> listAllWithDeleted() {
        return new ResponseEntity<>(customerGroupRepository.findAllWithDeleted(), HttpStatus.OK);
    }

    // find history by id
    @PreAuthorize("hasAuthority('CUSTOMER_READ')")
    @GetMapping("/customergrouphistory/{id}")
    public ResponseEntity<?> listAllcghistory(@PathVariable Long id) {
        return new ResponseEntity<>(customerGroupRepository.findByIdHistory(id), HttpStatus.OK);
    } 

}