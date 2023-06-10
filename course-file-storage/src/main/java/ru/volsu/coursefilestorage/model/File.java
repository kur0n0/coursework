package ru.volsu.coursefilestorage.model;

import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class File {

    @Transient
    public static final String SEQUENCE_NAME = "file_sequence";

    @Id
    private Integer fileId;

    private String uuid;

    private String title;

    private Binary file;

    public File() {
    }

    public Integer getFileId() {
        return fileId;
    }

    public void setFileId(Integer fileId) {
        this.fileId = fileId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Binary getFile() {
        return file;
    }

    public void setFile(Binary file) {
        this.file = file;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
