package baki.api.controller.domaincontroller;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import baki.api.dto.domaindto.CustomerGroupDto;
import baki.api.service.domainservice.CustomerGroupService;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/customergroup")
@RequiredArgsConstructor
@Validated
public class CustomerGroupController {

    private final CustomerGroupService customerGroupService;

    // CUSTOMER GROUP
    // security : dozvoljeno samo zaposlenima u zavisnosti od read/write prava (role user NEMA NIKAKAV PRISTUP customergroup )

    //@PreAuthorize("hasAuthority('CUSTOMER_WRITE')")
    @PostMapping
    public ResponseEntity<?> createCustomerGroup(@Valid @RequestBody CustomerGroupDto customerGroupDto) {
        return customerGroupService.createCustomerGroup(customerGroupDto);
    }

    
    @PreAuthorize("hasAuthority('CUSTOMER_WRITE')")
    @PutMapping("{id}")
    public ResponseEntity<?> updateCustomerGroup(@Valid @RequestBody CustomerGroupDto customerGroupDto,
                                                 @PathVariable @Positive Long id) {                              
        return customerGroupService.updateCustomerGroup(customerGroupDto,id);
    }

    @PreAuthorize("hasAuthority('CUSTOMER_WRITE')")
    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteCustomerGroup(@PathVariable @Positive Long id) {
        return customerGroupService.deleteCustomerGroup(id);
    }

    // posto roleuser ima permission customer-read (moramo dodatni uslov da zabranimo roleuser ).
    // @PreAuthorize("hasAuthority('CUSTOMER_READ') and !hasRole('ROLE_USER')")
    @GetMapping
    public ResponseEntity<?> getAllCustomerGroup() {
        return customerGroupService.getAllCustomerGroup();
    }

    // posto roleuser ima permission customer-read (moramo dodatni uslov da zabranimo roleuser ).
    @PreAuthorize("hasAuthority('CUSTOMER_READ') and !hasRole('ROLE_USER')")
    @GetMapping("{id}")
    public ResponseEntity<?> getCustomerGroup(@PathVariable @Positive Long id) {
        return customerGroupService.getCustomerGroup(id);
    }

}