package ru.volsu.coursefilestorage.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.volsu.coursefilestorage.dao.FileRepository;
import ru.volsu.coursefilestorage.model.File;

import java.util.NoSuchElementException;

@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private FileRepository fileRepository;

    @Override
    public void saveFile(File file) {
        fileRepository.save(file);
    }

    @Override
    public File getFile(Integer fileId) {
        return fileRepository.findById(fileId).orElseThrow(() -> new NoSuchElementException(String.format("Файла с id: %s не сущнествует", fileId)));
    }
}
