package ru.volsu.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.volsu.course.model.Article;
import ru.volsu.course.service.ArticleService;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {

    public static final String ARTICLE_FORM_URL = "/article-form";
    @Autowired
    private ArticleService articleService;

    @GetMapping(value = "/article-form")
    public String articleForm() {
        return "/article-form";
    }

    @ModelAttribute(value = "article")
    public Article newArticle() {
        return new Article();
    }

    @PostMapping(value = "/article")
    public String createArticle(@ModelAttribute Article article, ModelMap modelMap) {
        modelMap.clear();
        articleService.save(article);
        return ARTICLE_FORM_URL;
    }

    @PutMapping(value = "/article")
    public void updateArticle(@RequestParam Integer articleId,
                              @RequestParam String text,
                              @RequestParam String title,
                              @RequestParam String tag) {
        Article article = articleService.findById(articleId);
        article.setText(text);
        article.setTitle(title);
        article.setTag(tag);

        articleService.save(article);
    }

    @DeleteMapping(value = "/article")
    public void deleteArticle(@RequestParam Integer articleId) {
        articleService.delete(articleId);
    }

    @GetMapping(value = "/article")
    public ModelAndView getArticles(ModelAndView model) {
        model.addObject("articleList", articleService.findAll());
        model.setViewName("index");
        return model;
    }
}
