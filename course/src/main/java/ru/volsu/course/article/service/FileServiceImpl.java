package ru.volsu.course.article.service;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ru.volsu.commons.dto.FileDto;
import ru.volsu.commons.dto.FileRequest;
import ru.volsu.course.common.dto.InnerFileDto;

import java.util.Arrays;
import java.util.List;

@Service
public class FileServiceImpl implements FileService {

    Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    @Qualifier(value = "fileTemplate")
    private RestTemplate restTemplate;

    @Override
    public String createFile(InnerFileDto file, Integer articleId) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("title", file.getFileName());

        LinkedMultiValueMap<String, String> fileHeaderMap = new LinkedMultiValueMap<>();
        fileHeaderMap.add("Content-disposition", "form-data; name=file; filename=" + file.getFileName());
        map.add("file", new HttpEntity<>(file.getBytes(), fileHeaderMap));

        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(map, headers);

        ResponseEntity<String> responseEntity;
        try {
            responseEntity = restTemplate.postForEntity("/api/file", request, String.class);
        } catch (RestClientException e) {
            log.error("При запросе создания файла: {} возникла сетевая ошибка: {}", articleId, e.getMessage());
            throw new Exception("Сетевая ошибка при запросе создания файла: " + e.getMessage()); // todo создать свое исключение
        }

        String body = responseEntity.getBody();
        String uuid;
        try {
            JSONObject jsonObject = new JSONObject(body);
            uuid = jsonObject.getString("uuid");
        } catch (JSONException e) {
            log.error("Ошибка разбора ответа от файлового сервиса: {}", e.getMessage());
            throw new Exception("Ошибка разбора ответа от файлового сервиса: " + e.getMessage()); // todo создать свое исключение
        }

        log.debug("Создан файл: {} для статьи: {}", uuid, articleId);
        return uuid;
    }

    @Override
    public List<FileDto> getFiles(List<String> uuidList, Integer articleId) throws Exception {
        FileRequest fileRequest = new FileRequest(uuidList);
        ResponseEntity<FileDto[]> responseEntity;
        try {
            responseEntity = restTemplate.postForEntity("/api/file/batch", fileRequest, FileDto[].class);
        } catch (RestClientException e) {
            log.error("При получении списка файлов по статье: {} возникла ошибка: {}", articleId, e.getMessage());
            throw new Exception("Сетевая ошибка при получении списка файлов: " + e.getMessage()); // todo создать свое исключение
        }

        return Arrays.asList(responseEntity.getBody());
    }
}
