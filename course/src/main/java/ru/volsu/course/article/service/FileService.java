package ru.volsu.course.article.service;

import ru.volsu.commons.dto.FileDto;
import ru.volsu.course.common.dto.InnerFileDto;

import java.util.List;

public interface FileService {

    String createFile(InnerFileDto file, Integer articleId) throws Exception;

    List<FileDto> getFiles(List<String> uuidList, Integer articleId) throws Exception;
}
