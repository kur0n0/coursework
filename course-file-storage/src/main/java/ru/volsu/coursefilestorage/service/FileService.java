package ru.volsu.coursefilestorage.service;

import ru.volsu.coursefilestorage.model.File;

public interface FileService {
    String saveFile(File file);
    File getFile(String uuid);
}
