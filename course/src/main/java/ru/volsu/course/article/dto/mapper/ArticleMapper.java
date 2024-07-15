package ru.volsu.course.article.dto.mapper;

import org.mapstruct.Mapper;
import ru.volsu.commons.dto.ArticleDto;
import ru.volsu.commons.dto.ArticlePageDto;
import ru.volsu.commons.dto.FileDto;
import ru.volsu.course.article.model.Article;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ArticleMapper {

    ArticleDto toDto(Article article, List<FileDto> fileDtoList);
    ArticlePageDto toDtoPage(List<ArticleDto> content, Integer currentPage, Integer totalPages);
}
