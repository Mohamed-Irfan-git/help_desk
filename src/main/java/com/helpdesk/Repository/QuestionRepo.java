package com.helpdesk.Repository;

import com.helpdesk.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepo extends JpaRepository<Question,Long> {
}
