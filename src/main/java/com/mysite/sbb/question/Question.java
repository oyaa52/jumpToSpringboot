package com.mysite.sbb.question;

import java.time.LocalDateTime;
import java.util.List;

import com.mysite.sbb.answer.Answer;
import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Question {
    @Id // 기본키 설정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 1씩 자동 증가
    private Integer id;

    @Column(length = 200) // length: 열의 길이
    private String subject;

    @Column(columnDefinition = "TEXT") // columnDefinition: 열 데이터의 성격 설정
    private String content;

    private LocalDateTime createDate;

    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
    private List<Answer> answersList;
}
