package ru.volsu.course.configuration;

import org.hibernate.search.jpa.Search;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;

@Component
public class EntityIndexer implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private EntityManager entityManager;

    Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        try {
            var fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
            fullTextEntityManager.createIndexer().startAndWait();
        } catch (InterruptedException e) {
            log.error("An error occurred trying to build the search index: " + e.toString());
            throw new RuntimeException(e);
        }
    }
}
