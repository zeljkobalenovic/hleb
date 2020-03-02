package baki.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import baki.api.model.Role;
import baki.api.model.RoleName;

/**
 * RoleRepository
 */
public interface RoleRepository extends JpaRepository<Role,Long> {

	Optional<Role> findByName(RoleName admin);

	

    
}