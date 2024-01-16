package com.api.requesttracker.controllers;

import com.api.requesttracker.dto.UserDTO;
import com.api.requesttracker.entity.User;
import com.api.requesttracker.repository.UserRepository;
import com.api.requesttracker.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {
    public final UserRepository userRepository;
    public final UserService userService;
    public UserController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }
    /*This is a Get Method to Get all the available users*/
    @GetMapping("/getAllUsers")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public List<UserDTO> getAllUsers() {
        List<UserDTO> result=userService.getAllUsers();
        return ResponseEntity.ok().body(result).getBody();
    }
    @PutMapping("/updateUser")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public String updateUser(@RequestParam("userId") Long userId , @RequestParam("userName")String userName,@RequestParam("email") String email,@RequestParam("role") String role) {
        String result=userService.updateDetails(userId,userName,email,role);
        return ResponseEntity.ok().body(result).getBody();
    }
}
