package com.api.requesttracker.serviceimpl;

import com.api.requesttracker.dto.UserDTO;
import com.api.requesttracker.entity.ERole;
import com.api.requesttracker.entity.Role;
import com.api.requesttracker.entity.User;
import com.api.requesttracker.repository.RoleRepository;
import com.api.requesttracker.repository.UserRepository;
import com.api.requesttracker.services.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    public final UserRepository userRepository;
    public final RoleRepository roleRepository;
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }
    @Override
    public List<UserDTO> getAllUsers(){
        List<User> user = userRepository.findAll();
        List<UserDTO> response = new ArrayList<>();
        for(int i = 0 ; i< user.size();i++){
            UserDTO userDto=new UserDTO();
            userDto.setId(Math.toIntExact(user.get(i).getId()));
            userDto.setUserName(user.get(i).getUsername());
            userDto.setEmail(user.get(i).getEmail());

            // Extract and format roles
            Set<Role> roles = user.get(i).getRoles();
            List<String> roleNames = roles.stream()
                    .map(role -> role.getName().name()) // Assuming getName() returns an enum, and name() is used to get its name
                    .collect(Collectors.toList());

            userDto.setRole(String.join(",", roleNames));
            response.add(userDto);
        }
        return response;
    }
    @Override
    public User updateUser(User user){
        return userRepository.save(user);
    }

    @Override
    public String updateDetails(Long userId,String userName, String email, String roleName) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            // Update email if provided
            if (email != null && !email.isEmpty()) {
                user.setEmail(email);
            }
           // Update role if provided
            if (roleName != null && !roleName.isEmpty()) {
                Role role = roleRepository.findByName(ERole.valueOf(roleName.toUpperCase()));
                user.getRoles().clear();
                user.getRoles().add(role);
            }
           // Save the updated user
            userRepository.save(user);
            return "User details updated successfully.";
        }
        return "User not found.";
    }

}
