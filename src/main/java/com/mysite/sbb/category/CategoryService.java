package com.mysite.sbb.category;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public void create(Category category, String categoryName) {
        if (categoryRepository.count() == 0) {
            category.setCategoryName(categoryName);
            this.categoryRepository.save(category);
        }
    }

    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }

    public Category findById(Integer id) {
        return categoryRepository.findById(id).orElse(null);
    }
}
