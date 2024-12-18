package com.mysite.sbb.question;

import com.mysite.sbb.category.Category;
import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionForm {

//    @NotEmpty(message = "카테고리를 선택해주세요.")
    @NotNull
    private Integer categoryId;

    @NotEmpty(message = "제목을 입력해주세요.")
    @Size(max = 200)
    private String subject;

    @NotEmpty(message = "내용을 입력해주세요.")
    private String content;
}
