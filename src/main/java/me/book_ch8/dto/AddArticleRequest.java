package me.book_ch8.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.book_ch8.domain.Article;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AddArticleRequest {
    public String title;
    public String content;

    //빌더를 통해 DTO를 엔티티로 만들어주는 메서드
    //블로그 글을 추가할 때 저장할 엔티티로 변환하는 용도로 사용
//    public Article toEntity(){
//        return Article.builder()
//                .title(title)
//                .content(content)
//                .build();
//    }

    // 10장 작성자 추가
    public Article toEntity(String author){
        return Article.builder()
                .title(title)
                .content(content)
                .author(author)
                .build();
    }
}
