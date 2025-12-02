//package me.book_ch8.config;
//
//import me.book_ch8.service.UserDetailService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.ProviderManager;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
//
//import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;
//
//@Configuration
//@EnableWebSecurity
//@RequiredArgsConstructor
//public class WebSecurityConfig {
//    private final UserDetailService userService;
//
//    //스프링 시큐리티 기능 비활성화
//    //인증, 인가 서비스를 모든 곳에 적용하지 않음.
//    // 정적 리소스(이미지,html파일)는 시큐리티 비활성화
//    @Bean
//    public WebSecurityCustomizer configure(){
//        return (web) -> web.ignoring() // 시큐리티 사용 비활성화
//                .requestMatchers(toH2Console()) // h2 데이터 확인하는 h2-console 제외
//                .requestMatchers(new AntPathRequestMatcher("/static/**")); // static 하위 리소스 제외
//    }
//
//    //특정 http 요청에 대한 웹 기반 보안 구성 : 해당 메서드에서 인증/인가 및 로그인, 로그아웃 관련 설정한다.
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
//        return http
//                .authorizeRequests(auth -> auth //인증, 인가 설정
//                        .requestMatchers(
//                                new AntPathRequestMatcher("/login"),
//                                new AntPathRequestMatcher("/signup"),
//                                new AntPathRequestMatcher("/user")
//                        ).permitAll() // login, signup, user로 요청오면 누구나 접근 가능하게 설정(인증/인가 없이도)
//                        .anyRequest().authenticated())
//                .formLogin(formLogin -> formLogin //폼 기반 로그인 설정
//                        .loginPage("/login")
//                        .defaultSuccessUrl("/articles")
//                )
//                .logout(logout -> logout // 로그아웃 설정
//                        .logoutSuccessUrl("/login")
//                        .invalidateHttpSession(true) // 로그아웃 이후 세션을 전체 삭제할지 여부
//                )
//                .csrf(AbstractHttpConfigurer::disable) // csrf 비활성화
//                .build();
//    }
//
//    //인증 관리자 관련 설정
//    //사용자 정보를 가져올 서비스를 재정희거나,
//    // 인증 방법, 예를 들어 LDAP, JDBC 기반 인증 등을 설정할 때 사용
//    @Bean
//    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder,
//                                                       UserDetailService userDetailService) throws Exception{
//        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//        authProvider.setUserDetailsService(userService); //사용자 정보 서비스 설정
//        authProvider.setPasswordEncoder(bCryptPasswordEncoder);
//        return new ProviderManager(authProvider);
//    }
//
//    //패스워드 인코더로 사용할 빈 등록
//    @Bean
//    public BCryptPasswordEncoder bCryptPasswordEncoder(){
//        return new BCryptPasswordEncoder();
//    }
//}
