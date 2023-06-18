package ru.volsu.coursefilestorage.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.volsu.coursefilestorage.dao.FileRepository;
import ru.volsu.coursefilestorage.model.File;
import ru.volsu.coursefilestorage.model.FileDto;
import ru.volsu.coursefilestorage.model.FileRequest;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private SequenceGenerator sequenceGenerator;

    @Override
    public String saveFile(File file) {
        String uuid = UUID.randomUUID().toString();
        file.setFileId(sequenceGenerator.generateSequence(File.SEQUENCE_NAME));
        file.setUuid(uuid);
        fileRepository.save(file);
        return uuid;
    }

    @Override
    public File getFile(String uuid) {
        return fileRepository.findByUuid(uuid)
                .orElseThrow(() -> new NoSuchElementException(String.format("Файла с uuid: %s не существует", uuid)));
    }

    @Override
    public FileDto getFileDto(String uuid) {
        return new FileDto(getFile(uuid));
    }

    @Override
    public List<FileDto> getBatchFileDto(FileRequest fileRequest) {
        return fileRepository.findAllByUuidIn(fileRequest.getUuidList())
                .stream()
                .map(FileDto::new)
                .collect(Collectors.toList());
    }
}
