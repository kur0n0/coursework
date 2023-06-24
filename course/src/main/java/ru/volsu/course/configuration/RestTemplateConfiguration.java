package ru.volsu.course.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RootUriTemplateHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplateHandler;

@Configuration
public class RestTemplateConfiguration {

    @Value("${files.api.url}")
    public String rootUrl;

    @Bean(name = "fileTemplate")
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        UriTemplateHandler uriTemplateHandler = new RootUriTemplateHandler(rootUrl);
        restTemplate.setUriTemplateHandler(uriTemplateHandler);
        return restTemplate;
    }
}
