package com.jwt.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.jwt.entities.User;
import com.jwt.repositories.UserRepository;

/**
 * This service class is responsible for managing user-related operations, including user creation,
 * retrieval, updating, deletion, and password encoding.
 */
@Service
public class UserService {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    /**
     * Delete a user by username.
     *
     * @param username The username of the user to delete.
     */
    public void deleteUser(String username) {
        userRepository.delete(getUserByUsername(username));
    }

    /**
     * Get a user by username.
     *
     * @param username The username of the user to retrieve.
     * @return The user with the specified username.
     */
    public User getUserByUsername(String username) {
        return userRepository.getUserByUsername(username);
    }

    /**
     * Get a list of all users.
     *
     * @return A list of all user entities.
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Update a user's information.
     *
     * @param updatedUser The updated user object.
     * @return The updated user entity.
     */
    public User updateUser(User updatedUser) {
        return userRepository.save(updatedUser);
    }

    /**
     * Save a user entity with password encoding.
     *
     * @param user The user entity to save.
     * @return The saved user entity.
     */
    public User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    /**
     * Save a list of user entities with password encoding.
     *
     * @param users The list of user entities to save.
     * @return The saved list of user entities.
     */
    public List<User> saveUsers(List<User> users) {
        for (User user : users) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return userRepository.saveAll(users);
    }
}
