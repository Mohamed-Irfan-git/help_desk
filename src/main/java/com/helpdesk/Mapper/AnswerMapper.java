package com.helpdesk.Mapper;


import com.helpdesk.model.Answer;
import com.helpdesk.DTO.AnswerDTO;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",uses= UserMapper.class, imports = QuestionMapper.class)
public interface AnswerMapper {

    @Mapping(source = "question.questionId",target = "questionId")
    @Mapping(source = "user.userId",target = "userId")
    @Mapping(source = "createdDate",target="createdAt")
    AnswerDTO toDTO(Answer answer);


    @InheritInverseConfiguration
    @Mapping(target = "question", ignore = true)
    @Mapping(target = "createdDate",ignore = true)
    @Mapping(target = "user",ignore = true)
    Answer toEntity(AnswerDTO answerDTO);
}
