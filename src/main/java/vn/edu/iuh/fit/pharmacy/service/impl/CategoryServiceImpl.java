package vn.edu.iuh.fit.pharmacy.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.pharmacy.POJOs.Category;
import vn.edu.iuh.fit.pharmacy.mappers.CategoryMapper;
import vn.edu.iuh.fit.pharmacy.repositories.CategoryRepository;
import vn.edu.iuh.fit.pharmacy.service.CategoryService;
import vn.edu.iuh.fit.pharmacy.utils.response.CategoryResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private CategoryRepository categoryRepository;
    @Override
    public List<CategoryResponse> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();

        return categories.stream().map(categoryMapper::toDto).toList();
    }
}
