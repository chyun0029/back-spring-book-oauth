package me.book_ch8.config.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties("jwt") // 자바 클래스에 속성값을 가져와서 사용하는 어노테이션
//application.yml의 issuer, secret_key 값들을 변수로 접근하는데 사용할 클래스
public class JwtProperties {
    private String issuer;
    private String secretKey;
}
