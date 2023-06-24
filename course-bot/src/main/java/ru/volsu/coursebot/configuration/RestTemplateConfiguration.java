package ru.volsu.coursebot.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RootUriTemplateHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfiguration {

    @Value("${core.api.url}")
    private String rootUrl;

    @Value("${core.api.login}")
    private String login;

    @Value("${core.api.password}")
    private String password;

    @Bean(name = "coreTemplate")
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(login, password));
        restTemplate.setUriTemplateHandler(new RootUriTemplateHandler(rootUrl));
        return restTemplate;
    }
}
