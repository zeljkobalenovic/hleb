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

import baki.api.dto.domaindto.CustomerDto;
import baki.api.service.domainservice.CustomerService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
@Validated
public class CustomerController {

    private final CustomerService customerService;

    // CUSTOMER
    // Security : dozvoljeno zaposlenima u zavisnosti od read/write prava , i roleuser samo uslovni read
    // Uslov dodati tako da svi read metodi ako je roleuser prikazuju SAMO customera/e za koje je doticni registrovan

    @PreAuthorize("hasAuthority('CUSTOMER_WRITE')")
    @PostMapping
    public ResponseEntity<?> createCustomer(@Valid @RequestBody CustomerDto customerDto) {
        return customerService.createCustomer(customerDto);
    }

    @PreAuthorize("hasAuthority('CUSTOMER_WRITE')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCustomer(@Valid @RequestBody CustomerDto customerDto,
            @PathVariable("id") @Positive Long id) {
        return customerService.updateCustomer(customerDto, id);
    }

    @PreAuthorize("hasAuthority('CUSTOMER_WRITE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable @Positive Long id) {
        return customerService.deleteCustomer(id);
    }

    @PreAuthorize("hasAuthority('CUSTOMER_READ')")
    @GetMapping
    public ResponseEntity<?> getAllCustomer() {
        return customerService.getAllCustomer();
    }

    @PreAuthorize("hasAuthority('CUSTOMER_READ')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getCustomer(@PathVariable @Positive Long id) {
        return customerService.getCustomer(id);
    }    
}