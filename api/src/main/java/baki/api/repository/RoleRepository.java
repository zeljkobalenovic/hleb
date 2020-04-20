package baki.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import baki.api.model.Role;
import baki.api.model.RoleName;

/**
 * RoleRepository
 */
@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {

	Optional<Role> findByName(RoleName admin);

	

    
}