package ru.volsu.course.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class FileRequest {
    private List<String> uuidList;

    public FileRequest(List<String> uuidList) {
        this.uuidList = uuidList;
    }

    public List<String> getUuidList() {
        return uuidList;
    }

    public void setUuidList(List<String> uuidList) {
        this.uuidList = uuidList;
    }
}
