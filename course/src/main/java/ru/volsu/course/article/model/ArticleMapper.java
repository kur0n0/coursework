package ru.volsu.course.article.model;

import org.mapstruct.Mapper;
import ru.volsu.commons.dto.ArticleDto;
import ru.volsu.commons.dto.ArticlePageDto;
import ru.volsu.commons.dto.FileDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ArticleMapper {

    ArticleDto toDto(Article article, List<FileDto> fileDtoList);

    ArticlePageDto toDtoPage(List<ArticleDto> content, Integer currentPage, Integer totalPages);
}
