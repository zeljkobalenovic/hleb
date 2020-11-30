package baki.api.service.domainservice;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import baki.api.dto.domaindto.CustomerGroupDto;
import baki.api.exception.ApiError;
import baki.api.model.Customer;
import baki.api.model.CustomerGroup;
import baki.api.repository.CustomerGroupRepository;
import baki.api.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerGroupService {


    private final CustomerGroupRepository customerGroupRepository;
    private final CustomerRepository customerRepository;

    // CUSTOMER GROUP 
	// create, update, findbyid 	-> 	vracaju dto projekciju domena (sa status 200+) ili opis greske (sa status 400+)
	// delete						->  vraca status200+ ili opis greske ?(sa status 400+)
	// find all						->	vraca list projekciju domena (sa status 200+), lista moze biti empty
	// customergroup domen ima istu dto i list projekciju , kao i projekciju za constraint db check (u ostalim domenima NE)

	public ResponseEntity<?> createCustomerGroup(CustomerGroupDto customerGroupDto) {
	
		if ( customerGroupRepository.existsByName(customerGroupDto.getName())) {
			String message = "Grupa sa imenom : " + customerGroupDto.getName() + " vec postoji.";
			ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, message);
			return new ResponseEntity<>(apiError, apiError.getStatus());
		}
		CustomerGroup customerGroup = new CustomerGroup();
		BeanUtils.copyProperties(customerGroupDto, customerGroup);
		CustomerGroup savedCustomerGroup = customerGroupRepository.save(customerGroup);	
		customerGroupDto.setId(savedCustomerGroup.getId());	
		return new ResponseEntity<>(customerGroupDto , HttpStatus.CREATED);
	}

	public ResponseEntity<?> updateCustomerGroup(CustomerGroupDto customerGroupDto, Long id) {

		Optional<CustomerGroup> customerGroupOptional = customerGroupRepository.findById(id);
		
		if (customerGroupOptional.isPresent()) {
			// dalje sve isto kao create
			if (customerGroupRepository.existsByName(customerGroupDto.getName())) {
				String message = "Grupa sa imenom : " + customerGroupDto.getName() + " vec postoji.";
				ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, message);
				return new ResponseEntity<>(apiError, apiError.getStatus());
			}
			CustomerGroup customerGroup = customerGroupOptional.get();
			BeanUtils.copyProperties(customerGroupDto, customerGroup);
			customerGroupRepository.save(customerGroup);
			return new ResponseEntity<>(customerGroupDto , HttpStatus.OK);
		}
		// ako ga nenadje ( trazen je update nepostojeceg zapisa )
		else {
				String message = "Trazena grupa : " + id + " nije pronadjena.";
				ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, message);
				return new ResponseEntity<>(apiError, apiError.getStatus());
		}		
	}

	public ResponseEntity<?> deleteCustomerGroup(Long id) {

		Optional<CustomerGroup> customerGroupOptional = customerGroupRepository.findById(id);
				
		if (customerGroupOptional.isPresent()) {
			// ide uslovno brisanje - uslov je dali u toj grupi ima customera 
			List<Customer> customerList = customerRepository.findAllByCustomerGroup(customerGroupOptional.get());
			// ako nije nasao nijednog obrisi (!!! soft delete -> samo update deleted=id)
			if (customerList.isEmpty()) {
				CustomerGroup customerGroup = customerGroupOptional.get();
				customerGroup.setDeleted(customerGroup.getId());
				customerGroupRepository.save(customerGroup);
				return new ResponseEntity<>(HttpStatus.OK);
			}
			// ako ih ima ( bar jedan )
			else {
				String message = "Brisanje nije dozvoljeno. Postoje Customeri u ovoj grupi.";
				ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, message);
				return new ResponseEntity<>(apiError, apiError.getStatus());
			}
		} 
		// ako ga nenadje ( trazeno je brisanje nepostojeceg zapisa )
		else {
			String message = "Trazena grupa : " + id + " nije pronadjena.";
			ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, message);
			return new ResponseEntity<>(apiError, apiError.getStatus());
		}
	}

	public ResponseEntity<?> getAllCustomerGroup() {
		return new ResponseEntity<>(customerGroupRepository.findAllBy(), HttpStatus.OK);
		
	}

	public ResponseEntity<?> queryCustomerGroup(String searchstring) {
		return new ResponseEntity<>(customerGroupRepository.findByNameContaining(searchstring), HttpStatus.OK);
	}

	public ResponseEntity<?> getCustomerGroup(Long id) {		
	//	Optional<CustomerGroup> customerGroupOptional = customerGroupRepository.findById(id);
		Optional<CustomerGroupDto> customerGroupOptional = customerGroupRepository.findOneById(id);
		
		if(customerGroupOptional.isPresent()) {
		//	CustomerGroupDto customerGroupDto = new CustomerGroupDto(null,null);
		//	BeanUtils.copyProperties(customerGroupOptional.get(), customerGroupDto);
			return new ResponseEntity<>(customerGroupOptional.get(), HttpStatus.OK);
		} else {
			String message = "Trazena grupa : " + id + " nije pronadjena.";
			ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, message);
			return new ResponseEntity<>(apiError, apiError.getStatus());
		}	
		
	}

	

}