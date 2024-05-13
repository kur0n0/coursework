package ru.volsu.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.volsu.course.model.Article;
import ru.volsu.course.dto.ArticleDto;
import ru.volsu.course.service.ArticleService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {

    private static final String ARTICLE_FORM_URL = "article-form";

    @Autowired
    private ArticleService articleService;

    @ModelAttribute(value = "article")
    public Article newArticle() {
        return new Article();
    }

    @GetMapping(value = "/article-form")
    public String articleForm() {
        return "article-form";
    }

    @PostMapping(value = "/article")
    public String createArticle(@ModelAttribute Article article,
                                @RequestParam MultipartFile[] files) throws Exception {
        articleService.save(article, files);

        article = null; // очищаем модель для нового добавления
        return "redirect:/admin/article/page/";
    }

    @GetMapping(value = "/article/page")
    public String getPage(Model model,
                          @RequestParam Optional<Integer> page,
                          @RequestParam Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(10);

        Page<Article> articlePage = articleService.findAll(PageRequest.of(currentPage - 1, pageSize));
        model.addAttribute("articlePage", articlePage);
        int totalPages = articlePage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "article-table.html";
    }

    @GetMapping(value = "/article/one")
    public String getOne(@RequestParam Integer articleId,
                         Model model) throws Exception {
        ArticleDto articleDto = articleService.getOne(articleId);
        model.addAttribute("articleDto", articleDto);
        return "article-one.html";
    }

    @DeleteMapping(value = "/article")
    public void deleteArticle(@RequestParam Integer articleId) {
        articleService.delete(articleId);
    }
}
