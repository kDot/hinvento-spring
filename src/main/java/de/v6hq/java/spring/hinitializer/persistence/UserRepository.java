package de.v6hq.java.spring.hinitializer.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    // You can add custom query methods here if needed, e.g.,
    // User findByUsername(String username); 
}