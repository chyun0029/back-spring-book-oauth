package me.book_ch8.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class) // 7장 219pg. 엔티티의 생성 및 수정 시간을 자동으로 감시하고 기록
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//기본키 자동 1씩 증가
    @Column(name="id", updatable = false)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate // 엔티티가 수정될 떄 수정 시간 저장
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

//    @Builder // 빌더 패턴으로 객체 생성(가독성 높) 174pg
//    public Article(String title, String content) {
//        this.title = title;
//        this.content = content;
//    }

    public void update(String title, String content){
        this.title = title;
        this.content = content;
    }

    // 10장 글에 글쓴이 추가하가ㅣ 327pg
    @Column(name = "author", nullable = false)
    private String author;

    @Builder
    public Article(String author, String title, String content) {
        this.author = author;
        this.title = title;
        this.content = content;
    }

}
