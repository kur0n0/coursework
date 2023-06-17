package ru.volsu.coursefilestorage.controller;

import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.volsu.coursefilestorage.model.File;
import ru.volsu.coursefilestorage.model.FileDto;
import ru.volsu.coursefilestorage.model.FileRequest;
import ru.volsu.coursefilestorage.service.FileService;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(value = "/api/file")
public class FileController {

    @Autowired
    private FileService fileService;

    @GetMapping(value = "/{uuid}")
    public FileDto getFile(@PathVariable String uuid) {
        return fileService.getFileDto(uuid);
    }

    @GetMapping(value = "/batch")
    public List<FileDto> getBatchFiles(@RequestBody FileRequest fileRequest) {
        return fileService.getBatchFileDto(fileRequest);
    }

    @PostMapping
    public ResponseEntity saveFile(@RequestParam String title,
                                   @RequestParam byte[] file) throws IOException {
        File newFile = new File();
        newFile.setFile(new Binary(BsonBinarySubType.BINARY, file));
        newFile.setTitle(title);

        HashMap<String, Object> response = new HashMap<>();
        response.put("uuid", fileService.saveFile(newFile));
        return ResponseEntity.ok(response);
    }
}
