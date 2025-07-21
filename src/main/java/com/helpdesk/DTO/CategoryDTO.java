package com.helpdesk.DTO;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {
    private long categoryId;
    private String categoryName;
    private List<QuestionDTO> questions;


    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<QuestionDTO> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionDTO> questions) {
        this.questions = questions;
    }
}
