package baki.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import baki.api.model.User;

/**
 * UserRepository
 */
@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
	Optional<User> findByUsername(String username);
	

    
}