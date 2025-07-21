package com.helpdesk.Repository;

import com.helpdesk.model.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnnouncementRepo extends JpaRepository<Announcement,Long> {
    List<Announcement> findByTitleContainingIgnoreCase(String title);

}

