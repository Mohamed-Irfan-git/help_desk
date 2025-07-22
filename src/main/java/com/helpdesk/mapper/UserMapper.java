package com.helpdesk.mapper;

import com.helpdesk.model.User;
import com.helpdesk.dto.UserDTO;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;



@Mapper(componentModel = "spring"  ,uses = {QuestionMapper.class, AnswerMapper.class})
public interface UserMapper {

    @Mapping(source = "questions", target = "questions")
    @Mapping(source = "answers", target = "answers")
    @Mapping(source = "announcements", target = "announcements")
    @Mapping(source = "batchNo", target = "batchNo")
    @Mapping(source = "department.departmentId", target = "departmentId")
    UserDTO toDTO(User user);

    @InheritInverseConfiguration
    @Mapping(target = "questions", ignore = true)
    @Mapping(target = "answers", ignore = true)
    @Mapping(target = "announcements", ignore = true)
    @Mapping(target = "batchNo", ignore = true)
    @Mapping(target = "department", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    User toEntity(UserDTO userDTO);

//    @Named("mapQuestionsToIds")
//    static List<Long> mapQuestionsToIds(List<Question> questions) {
//        return questions != null ? questions.stream()
//                .map(Question::getQuestionID)
//                .collect(Collectors.toList()) : null;
//    }

//    @Named("mapAnswersToIds")
//    static List<Long> mapAnswersToIds(List<Answer> answers) {
//        return answers != null ? answers.stream()
//                .map(Answer::getAnswerId)
//                .collect(Collectors.toList()) : null;
//    }

//    @Named("mapAnnouncementsToIds")
//    static List<Long> mapAnnouncementsToIds(List<Announcement> announcements) {
//        return announcements != null ? announcements.stream()
//                .map(Announcement::getAnnouncementId)
//                .collect(Collectors.toList()) : null;
//    }
}
