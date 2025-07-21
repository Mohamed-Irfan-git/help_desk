package com.helpdesk.service;

import com.helpdesk.model.Category;
import com.helpdesk.Repository.CategoryRepo;
import com.helpdesk.DTO.CategoryDTO;
import com.helpdesk.Mapper.CategoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepo categoryRepo;
    private final CategoryMapper categoryMapper;

    public CategoryDTO createCategory(CategoryDTO dto) {
        Category category = categoryMapper.toEntity(dto);
        Category saved = categoryRepo.save(category);
        return categoryMapper.toDTO(saved);
    }

    public List<CategoryDTO> findAllCategories() {
        return categoryRepo.findAll().stream()
                .map(categoryMapper::toDTO)
                .collect(Collectors.toList());
    }

    public CategoryDTO findCategoryById(Long id) {
        Category category = categoryRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        return categoryMapper.toDTO(category);
    }

    public CategoryDTO updateCategory(CategoryDTO dto) {
        Category category = categoryRepo.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        category.setCategoryName(dto.getCategoryName());
        Category updated = categoryRepo.save(category);

        return categoryMapper.toDTO(updated);
    }

    public void deleteCategory(Long id) {
        categoryRepo.deleteById(id);
    }


}
