package baki.api.service.domainservice;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import baki.api.dto.domaindto.CustomerDto;
import baki.api.exception.ApiError;
import baki.api.model.Customer;
import baki.api.model.CustomerGroup;
import baki.api.model.Order;
import baki.api.repository.CustomerGroupRepository;
import baki.api.repository.CustomerRepository;
import baki.api.repository.OrderRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerService {

	private final CustomerGroupRepository customerGroupRepository;
	private final CustomerRepository customerRepository;
	private final OrderRepository orderRepository;

	// CUSTOMER
	// create, update, findbyid 	-> 	vracaju dto projekciju domena (sa status 200+) ili opis greske (sa status 400+)
	// delete						->  vraca status200+ ili opis greske (sa status 400+)
	// find all						->	vraca list projekciju domena (sa status 200+), lista moze biti empty
	

	public ResponseEntity<?> createCustomer(CustomerDto customerDto) {
		// customerInputDTO proveren logicki (ima svih 5 polja validnih)
		// check unique constraint ( name + code )
		if (customerRepository.existsByName(customerDto.getName())) {
			String message = "Customer sa imenom : " + customerDto.getName() + " vec postoji.";
			ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, message);
			return new ResponseEntity<>(apiError, apiError.getStatus());
		}
		if (customerRepository.existsByCode(customerDto.getCode())) {
			String message = "Customer sa kodom : " + customerDto.getCode() + " vec postoji.";
			ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, message);
			return new ResponseEntity<>(apiError, apiError.getStatus());
		}
		// check customergroup
		Optional<CustomerGroup> customerGroupOpt = customerGroupRepository.findById(customerDto.getCustomerGroupId());
		if (!customerGroupOpt.isPresent()) {
			String message = "CustomerGroup : " + customerDto.getCustomerGroupId() + " ne postoji.";
			ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, message);
			return new ResponseEntity<>(apiError, apiError.getStatus());
		}
		// ako su sva tri uslova zadovoljena moze upis 
		Customer customer = new Customer();
		BeanUtils.copyProperties(customerDto, customer);		
		customer.setCustomerGroup(customerGroupOpt.get());
		customerRepository.save(customer);
		return new ResponseEntity<>(customerDto, HttpStatus.CREATED);
	}

	public ResponseEntity<?> updateCustomer(CustomerDto customerDto, Long id) {

		Optional<Customer> customerOpt = customerRepository.findById(id); 
			
			if(customerOpt.isPresent()) {
				// dalje slicno kao create				
				if (customerRepository.existsByName(customerDto.getName())) {
					String message = "Customer sa imenom : " + customerDto.getName() + " vec postoji.";
					ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, message);
					return new ResponseEntity<>(apiError, apiError.getStatus());
				}
				if (customerRepository.existsByCode(customerDto.getCode())) {
					String message = "Customer sa kodom : " + customerDto.getCode() + " vec postoji.";
					ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, message);
					return new ResponseEntity<>(apiError, apiError.getStatus());
				}		
				Optional<CustomerGroup> customerGroupOpt = customerGroupRepository.findById(customerDto.getCustomerGroupId());
				if (!customerGroupOpt.isPresent()) {
					String message = "CustomerGroup : " + customerDto.getCustomerGroupId() + " ne postoji.";
					ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, message);
					return new ResponseEntity<>(apiError, apiError.getStatus());
				}		
				Customer customer = customerOpt.get();
				BeanUtils.copyProperties(customerDto, customer);
				customer.setCustomerGroup(customerGroupOpt.get());
				customerRepository.save(customer);
				return new ResponseEntity<>(customerDto, HttpStatus.OK);
				}
			// ako ga nenadje ( trazen je update nepostojeceg zapisa )
			else {
				String message = "Trazeni customer : " + id + " nije pronadjen.";
				ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, message);
				return new ResponseEntity<>(apiError, apiError.getStatus());
			}		
	}

	public ResponseEntity<?> deleteCustomer(Long id) {

		Optional<Customer> customerOptional = customerRepository.findById(id);
		// ako ga nadje		
		if (customerOptional.isPresent()) {
			// ide uslovno brisanje - uslov je dali trazeni customer ima ordera
			List<Order> orderList = orderRepository.findByCustomer(customerOptional.get());
			// ako nije nasao nijednog obrisi (!!! soft delete -> samo update deleted=id)
			if (orderList.isEmpty()) {
				Customer customer = customerOptional.get();
				customer.setDeleted(customer.getId());
				// takodje ukloni SVE registracije (useri registruju rad sa customerima ) za tog customera				
				customer.getUsers().clear();
				customerRepository.save(customer);
				return new ResponseEntity<>(HttpStatus.OK);
			}
			// ako ih ima ( bar jedan )
			else {
				String message = "Brisanje nije dozvoljeno. Postoje Orderi za ovog customera.";
				ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, message);
				return new ResponseEntity<>(apiError, apiError.getStatus());
			}
		}
		// ako ga nenadje ( trazeno je brisanje nepostojeceg zapisa )
		else {
			String message = "Trazena customer : " + id + " nije pronadjen.";
			ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, message);
			return new ResponseEntity<>(apiError, apiError.getStatus());
		}	
		
	}

	// ovo za probu frontenda 
	public ResponseEntity<?> getAllCustomer() {		
		return new ResponseEntity<>(customerRepository.findAllBy() , HttpStatus.OK);
	}

	public ResponseEntity<?> getCustomer(Long id) {
		return null;
	}

	
}