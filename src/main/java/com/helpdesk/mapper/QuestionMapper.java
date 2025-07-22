package com.helpdesk.mapper;


import com.helpdesk.model.Question;
import com.helpdesk.dto.QuestionDTO;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;



@Mapper(componentModel = "spring",uses = {AnswerMapper.class})
public interface QuestionMapper {

    @Mapping(source = "answers",target = "answers")
    @Mapping(source = "user.userId",target = "userId")
    @Mapping(source = ".", target = "userName", qualifiedByName = "mapUserName")
    @Mapping(source = "category.categoryId",target = "categoryId")
    QuestionDTO toDTO(Question question);

    @InheritInverseConfiguration
    @Mapping(target = "answers",ignore = true)
    @Mapping(target = "user",ignore = true)
    @Mapping(target = "category",ignore = true)
    @Mapping(target = "createdDate",ignore = true)
    @Mapping(source = "anonymous", target = "anonymous")
    Question toEntity(QuestionDTO questionDTO);


//    @Named("mapAnswersToIds")
//    static List<Long> mapAnswersToIds(List<Answer>answers) {
//        return answers != null ? answers
//                .stream()
//                .map(Answer::getAnswerId)
//                .collect(Collectors.toList()) : null;
//    }

    @Named("mapUserName")
    static String mapUserName(Question question) {
        if (question.getAnonymous()) {
            return "Anonymous";
        } else if (question.getUser() != null) {
            return question.getUser().getFirstName() + " " + question.getUser().getLastName();
        } else {
            return null;
        }
    }


}
