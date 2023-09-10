package com.jwt.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jwt.entities.User;

/**
 * This interface defines a repository for managing User entities.
 * It extends JpaRepository for basic CRUD operations on User entities.
 */
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * Find a User entity by its userName.
     *
     * @param username The userName to search for.
     * @return The User entity with the specified username.
     */
    public User getUserByUsername(String username);
}
