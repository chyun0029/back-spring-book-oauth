package me.book_ch8.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.book_ch8.domain.Article;
import me.book_ch8.domain.User;
import me.book_ch8.dto.AddArticleRequest;
import me.book_ch8.dto.UpdateArticleRequest;
import me.book_ch8.repository.BlogRepository;
import me.book_ch8.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.script.ScriptContext;
import java.security.Principal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest // 테스트용 애플리케이션 컨택스트
@AutoConfigureMockMvc // MockMvc 생성 및 자동 구성
class BlogApiControllerTest {
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper; //직렬화, 역직렬화를 위한 클래스

    @Autowired
    private WebApplicationContext context;

    @Autowired
    BlogRepository blogRepository;

    @Autowired // 10장
    UserRepository userRepository;
    User user;

    @BeforeEach //테스트 실행 전 실행하는 메서드
    public void mockMvcSetUp(){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
        blogRepository.deleteAll();
    }

    @BeforeEach // 10장
    void setSecurityContext(){
        userRepository.deleteAll();
        user = userRepository.save(User.builder()
                .email("user@gmail.com")
                .password("test")
                .build());
        SecurityContext context = SecurityContextHolder.getContext();
        //인증 객체를 저장하는 시큐리티 컨텍스트에 setAuthentication() 메서드로 테스트 유저를 지정
        context.setAuthentication(new UsernamePasswordAuthenticationToken(
                user,user.getPassword(), user.getAuthorities()));
    }

