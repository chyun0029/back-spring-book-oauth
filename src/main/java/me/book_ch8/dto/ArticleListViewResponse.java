package me.book_ch8.dto;

import lombok.Getter;
import me.book_ch8.domain.Article;

//뷰에게 데이터를 전달하기 위한 객체
@Getter
public class ArticleListViewResponse {
    private final Long id;
    private final String title;
    private final String content;

    public ArticleListViewResponse(Article article) {
        this.id = article.getId();
        this.title = article.getTitle();
        this.content = article.getContent();
    }

}
