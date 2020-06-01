package baki.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import baki.api.model.OrderItem;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem,Long> {

}