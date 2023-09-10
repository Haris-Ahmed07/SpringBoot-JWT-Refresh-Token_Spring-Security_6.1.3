package com.jwt.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.jwt.entities.User;
import com.jwt.services.UserService;

@RestController
public class HomeController {

    @Autowired
    private UserService userService;

    // EndPoint for adding a single user
    @PostMapping("/add-user")
    public ResponseEntity<?> addUser(@RequestBody User user) {
        if (user != null) {
            userService.saveUser(user);
            return ResponseEntity.ok("User Has Been Saved");
        } else {
            return ResponseEntity.ok("Something Went Wrong!!");
        }
    }

    // EndPoint for adding multiple users
    @PostMapping("/add-users")
    public ResponseEntity<?> addUser(@RequestBody List<User> users) {
        if (users != null) {
            userService.saveUsers(users);
            return ResponseEntity.ok("Users Have Been Saved");
        } else {
            return ResponseEntity.ok("Something Went Wrong!!");
        }
    }

    // EndPoint for retrieving all users
    @GetMapping("/getall")
    public List<User> getAll() {
        return userService.getAllUsers();
    }

    // EndPoint for admin access
    @GetMapping("/admin")
    public ResponseEntity<?> admin() {
        return ResponseEntity.ok("You Are An Admin");
    }

    // EndPoint for user access
    @GetMapping("/user")
    public ResponseEntity<?> user() {
        return ResponseEntity.ok("You Are A User");
    }

}
