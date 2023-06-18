package ru.volsu.course.service;

import org.springframework.web.multipart.MultipartFile;
import ru.volsu.course.model.FileDto;

import java.util.List;

public interface FileService {

    String createFile(MultipartFile file, Integer articleId) throws Exception;

    List<FileDto> getFiles(List<String> uuidList, Integer articleId) throws Exception;
}
