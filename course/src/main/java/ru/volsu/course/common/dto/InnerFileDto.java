package ru.volsu.course.common.dto;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

public class InnerFileDto {
    private String fileName;
    private byte[] bytes;

    public InnerFileDto(MultipartFile multipartFile) throws IOException {
        this.fileName = Optional.ofNullable(multipartFile.getOriginalFilename()).orElse(UUID.randomUUID().toString());
        this.bytes = multipartFile.getBytes();
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }
}
