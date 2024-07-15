package ru.volsu.course.article.service;

import org.springframework.web.multipart.MultipartFile;
import ru.volsu.commons.dto.FileDto;

import java.util.List;

public interface FileService {

    String createFile(MultipartFile file, Integer articleId) throws Exception;

    List<FileDto> getFiles(List<String> uuidList, Integer articleId) throws Exception;
}
