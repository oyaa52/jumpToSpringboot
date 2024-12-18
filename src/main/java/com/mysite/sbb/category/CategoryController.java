package com.mysite.sbb.category;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class CategoryController {

    private final CategoryService categoryService;

    @PostConstruct
    public void init() {
        createCategory();
    }

    public void createCategory() {
        Category qnaCategory = new Category();
        Category courseCategory = new Category();
        Category freeBoardCategory = new Category();
        this.categoryService.create(qnaCategory, "질문답변");
        this.categoryService.create(courseCategory, "강좌");
        this.categoryService.create(freeBoardCategory, "자유게시판");
    }
}
