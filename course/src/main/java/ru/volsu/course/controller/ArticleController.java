package ru.volsu.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.volsu.course.model.Article;
import ru.volsu.course.service.ArticleService;

import java.util.List;

@RestController
@RequestMapping(value = "/api/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @GetMapping(value = "/{articleId}")
    public Article getById(@PathVariable Integer articleId) {
        return articleService.findById(articleId);
    }

    @PostMapping
    public void create(@RequestBody Article article) {
        articleService.save(article);
    }

    @DeleteMapping(value = "/{articleId}")
    public void delete(@PathVariable Integer articleId) {
        articleService.delete(articleId);
    }

    @GetMapping(value = "/all")
    public List<Article> getList() {
        return articleService.findAll();
    }
}
