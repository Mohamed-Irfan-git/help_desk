package com.helpdesk.Repository;

import com.helpdesk.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestionRepo extends JpaRepository<Question,Long> {
    @Query("SELECT q FROM Question q WHERE q.user.userId = :userId")
    List<Question> findByUserId(@Param("userId") Long userId);
}
