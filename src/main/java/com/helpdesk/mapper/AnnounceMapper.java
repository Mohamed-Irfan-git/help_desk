package com.helpdesk.mapper;

import com.helpdesk.model.Announcement;
import com.helpdesk.dto.AnnouncementDTO;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AnnounceMapper {

    @Mapping(source = "user.userId",target = "userId")
    AnnouncementDTO toDTO(Announcement announcement);

    @InheritInverseConfiguration
    @Mapping(target = "user",ignore = true)
    @Mapping(target = "createdAt",ignore = true)
    Announcement toEntity(AnnouncementDTO announcementDTO);

}
