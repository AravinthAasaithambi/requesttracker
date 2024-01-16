package com.api.requesttracker.services;

import com.api.requesttracker.dto.UserDTO;
import com.api.requesttracker.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {


    List<UserDTO> getAllUsers();

    User updateUser(User user);

    String updateDetails(Long userId,String userName, String email, String roleName);
}
