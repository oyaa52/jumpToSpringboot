package com.mysite.sbb.question;

import com.mysite.sbb.answer.Answer;
import com.mysite.sbb.answer.AnswerForm;
import com.mysite.sbb.answer.AnswerService;
import com.mysite.sbb.category.Category;
import com.mysite.sbb.category.CategoryService;
import com.mysite.sbb.comment.Comment;
import com.mysite.sbb.comment.CommentForm;
import com.mysite.sbb.comment.CommentService;
import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RequestMapping("/question")
@RequiredArgsConstructor
@Controller
public class QuestionController {

    private final QuestionService questionService;
    private final UserService userService;
    private final AnswerService AnswerService;
    private final CommentService commentService;
    private final CategoryService categoryService;

    @GetMapping("/list")
    public String list(Model model,
                       @RequestParam(value = "page", defaultValue = "0") int page,
                       @RequestParam(value = "keyword", defaultValue = "") String keyword) {
        Page<Question> paging = this.questionService.getList(page, keyword);
        model.addAttribute("paging", paging);
        model.addAttribute("keyword", keyword);
        return "question_list";
    }

    @GetMapping(value = "/detail/{id}")
    public String detail(Model model, AnswerForm answerForm, CommentForm commentForm,
                         @PathVariable("id") Integer id,
                         @RequestParam(value = "page", defaultValue = "0") int page,
                         @RequestParam(value = "sort", defaultValue = "latest") String sortOption) {
        Question question = this.questionService.getQuestion(id);
        model.addAttribute("question", question);

        Page<Answer> paging = this.AnswerService.getAnswerList(page, question, sortOption);
        model.addAttribute("paging", paging);
        model.addAttribute("sortOption", sortOption);

        List<Comment> comments = new ArrayList<>();
        for (Answer answer : paging) {
            comments.addAll(this.commentService.getComments(answer));
        }
        model.addAttribute("comments", comments);

        return "question_detail";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String createQuestion(QuestionForm questionForm, Model model) {
        List<Category> categoriesList = this.categoryService.getCategories();
        model.addAttribute("categoriesList", categoriesList);
        model.addAttribute("questionForm", questionForm);
        return "question_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String createQuestion(Model model,
                                 @Valid QuestionForm questionForm,
                                 BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            List<Category> categoriesList = this.categoryService.getCategories();
            model.addAttribute("categoriesList", categoriesList);
            return "question_form";
        }
        SiteUser siteUser = this.userService.getUser(principal.getName());
        Category category = this.categoryService.findById(questionForm.getCategoryId());
        this.questionService.create(questionForm.getSubject(), questionForm.getContent(),
                                    siteUser, category);
        return "redirect:/question/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String modifyQuestion(QuestionForm questionForm, Model model,
                                 @PathVariable("id") Integer id, Principal principal) {
        Question question = this.questionService.getQuestion(id);
        if (!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        }

        if (question.getCategory() != null) {
            questionForm.setCategoryId(question.getCategory().getId());
        } else {
            questionForm.setCategoryId(null);
        }
        questionForm.setSubject(question.getSubject());
        questionForm.setContent(question.getContent());

        List<Category> categoriesList = this.categoryService.getCategories();
        model.addAttribute("categoriesList", categoriesList);

        return "question_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String modifyQuestion(@Valid QuestionForm questionForm, BindingResult bindingResult, Principal principal,
                                 @PathVariable("id") Integer id) {
        if (bindingResult.hasErrors()) {
            return "question_form";
        }
        Question question = this.questionService.getQuestion(id);
        if (!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        }
        Category category = this.categoryService.findById(questionForm.getCategoryId());
        this.questionService.modify(question, questionForm.getSubject(), questionForm.getContent(), category);
        return String.format("redirect:/question/detail/%s", id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id, Principal principal) {
        Question question = this.questionService.getQuestion(id);
        if (!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제 권한이 없습니다.");
        }
        this.questionService.delete(question);
        return "redirect:/";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public String vote(@PathVariable("id") Integer id, Principal principal) {
        Question question = this.questionService.getQuestion(id);
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.questionService.vote(question, siteUser);
        return String.format("redirect:/question/detail/%s", id);
    }

//    @PostMapping("/detail/{id}")
//    public String sortAnswers(Model model,
//                              @PathVariable("id") Integer id,
//                              @RequestParam String sortOption,
//                              @RequestParam(value = "page", defaultValue = "0") int page) {
//        Question question = this.questionService.getQuestion(id);
//        model.addAttribute("sortOption", sortOption);
//        Page<Answer> paging = this.AnswerService.getAnswerList(page, question.getId(), sortOption );
//        model.addAttribute("paging", paging);
//
//        return "question_detail";
//    }
}
