package ru.volsu.course.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.volsu.course.model.Article;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Integer> {
//    @Query(value = "SELECT a FROM Article a WHERE a.tag = :tag")
//    Page<Article> findByTag(@Param(value = "tag") String tag, Pageable pageable);
    Page<Article> findByTag(String tag, Pageable pageable);
}
