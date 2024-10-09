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
import java.util.stream.Collectors;

@Service
public class FileServiceImpl implements FileService {

    Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    @Qualifier(value = "fileTemplate")
    private RestTemplate restTemplate;

    @Override
    public String createFile(InnerFileDto file) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        log.info("Создание файла с названием: {}", file.getFileName());

        log.debug("Сбор запроса для создания файла");
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("title", file.getFileName());

        LinkedMultiValueMap<String, String> fileHeaderMap = new LinkedMultiValueMap<>();
        fileHeaderMap.add("Content-disposition", "form-data; name=file; filename=" + file.getFileName());
        map.add("file", new HttpEntity<>(file.getBytes(), fileHeaderMap));

        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(map, headers);

        ResponseEntity<String> responseEntity;
        try {
            log.debug("Отправка запроса на создание запроса во внешней системы");
            responseEntity = restTemplate.postForEntity("/api/file", request, String.class);
        } catch (RestClientException e) {
            log.error("При запросе создания файла: {} возникла сетевая ошибка: {}", file.getFileName(), e.getMessage());
            throw new Exception("Сетевая ошибка при запросе создания файла: " + e.getMessage()); // todo создать свое исключение
        }

        String body = responseEntity.getBody();
        String uuid;
        try {
            log.debug("Разбор ответа от внешней системы");
            JSONObject jsonObject = new JSONObject(body);
            uuid = jsonObject.getString("uuid");
        } catch (JSONException e) {
            log.error("Ошибка разбора ответа от файлового сервиса: {}", e.getMessage());
            throw new Exception("Ошибка разбора ответа от файлового сервиса: " + e.getMessage()); // todo создать свое исключение
        }

        log.info("Создан файл: {} с названием: {}", uuid, file.getFileName());
        return uuid;
    }

    @Override
    public List<FileDto> getFiles(List<String> uuidList) throws Exception {
        String uuidListString = uuidList.stream().collect(Collectors.joining(","));
        log.info("Получение файлов по списку uuid: {}", uuidListString);

        FileRequest fileRequest = new FileRequest(uuidList);
        ResponseEntity<FileDto[]> responseEntity;
        try {
            responseEntity = restTemplate.postForEntity("/api/file/batch", fileRequest, FileDto[].class);
        } catch (RestClientException e) {
            log.error("При получении списка файлов: {} возникла ошибка: {}", uuidListString, e.getMessage());
            throw new Exception("Сетевая ошибка при получении списка файлов: " + e.getMessage()); // todo создать свое исключение
        }

        FileDto[] fileDtos = responseEntity.getBody();
        log.info("Получено {} файлов", fileDtos.length);
        return Arrays.asList(fileDtos);
    }
}
