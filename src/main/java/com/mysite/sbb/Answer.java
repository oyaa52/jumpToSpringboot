package com.mysite.sbb;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // int 대신 Integer을 쓰는 이유: nullable 값을 처리해야 해서

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createTime; // column annotation은 column의 성격을 특정할 때 사용되며, 보통은 annotation 없이도 column으로 인식함

    private Question question;
}
