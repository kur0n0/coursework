package ru.volsu.coursefilestorage.controller;

import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.volsu.coursefilestorage.model.File;
import ru.volsu.coursefilestorage.service.FileService;

import java.io.IOException;
import java.util.HashMap;

@RestController
@RequestMapping(value = "/api/file")
public class FileController {

    @Autowired
    private FileService fileService;

    @GetMapping(value = "/{uuid}")
    public File getFile(@PathVariable String uuid) {
        return fileService.getFile(uuid);
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
