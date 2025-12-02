package me.book_ch8.service;

import lombok.RequiredArgsConstructor;
import me.book_ch8.config.jwt.TokenProvider;
import me.book_ch8.domain.User;
import org.springframework.stereotype.Service;

import java.time.Duration;

@RequiredArgsConstructor
@Service
public class TokenService {
    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;

    // 전달 받은 리프레시 토큰으로 토큰 유효성 검사 진행하고,
    // 유효한 토큰일 떄 리프레시 토큰으로 사용자 ID를 찾는 메서드
    public String createNewAccessToken(String refreshToken){
        // 토큰 유효성 검사에 실패하면 예외 발생
        if(!tokenProvider.validToken(refreshToken)){
            throw new IllegalArgumentException("Unexpected token");
        }
        Long userId = refreshTokenService.findByRefreshToken(refreshToken).getUserId();
        User user = userService.findById(userId);

        // 사용자 ID로 사용자를 찾은 후에 토큰 제공자의 generateToken() 메서드를 호출해 새로운 액세스 토큰을 생성
        return tokenProvider.generateToken(user, Duration.ofHours(2));
    }

}
