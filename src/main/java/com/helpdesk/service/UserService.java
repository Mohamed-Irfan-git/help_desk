package com.helpdesk.service;



import com.helpdesk.model.Department;
import com.helpdesk.model.User;
import com.helpdesk.repository.DepartmentRepo;
import com.helpdesk.repository.UserRepo;
import com.helpdesk.dto.UserDTO;
import com.helpdesk.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepo;
    private final DepartmentRepo departmentRepo;
    private final UserMapper userMapper;

    public UserDTO createUser(UserDTO userDTO) {
        User user = userMapper.toEntity(userDTO);


        if (userDTO.getDepartmentId() != null) {
            Department department = departmentRepo.findById(userDTO.getDepartmentId())
                    .orElseThrow(() -> new RuntimeException("Department ID not found"));
            user.setDepartment(department);
        }

        User saved = userRepo.save(user);
        return userMapper.toDTO(saved);
    }

    public List<UserDTO> findAllUsers() {
        return userRepo.findAll().stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    public UserDTO findUserById(Long id) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return userMapper.toDTO(user);
    }

    public List<UserDTO> findUserByFirstname(String name) {
        return userRepo.findByFirstNameContainingIgnoreCase(name)
                .stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    public UserDTO updateUser(UserDTO userDTO) {
        User existingUser = userRepo.findById(userDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Update fields
        existingUser.setFirstName(userDTO.getFirstName());
        existingUser.setLastName(userDTO.getLastName());
        existingUser.setEmail(userDTO.getEmail());
        existingUser.setPassword(userDTO.getPassword());
        existingUser.setGender(userDTO.getGender());


        if (userDTO.getDepartmentId() != null) {
            Department department = departmentRepo.findById(userDTO.getDepartmentId())
                    .orElseThrow(() -> new RuntimeException("Department ID not found"));
            existingUser.setDepartment(department);
        }

        User updated = userRepo.save(existingUser);
        return userMapper.toDTO(updated);
    }

    public void deleteUser(Long id) {
        if (!userRepo.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        userRepo.deleteById(id);
    }
}