    //////////////////////////////////////////////////////////
    @DisplayName("addArticle: 블로그 글 추가에 성공한다.")
    @Test
    public void addArticle() throws Exception{
        //given : 블로그 글 추가에 필요한 요청 객체를 만든다.
        final String url = "/api/articles";
        final String title = "title";
        final String content = "content";
        final AddArticleRequest userRequest = new AddArticleRequest(title, content);

        //객체를 JSON으로 직렬화
        final String requestBody = objectMapper.writeValueAsString(userRequest);

        // 10장
        Principal principal = Mockito.mock(Principal.class);
        Mockito.when(principal.getName()).thenReturn("username");

        //when : 블로그 글 추가 API에 요청을 보낸다.
        //       요청 타입은 JSON, given절에서 미리 만들어둔 객체를 요청 본문으로 함께 보낸다.
        //설정한 내용을 바탕으로 요청 전송
        //mockMvc로 HTTP 메서드, URL, 요청 본문 요청 타입 설정한 내용을 바탕으로 테스트 요청 보낸다
        ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON) // JSON타입으로 요청
                .principal(principal) // 10장 추가
                .content(requestBody));

        //then : 응답 코드가 201 Created인지 확인.
        //       블로그를 전체 조회해 크기가 1인지 확인하고, 실제 저장된 데이터와 요청 값을 비교한다.
        result.andExpect(status().isCreated());
        List<Article> articles = blogRepository.findAll();
        assertThat(articles.size()).isEqualTo(1); // 블로그 글 개수가 1인지 확인
        assertThat(articles.get(0).getTitle()).isEqualTo(title);
        assertThat(articles.get(0).getContent()).isEqualTo(content);
    }

    //////////////////////////////////////////////////////////
    @DisplayName("findAllArticles: 블로그 글 목록 조회에 성공한다.")
    @Test
    public void findAllArticles() throws Exception{
        //given : 블로그 글을 저장
        final String url = "/api/articles";

        /*
        final String title = "title";
        final String content = "content";
        blogRepository.save(Article.builder()
                .title(title)
                .content(content)
                .build()); */

        // 10장. 제목, 내용 등을 함수로 생성해서 기존 코드 대치
        Article savedArticle = createDefaultArticle();


        //when : 목록 조회 API를 호출
        final ResultActions resultActions = mockMvc.perform(get(url)
                .accept(MediaType.APPLICATION_JSON));

        //then : 응답 코드가 200 OK이고, 반환받은 값 중에 0번째 요소의 content와 title이 저장된 값과 일치하는지 확인
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].content").value(savedArticle.getContent())) //.value(content)
                .andExpect(jsonPath("$[0].title").value(savedArticle.getTitle())); //.value(title)));
    }


    //////////////////////////////////////////////////////////
    @DisplayName("findArticles: 블로그 글 1개 조회에 성공한다.")
    @Test
    public void findArticle() throws Exception{
        //given : 블로그 글을 저장
        final String url = "/api/articles/{id}";
        /*
        final String title = "title";
        final String content = "content";
        Article savedArticle = blogRepository.save(Article.builder()
                .title(title)
                .content(content)
                .build());*/

        // 10장. 제목, 내용 등을 함수로 생성해서 기존 코드 대치
        Article savedArticle = createDefaultArticle();

        //when : 저장한 블로그 글의 id값으로 API를 호출
        final ResultActions resultActions = mockMvc.perform(get(url, savedArticle.getId()));

        //then : 응답 코드가 200 OK이고, 반환받은 content와 title이 저장된 값과 일치하는지 확인
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value(savedArticle.getContent()))
                .andExpect(jsonPath("$.title").value(savedArticle.getTitle()));
    }

    //////////////////////////////////////////////////////////
    @DisplayName("deleteArticle: 블로그 글 삭제에 성공한다.")
    @Test
    public void deleteArticle() throws Exception{
        //given : 블로그 글을 저장
        final String url = "/api/articles/{id}";
        /*
        final String title = "title";
        final String content = "content";
        Article savedArticle = blogRepository.save(Article.builder()
                .title(title)
                .content(content)
                .build());*/

        // 10장. 제목, 내용 등을 함수로 생성해서 기존 코드 대치
        Article savedArticle = createDefaultArticle();

        //when : 저장한 블로그 글의 id값으로 삭제 API를 호출
        final ResultActions resultActions = mockMvc.perform(delete(url, savedArticle.getId()))
                .andExpect(status().isOk());

        //then : 응답 코그다 200 OK이고,
        //       블로그 글 리스트를 전체 조회해 조회한 배열 크기가 0인지 확인
        List<Article> articles = blogRepository.findAll();
        assertThat(articles).isEmpty(); // 1개 넣고 삭제해서 총 0개.
    }

    //////////////////////////////////////////////////////////
    @DisplayName("updateArticle: 블로그 글 수정에 성공한다.")
    @Test
    public void updateArticle() throws Exception{
        //given : 블로그 글을 저장하고, 블로그 글 수정에 필요한 요청 객체를 만든다
        final String url = "/api/articles/{id}";
//        final String title = "title";
//        final String content = "content";
//        Article savedArticle = blogRepository.save(Article.builder()
//                .title(title)
//                .content(content)
//                .build());

        // 10장. 제목, 내용 등을 함수로 생성해서 기존 코드 대치
        Article savedArticle = createDefaultArticle();

        final String newTitle = "new title";
        final String newContent = "new content";
        final UpdateArticleRequest request = new UpdateArticleRequest(newTitle, newContent);


        //then : UPDATE API로 수정 요청을 보낸다.
        //       요청 타입은 JSON이며, given절에서 만든 객체를 요청 본문으로 함께 보낸다
        final ResultActions result = mockMvc.perform(put(url, savedArticle.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request)));

        //when : 응답 코드가 200 OK인지 확인한다.
        //       블로그 글 id로 조회한 후 값이 수정되었는지 확인한다.
        result.andExpect(status().isOk());
        Article article = blogRepository.findById(savedArticle.getId()).get();
        assertThat(article.getTitle()).isEqualTo(newTitle);
        assertThat(article.getContent()).isEqualTo(newContent);
    }

    // 중복 코드들 메서드로 추출
    private Article createDefaultArticle() {
        return blogRepository.save(Article.builder()
                .title("title")
                .author(user.getUsername())
                .content("content")
                .build());
    }
}
