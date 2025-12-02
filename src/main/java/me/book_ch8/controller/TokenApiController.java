package me.book_ch8.controller;

import lombok.RequiredArgsConstructor;
import me.book_ch8.dto.CreateAccessTokenRequest;
import me.book_ch8.dto.CreateAccessTokenResponse;
import me.book_ch8.service.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.templateparser.markup.HTMLTemplateParser;

@RequiredArgsConstructor
@RestController
public class TokenApiController {
    private final TokenService tokenService;

    @PostMapping("/api/token") // Post 요청오면 토큰 서비스에서 리프레시 토큰을 기반으로 새로운 액세스 토큰을 만들어 준다.
    public ResponseEntity<CreateAccessTokenResponse> createNewAccessToken
            (@RequestBody CreateAccessTokenRequest request){
        String newAccessToken = tokenService.createNewAccessToken(request.getRefreshToken());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CreateAccessTokenResponse(newAccessToken));
    }
}
