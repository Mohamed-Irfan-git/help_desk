package com.helpdesk.service;

import com.helpdesk.model.Answer;
import com.helpdesk.model.Question;
import com.helpdesk.model.User;
import com.helpdesk.repository.AnswerRepo;
import com.helpdesk.repository.QuestionRepo;
import com.helpdesk.repository.UserRepo;
import com.helpdesk.dto.AnswerDTO;
import com.helpdesk.mapper.AnswerMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerRepo answerRepo;
    private final QuestionRepo questionRepo;
    private final UserRepo userRepo;
    private final AnswerMapper answerMapper;



    public AnswerDTO createAnswer(AnswerDTO dto) {
        Answer answer = answerMapper.toEntity(dto);
        if(dto.getUserId() != null) {
            User user =  userRepo.findById(dto.getUserId())
                    .orElseThrow(() -> new EntityNotFoundException("User not found"));
            answer.setUser(user);
        }
        if(dto.getQuestionId() != null) {
            Question question = questionRepo.findById(dto.getQuestionId())
                            .orElseThrow(() -> new EntityNotFoundException("Question not found"));
            answer.setQuestion(question);
        }

        Answer savedAnswer = answerRepo.save(answer);
        return answerMapper.toDTO(savedAnswer);
    }

    public AnswerDTO updateAnswer(AnswerDTO dto) {
        Answer exist = answerRepo.findById(dto.getAnswerId())
                        .orElseThrow(()-> new EntityNotFoundException("Answer not found"));

        exist.setDescription(dto.getDescription());
        Answer savedAnswer = answerRepo.save(exist);
        return answerMapper.toDTO(savedAnswer);
    }

    public List<AnswerDTO> findAllAnswer() {
        return answerRepo.findAll()
                .stream()
                .map(answerMapper::toDTO)
                .collect(Collectors.toList());
    }

    public AnswerDTO findAnswerById(Long id) {
        return answerMapper.toDTO(answerRepo.findById(id)
                .orElseThrow(()->new RuntimeException("Answer not found!")));
    }
    public void deleteAnswer(Long id) {
        answerRepo.deleteById(id);
    }


}
