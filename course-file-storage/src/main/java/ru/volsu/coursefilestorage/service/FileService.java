package ru.volsu.coursefilestorage.service;

import ru.volsu.coursefilestorage.model.File;
import ru.volsu.coursefilestorage.model.FileDto;
import ru.volsu.coursefilestorage.model.FileRequest;

import java.util.List;

public interface FileService {
    String saveFile(File file);

    File getFile(String uuid);

    FileDto getFileDto(String uuid);

    List<FileDto> getBatchFileDto(FileRequest fileRequest);
}
