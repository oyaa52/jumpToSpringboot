package com.mysite.sbb;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // int 대신 Integer을 쓰는 이유: nullable 값을 처리해야 해서

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createTime; // column annotation은 column의 성격을 특정할 때 사용되며, 보통은 annotation 없이도 column으로 인식함

    @ManyToOne
    private Question question; // 질문 엔티티 참조하기 위해서
}
