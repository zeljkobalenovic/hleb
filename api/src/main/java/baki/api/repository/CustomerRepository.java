package baki.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import baki.api.dto.domaindto.CustomerDto;
import baki.api.model.Customer;
import baki.api.model.CustomerGroup;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Long> {
	  
	Boolean existsByName(String name);	 
	Boolean existsByCode(String code);
	List<Customer> findAllByCustomerGroup(CustomerGroup customerGroup);
	List<CustomerDto> findAllBy();	
    
} 