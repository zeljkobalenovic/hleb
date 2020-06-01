package baki.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import baki.api.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

}