package baki.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import baki.api.dto.CgView1;
import baki.api.model.CustomerGroup;

@Repository
public interface CustomerGroupRepository extends JpaRepository<CustomerGroup, Long> {

    @Query(value = "SELECT * FROM customers_group WHERE deleted=true AND id= ?", nativeQuery = true)
    Optional<CustomerGroup> findByIdDeleted(Long id);

    @Query(value = "SELECT * FROM customers_group", nativeQuery = true)
    List<CustomerGroup> findAllWithDeleted();

    @Query(value = "SELECT id,name,version,deleted,last_modified_by,last_modified_date FROM customers_group_history WHERE id= ?", nativeQuery = true)
    List<CgView1> findByIdHistory(Long id);

   

}