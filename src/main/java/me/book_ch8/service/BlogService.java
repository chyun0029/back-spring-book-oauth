package me.book_ch8.service;

import lombok.RequiredArgsConstructor;
import me.book_ch8.domain.Article;
import me.book_ch8.dto.AddArticleRequest;
import me.book_ch8.dto.UpdateArticleRequest;
import me.book_ch8.repository.BlogRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor // final이나 @NotNull 붙은 필드의 생성자 추가
@Service
public class BlogService {
    private final BlogRepository blogRepository;

    //블로그 글 추가 메서드
//    public Article save(AddArticleRequest request){
//        return blogRepository.save(request.toEntity());
//    }

    // 유저 이름(author) 추가 입력 받는다
    public Article save(AddArticleRequest request, String userName){
        return blogRepository.save(request.toEntity(userName));
    }

    //전체 조회
    public List<Article> findAll(){
        return blogRepository.findAll();
    }

    //조회 1개(id로)
    public Article findById(long id){
        return blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found: " +  id));
    }

    //글 삭제(id로)
    public void delete(long id){
        //7장
//        blogRepository.deleteById(id);

        //10장
        Article article = blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found: " +  id));
        authorizeArticleAuthor(article);
        blogRepository.delete(article);
    }


    //글 수정(id로)
    @Transactional // 트랜잭션은 DB의 데이터를 바꾸기 위한 작업 단위를 말한다. (수정, 삭제 충돌 안되게)
    public Article update(long id, UpdateArticleRequest request){
        Article article = blogRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("not found: " + id));

        authorizeArticleAuthor(article); //10장 추가
        article.update(request.getTitle(), request.getContent());

        return article;
    }

    // 10장. 게시글을 작성한 유저인지 확인
    private static void authorizeArticleAuthor(Article article) {
       String userName = SecurityContextHolder.getContext().getAuthentication().getName();
       if(!article.getAuthor().equals(userName)){
           throw new IllegalArgumentException("not authorized");
       }
    }
}
