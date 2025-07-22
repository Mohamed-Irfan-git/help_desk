package com.helpdesk.controller;

import com.helpdesk.service.AnnouncementService;
import com.helpdesk.dto.AnnouncementDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/announcements")
@RequiredArgsConstructor
public class AnnouncementController {

    private final AnnouncementService announcementService;

    @PostMapping
    public ResponseEntity<AnnouncementDTO> createAnnouncement(@RequestBody AnnouncementDTO dto) {
        AnnouncementDTO created = announcementService.createAnnouncements(dto);
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<List<AnnouncementDTO>> getAllAnnouncements() {
        List<AnnouncementDTO> list = announcementService.findAllAnnouncements();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/search")
    public ResponseEntity<List<AnnouncementDTO>> searchByTitle(@RequestParam("title") String title) {
        List<AnnouncementDTO> result = announcementService.findByTitle(title);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnnouncementDTO> getById(@PathVariable Long id) {
        AnnouncementDTO dto = announcementService.findById(id);
        if (dto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AnnouncementDTO> updateAnnouncement(@PathVariable Long id,
                                                              @RequestBody AnnouncementDTO dto) {
        dto.setAnnouncementID(id);
        AnnouncementDTO updated = announcementService.update(dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnnouncement(@PathVariable Long id) {
        announcementService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
