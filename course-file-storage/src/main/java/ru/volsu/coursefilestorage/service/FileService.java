package ru.volsu.coursefilestorage.service;

import ru.volsu.coursefilestorage.model.File;

public interface FileService {
    void saveFile(File file);
    File getFile(Integer fileId);
}
