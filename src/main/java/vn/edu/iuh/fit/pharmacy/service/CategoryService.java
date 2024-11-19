package vn.edu.iuh.fit.pharmacy.service;

import vn.edu.iuh.fit.pharmacy.utils.response.CategoryResponse;

import java.util.List;

public interface CategoryService {
    public List<CategoryResponse> getAllCategories();
}
