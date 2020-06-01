package baki.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import baki.api.model.ProductGroup;

@Repository
public interface ProductGroupRepository extends JpaRepository<ProductGroup,Long> {

}