package baki.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import baki.api.model.User;

/**
 * UserRepository
 */
public interface UserRepository extends JpaRepository<User,Long> {

    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
	Optional<User> findByUsername(String username);
	

    
}