package com.mysite.sbb.category;

import com.mysite.sbb.question.Question;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 20)
    private String categoryName;

    @OneToMany(mappedBy="category", cascade = CascadeType.REMOVE)
    private List<Question> questionsList;
}
