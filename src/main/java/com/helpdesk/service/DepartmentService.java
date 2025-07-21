package com.helpdesk.service;

import com.helpdesk.model.Department;
import com.helpdesk.Repository.DepartmentRepo;
import com.helpdesk.DTO.DepartmentDTO;
import com.helpdesk.Mapper.DepartmentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepo departmentRepo;
    private final DepartmentMapper departmentMapper;

    public DepartmentDTO createDepartment(DepartmentDTO dto) {
        Department department = departmentMapper.toEntity(dto);
        Department saved = departmentRepo.save(department);
        return departmentMapper.toDTO(saved);
    }

    public List<DepartmentDTO> getAllDepartments() {
        return departmentRepo.findAll().stream()
                .map(departmentMapper::toDTO)
                .collect(Collectors.toList());
    }

    public DepartmentDTO getDepartmentById(Long id) {
        Department dept = departmentRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found"));
        return departmentMapper.toDTO(dept);
    }

    public DepartmentDTO updateDepartment(DepartmentDTO dto) {
        Department existing = departmentRepo.findById(dto.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found"));

        existing.setDepartmentName(dto.getDepartmentName());

        Department updated = departmentRepo.save(existing);
        return departmentMapper.toDTO(updated);
    }

    public void deleteDepartment(Long id) {
        if (!departmentRepo.existsById(id)) {
            throw new RuntimeException("Department not found");
        }
        departmentRepo.deleteById(id);
    }


}
