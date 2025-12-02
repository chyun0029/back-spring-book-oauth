package me.book_ch8.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.book_ch8.domain.Article;
import me.book_ch8.dto.ArticleListViewResponse;
import me.book_ch8.dto.ArticleViewResponse;
import me.book_ch8.service.BlogService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class BlogViewController {
    private final BlogService blogService;

    // 전체 조회
    @GetMapping("/articles")
    public String getArticles(Model model) {
        List<ArticleListViewResponse> articles = blogService.findAll().stream()
                .map(ArticleListViewResponse::new)
                .toList();
        model.addAttribute("articles", articles);
        return "articleList";
    }

    // 조회 1개
    @GetMapping("/articles/{id}")
    public String getArticle(@PathVariable("id") Long id, Model model) {
        Article article = blogService.findById(id);
        model.addAttribute("article", new ArticleViewResponse(article));
        return "article";
    }

    // 글 등록 or 수정
    @GetMapping("/new-article")
    //id 키를 가진 쿼리 파라미터의 값(?id=값)을 id 변수에 매핑(id는 없을수도있음)
    public String newArticle(@RequestParam(required = false) Long id, Model model) {
        if(id==null){//id가 없으면 생성
            model.addAttribute("article", new ArticleViewResponse());
        }else{ //id가 있으면 수정
            Article article = blogService.findById(id);
            model.addAttribute("article", new ArticleViewResponse(article));
        }
        return "newArticle";
    }
}
