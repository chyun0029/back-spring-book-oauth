package me.book_ch8.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.book_ch8.domain.Article;
import me.book_ch8.dto.AddArticleRequest;
import me.book_ch8.dto.ArticleResponse;
import me.book_ch8.dto.UpdateArticleRequest;
import me.book_ch8.service.BlogService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

// 현재 페이지 기능은 Postman
@Slf4j
@RequiredArgsConstructor
@RestController // HTTP ResponseBody에 객체 데이터를 JSON 형식으로 반환하는 컨트롤러
                //HTTP 요청과 메서드를 연결해주는 장치 (@Controller, @ResponseBody 포함)
public class BlogApiController {
    private final BlogService blogService;

//    //글 등록
//    //HTTP 메서드가 POST일 때 전달받은 URL과 동일하면 메서드로 매핑
//    @PostMapping("/api/articles")
    //@RequestBody로 요청 본문 값 매핑
//    public ResponseEntity<Article> addArticle(@RequestBody AddArticleRequest request){
//        Article savedArticle = blogService.save(request);
//        //요청한 자원이 성공적으로 생성되었으며 저장된 블로그 글 정보를 응답 객체에 담아 전송
//        return ResponseEntity.status(HttpStatus.CREATED)
//                .body(savedArticle);
//    }

    //글 등록(10장). 현재 인증 정보를 가져오는 principal 객체를 파라미터로 추가
    //HTTP 메서드가 POST일 때 전달받은 URL과 동일하면 메서드로 매핑
    @PostMapping("/api/articles")
    public ResponseEntity<Article> addArticle(@RequestBody AddArticleRequest request, Principal principal){
        Article savedArticle = blogService.save(request, principal.getName());
//        log.info("로그 실행");
//        log.info(principal.getName());
        //요청한 자원이 성공적으로 생성되었으며 저장된 블로그 글 정보를 응답 객체에 담아 전송
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedArticle);
    }

    //전체 조회
    @GetMapping("/api/articles")
    public ResponseEntity<List<ArticleResponse>> findAllArticles(){
        List<ArticleResponse> articles = blogService.findAll()
                .stream()
                .map(ArticleResponse::new)
                .toList();

        return ResponseEntity.ok()
                .body(articles);
    }

    //조회 1개(id로)
    @GetMapping("/api/articles/{id}")
    public ResponseEntity<ArticleResponse> findArticle(@PathVariable("id") long id){
        Article article = blogService.findById(id);
        return ResponseEntity.ok()
                .body(new ArticleResponse(article));
    }

    //글 삭제(id로)
    @DeleteMapping("/api/articles/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable("id") long id){
        blogService.delete(id);
        return ResponseEntity.ok()
                .build();
    }

    //글 수정(id로)
    @PutMapping("/api/articles/{id}")
    public ResponseEntity<Article> updateArticle(@PathVariable("id") long id,
                                                 @RequestBody UpdateArticleRequest request){
        Article updatedArticle = blogService.update(id, request);
        return ResponseEntity.ok()
                .body(updatedArticle);
    }
}
