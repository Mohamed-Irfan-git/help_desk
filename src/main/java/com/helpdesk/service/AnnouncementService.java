package com.helpdesk.service;

import com.helpdesk.model.Announcement;
import com.helpdesk.model.User;
import com.helpdesk.Repository.AnnouncementRepo;
import com.helpdesk.Repository.UserRepo;
import com.helpdesk.DTO.AnnouncementDTO;
import com.helpdesk.Mapper.AnnounceMapper;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnnouncementService {

    private final AnnouncementRepo announcementRepo;
    private final UserRepo userRepo;
    private final AnnounceMapper announceMapper;


    public AnnouncementDTO createAnnouncements(AnnouncementDTO announcementDTO) {
        Announcement announcement = announceMapper.toEntity(announcementDTO);

        if(announcementDTO.getUserId() != null) {
            User user = userRepo.findById(announcementDTO.getUserId())
                    .orElseThrow(()->new RuntimeException("User not found"));
            announcement.setUser(user);

        }

       Announcement save = announcementRepo.save(announcement);
        return announceMapper.toDTO(save);

    }

    public List<AnnouncementDTO> findAllAnnouncements() {
        List<Announcement> announcements = announcementRepo.findAll();
        return announcements.stream()
                .map(announceMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<AnnouncementDTO> findByTitle(String title) {
        List<Announcement> announcements = announcementRepo.findByTitleContainingIgnoreCase(title);
        return announcements.stream()
                .map(announceMapper::toDTO)
                .collect(Collectors.toList());

    }

    public AnnouncementDTO findById(Long id) {
        Announcement announcement = announcementRepo.findById(id).orElse(null);
        return announceMapper.toDTO(announcement);
    }

    public AnnouncementDTO update(AnnouncementDTO announcementDTO) {
        Announcement announcement = announcementRepo.findById(announcementDTO.getAnnouncementID())
                .orElseThrow(()->new RuntimeException("Announcement not found"));
        announcement.setTitle(announcementDTO.getTitle());
        announcement.setDescription(announcementDTO.getDescription());
        announcementRepo.save(announcement);
        return announceMapper.toDTO(announcement);

    }
    public void delete(Long id) {
       announcementRepo.deleteById(id);

    }


}
