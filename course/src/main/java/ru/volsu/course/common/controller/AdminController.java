package ru.volsu.course.common.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.volsu.commons.dto.ArticleDto;
import ru.volsu.course.task.model.enums.AnswerMappingEnum;
import ru.volsu.course.article.model.Article;
import ru.volsu.course.task.model.Task;
import ru.volsu.course.article.service.ArticleService;
import ru.volsu.course.task.service.TaskService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private TaskService taskService;

    @ModelAttribute(value = "article")
    public Article newArticle() {
        return new Article();
    }

    @GetMapping(value = "/article/form")
    public String articleForm() {
        return "article-form.html";
    }

    @PostMapping(value = "/article")
    public String createArticle(@ModelAttribute Article article,
                                @RequestParam MultipartFile[] files) throws Exception {
        articleService.save(article, files);

        article = null; // очищаем модель для нового добавления
        return "redirect:/admin/article/page/";
    }

    @GetMapping(value = "/article/page")
    public String getAritclePage(Model model,
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

    @PostMapping(value = "/article/delete")
    public String deleteArticle(@RequestParam Integer articleId) {
        articleService.delete(articleId);
        return "redirect:/admin/article/page/";
    }

    @GetMapping(value = "/task/page")
    public String getTaskPage(Model model,
                              @RequestParam Optional<Integer> page,
                              @RequestParam Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(10);

        Page<Task> taskPage = taskService.findAll(PageRequest.of(currentPage - 1, pageSize));
        model.addAttribute("taskPage", taskPage);
        int totalPages = taskPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "task-table.html";
    }

    @GetMapping(value = "/task/form")
    public String taskForm() {
        return "task-form.html";
    }

    @PostMapping(value = "/task")
    public String createTask(@RequestParam String question,
                             @RequestParam String answer,
                             @RequestParam AnswerMappingEnum answerMapping,
                             @RequestParam(required = false) String articleTitleHint) {
        Optional<Article> article = articleService.findByTitle(articleTitleHint);

        Task task = new Task();
        task.setQuestion(question);
        task.setAnswer(answer);
        task.setAnswerMapping(answerMapping);
        article.ifPresent(a -> task.setHintArticleId(a.getArticleId()));

        taskService.save(task);

        return "redirect:/admin/task/page/";
    }

    @PostMapping(value = "/task/delete") // todo: исправить на софт удаление
    public String deleteTask(@RequestParam Long taskId) {
        taskService.delete(taskId);
        return "redirect:/admin/task/page/";
    }
}
