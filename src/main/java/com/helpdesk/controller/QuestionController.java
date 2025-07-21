package com.helpdesk.controller;

import com.helpdesk.DTO.QuestionDTO;
import com.helpdesk.service.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;


    @PostMapping
    public ResponseEntity<QuestionDTO> createQuestion(@Valid @RequestBody QuestionDTO questionDTO) {
        QuestionDTO createdQuestion = questionService.createQuestion(questionDTO);
        return new ResponseEntity<>(createdQuestion, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<QuestionDTO>> getAllQuestions() {
        List<QuestionDTO> questions = questionService.getAllQuestions();
        return ResponseEntity.ok(questions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuestionDTO> getQuestionById(@PathVariable Long id) {
        QuestionDTO question = questionService.findById(id);
        return ResponseEntity.ok(question);
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuestionDTO> updateQuestion(@PathVariable Long id,
                                                      @Valid @RequestBody QuestionDTO questionDTO) {
        questionDTO.setQuestionId(id);
        QuestionDTO updatedQuestion = questionService.updateQuestion(questionDTO);
        return ResponseEntity.ok(updatedQuestion);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long id) {
        questionService.deleteQuestion(id);
        return ResponseEntity.noContent().build();
    }
}
