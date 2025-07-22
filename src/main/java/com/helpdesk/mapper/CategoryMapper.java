package com.helpdesk.mapper;

import com.helpdesk.model.Category;
import com.helpdesk.dto.CategoryDTO;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;




@Mapper(componentModel = "spring" ,uses = {QuestionMapper.class})
public interface CategoryMapper {

    @Mapping(source = "questions",target = "questions")
    CategoryDTO toDTO(Category category);

    @InheritInverseConfiguration
    @Mapping(target = "questions",ignore = true)
    @Mapping(target = "categoryId",ignore = true)
    Category toEntity(CategoryDTO dto);

//    @Named("mapQuestionToIds")
//    static List<Long> mapQuestionToIds(List<Question> questions) {
//        return questions !=null?
//                questions.stream()
//                        .map(Question::getQuestionID)
//                        .collect(Collectors.toList()): null;
//    }
}
