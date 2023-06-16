package ru.volsu.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import ru.volsu.course.model.Article;
import ru.volsu.course.service.ArticleService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {

    private static final String ARTICLE_FORM_URL = "/article-form";

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
    public String createArticle(@ModelAttribute Article article,
                                ModelMap modelMap) {
        modelMap.clear();
        articleService.save(article);
        return ARTICLE_FORM_URL;
    }

    @GetMapping(value = "/article/page")
    public String getPage(Model model,
                          @RequestParam("page") Optional<Integer> page,
                          @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(1);

        Page<Article> articlePage = articleService.findAll(PageRequest.of(currentPage - 1, pageSize));
        model.addAttribute("articlePage", articlePage);
        int totalPages = articlePage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "/article-table.html";
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
}
