package com.mysite.sbb;

import com.mysite.sbb.answer.Answer;
import com.mysite.sbb.answer.AnswerRepository;
import com.mysite.sbb.question.Question;
import com.mysite.sbb.question.QuestionRepository;
import com.mysite.sbb.question.QuestionService;
import com.mysite.sbb.user.SiteUser;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class JumpToSpringbootApplicationTests {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private AnswerRepository answerRepository;

    @Test
    void testInsertQuestion() {
        Question q = new Question();
        q.setContent("sbb가 정확히 뭔가요?");
        q.setCreateDate(LocalDateTime.now());
        q.setSubject("sbb 관련 질문!");
        this.questionRepository.save(q);
    }

    @Test
    void testFindByIdInQuestion() {
        Optional<Question> opQuestion = this.questionRepository.findById(5);
        if (opQuestion.isPresent()) {
            Question q = opQuestion.get();
            assertEquals("sbb가 무엇인가요?", q.getSubject());
        }
    }

    @Test
    void testFindBySubjectInQuestion() {
        Question q = this.questionRepository.findBySubject("sbb가 무엇인가요?");
        assertEquals(5, q.getId());
    }

    @Test
    void testFindBySubjectAndContentInQuestion() {
        Question q = this.questionRepository.findBySubjectAndContent(
                "sbb가 무엇인가요?", "sbb에 대해서 알고 싶습니다."
        );
        assertEquals(5, q.getId());
    }

    @Test
    void testFindBySubjectLikeInQuestion() {
        List<Question> qList = this.questionRepository.findBySubjectLike("%sbb%");
//        Question q = qList.get(0);
//        assertEquals("sbb가 무엇인가요?", q.getSubject());
        assertThat(qList).extracting(Question::getSubject)
                         .contains("sbb가 무엇인가요?", "sbb 관련 질문!");
    }

    @Test
    void testModifySubjectInQuestion() {
        Optional<Question> opQuestion = this.questionRepository.findById(5);
        if (opQuestion.isPresent()) {
            Question q = opQuestion.get();
            q.setSubject("수정된 제목입니다.");
            this.questionRepository.save(q);
        }
    }

    @Test
    void testDeleteByIdInQuestion() {
        Optional<Question> opQuestion = this.questionRepository.findById(5);
        if (opQuestion.isPresent()) {
            Question q = opQuestion.get();
            this.questionRepository.delete(q);
            assertEquals(2, this.questionRepository.count());
        }
    }

    @Test
    void testInsertAnswer() {
        Answer a = new Answer();
        Optional<Question> opQuestion = this.questionRepository.findById(6);
        if (opQuestion.isPresent()) {
            Question q = opQuestion.get();
            a.setContent("네 자동으로 생성됩니다.");
            a.setQuestion(q);
            a.setCreateDate(LocalDateTime.now());
            this.answerRepository.save(a);
        }
    }

    @Test
    void testFindByIdInAnswer() {
        Optional<Answer> opAnswer = this.answerRepository.findById(1);
        assertTrue(opAnswer.isPresent());
        Answer a = opAnswer.get();
        assertEquals(6, a.getQuestion().getId());
    }

    @Transactional
    @Test
    void testFindByIdInQuestionAndCompareWithAnswerList() {
        Optional<Question> opQuestion = this.questionRepository.findById(6);
        assertTrue(opQuestion.isPresent());
        Question q = opQuestion.get();
        List<Answer> answersList = q.getAnswersList();
        assertEquals("네 자동으로 생성됩니다.", answersList.getFirst().getContent());
    }

//    @Test
//    void testInsertDummyContents() {
//        for (int i = 0; i < 100; i++) {
//            String subject = String.format("Test " + i);
//            String content = "No Content";
//            SiteUser user = "user1";
//            this.questionService.create(subject, content, user);
//        }
//    }
}
