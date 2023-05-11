package ru.volsu.coursefilestorage.controller;

import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.volsu.coursefilestorage.model.File;
import ru.volsu.coursefilestorage.service.FileService;

import java.io.IOException;

@RestController
@RequestMapping(value = "/api/file")
public class FileController {

    @Autowired
    private FileService fileService;

    @GetMapping(value = "/{fileId}")
    public File getFile(@PathVariable Integer fileId) {
        return fileService.getFile(fileId);
    }

    @PostMapping
    public void saveFile(@RequestParam String title,
                         @RequestParam Integer id,
                         @RequestParam MultipartFile file) throws IOException {
        File newFile = new File();
        newFile.setFileId(id);
        newFile.setFile(new Binary(BsonBinarySubType.BINARY, file.getBytes()));
        newFile.setTitle(title);
        fileService.saveFile(newFile);
    }
}
