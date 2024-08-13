package com.example.easyrent.service.Impl;

import com.example.easyrent.entity.Category;
import com.example.easyrent.repository.CategoryRepository;
import com.example.easyrent.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
}
