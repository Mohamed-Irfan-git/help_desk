package com.helpdesk.controller;

import com.helpdesk.service.AnswerService;
import com.helpdesk.dto.AnswerDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "https://help-desk-eosin.vercel.app")
@RestController
@RequestMapping("/api/answers")
@RequiredArgsConstructor
public class AnswerController {

    private final AnswerService answerService;

    @PostMapping
    public ResponseEntity<AnswerDTO> createAnswer(@RequestBody AnswerDTO dto) {
        AnswerDTO created = answerService.createAnswer(dto);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AnswerDTO> updateAnswer(@PathVariable Long id, @RequestBody AnswerDTO dto) {
        dto.setAnswerId(id);
        AnswerDTO updated = answerService.updateAnswer(dto);
        return ResponseEntity.ok(updated);
    }

    @GetMapping
    public ResponseEntity<List<AnswerDTO>> getAllAnswers() {
        return ResponseEntity.ok(answerService.findAllAnswer());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnswerDTO> getAnswerById(@PathVariable Long id) {
        return ResponseEntity.ok(answerService.findAnswerById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnswer(@PathVariable Long id) {
        answerService.deleteAnswer(id);
        return ResponseEntity.noContent().build();
    }
}
