package com.helpdesk.Repository;

import com.helpdesk.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepo extends JpaRepository<Category,Long> {
}
