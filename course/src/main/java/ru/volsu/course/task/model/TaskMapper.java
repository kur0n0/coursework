package ru.volsu.course.task.model;

import org.mapstruct.Mapper;
import ru.volsu.commons.dto.ArticleDto;
import ru.volsu.commons.dto.TaskDTO;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    TaskDTO toDto(Task task, ArticleDto hint);
}
