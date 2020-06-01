package baki.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import baki.api.model.Customer;
import baki.api.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {

	List<Order> findByCustomer(Customer customer);

	

}