package ru.volsu.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.volsu.course.model.Article;
import ru.volsu.course.service.ArticleService;

import java.util.List;

@RestController
@RequestMapping(value = "/api/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @GetMapping(value = "/page")
    public Page<Article> getPage(@RequestParam Integer page,
                                 @RequestParam Integer size,
                                 @RequestParam String tag) {
        return articleService.findAllByTag(tag, PageRequest.of(page, size));
    }
}
