package ru.volsu.coursefilestorage.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Base64;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class FileDto {
    private String uuid;

    private String title;

    private String file;

    public FileDto(File file) {
        this.uuid = file.getUuid();
        this.title = file.getTitle();
        this.file = Base64.getEncoder().encodeToString(file.getFile().getData());
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
}
