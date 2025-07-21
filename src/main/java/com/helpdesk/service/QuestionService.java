package com.helpdesk.service;

import com.helpdesk.model.Category;
import com.helpdesk.model.Question;
import com.helpdesk.model.User;
import com.helpdesk.Repository.CategoryRepo;
import com.helpdesk.Repository.QuestionRepo;
import com.helpdesk.Repository.UserRepo;
import com.helpdesk.DTO.QuestionDTO;
import com.helpdesk.Mapper.QuestionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepo questionRepo;
    private final UserRepo userRepo;
    private final CategoryRepo categoryRepo;
    private final QuestionMapper questionMapper;

    public QuestionDTO createQuestion(QuestionDTO dto) {
        Question question = questionMapper.toEntity(dto);

        if (dto.getUserId() != null) {
            User user = userRepo.findById(dto.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            question.setUser(user);
        }

        if (dto.getCategoryId() != null) {
            Category category = categoryRepo.findById(dto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            question.setCategory(category);
        }

        Question saved = questionRepo.save(question);
        return questionMapper.toDTO(saved);
    }

    public List<QuestionDTO> getAllQuestions() {
        List<Question> questions = questionRepo.findAll();

        // Defensive: ensure user and category are loaded if lazy (optional)
        questions.forEach(q -> {
            if (q.getUser() != null) {
                q.getUser().getUserId();
            }
            if (q.getCategory() != null) {
                q.getCategory().getCategoryId();
            }
        });

        return questions.stream()
                .map(questionMapper::toDTO)
                .collect(Collectors.toList());
    }

    public QuestionDTO findById(Long id) {
        Question question = questionRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Question not found"));
        return questionMapper.toDTO(question);
    }

    public QuestionDTO updateQuestion(QuestionDTO dto) {
        Question existing = questionRepo.findById(dto.getQuestionId())
                .orElseThrow(() -> new RuntimeException("Question not found"));

        existing.setTitle(dto.getTitle());
        existing.setDescription(dto.getDescription());
        existing.setAnonymous(dto.getAnonymous());
        existing.setVote(dto.getVote());

        if (dto.getCategoryId() != null) {
            Category category = categoryRepo.findById(dto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            existing.setCategory(category);
        }

        Question updated = questionRepo.save(existing);
        return questionMapper.toDTO(updated);
    }


    public void deleteQuestion(Long id) {
        questionRepo.deleteById(id);
    }


}
