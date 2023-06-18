package ru.volsu.course.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
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
        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();

        UriTemplateHandler uriTemplateHandler = new RootUriTemplateHandler(rootUrl);
        return restTemplateBuilder.uriTemplateHandler(uriTemplateHandler)
                .build();
    }
}
