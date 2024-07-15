package ru.volsu.course.article.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.volsu.commons.dto.ArticlePageDto;
import ru.volsu.course.article.service.ArticleService;

@RestController
@RequestMapping(value = "/api/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @GetMapping(value = "/tag/page")
    public ArticlePageDto getPageByTag(@RequestParam Integer page,
                                       @RequestParam Integer size,
                                       @RequestParam String tag) throws Exception {
        return articleService.findByTag(tag, PageRequest.of(page, size));
    }

    @GetMapping(value = "/title/page")
    public ArticlePageDto getPageByTitle(@RequestParam Integer page,
                                         @RequestParam Integer size,
                                         @RequestParam String title) throws Exception {
        return articleService.findByTitle(title, PageRequest.of(page, size));
    }

    @GetMapping(value = "/page")
    public ArticlePageDto fullTextSearch(@RequestParam String query,
                                         @RequestParam Integer page,
                                         @RequestParam Integer size) throws Exception {
        return articleService.fullTextSearch(query, PageRequest.of(page, size));
    }
}
