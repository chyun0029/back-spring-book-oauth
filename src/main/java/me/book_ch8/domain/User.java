package me.book_ch8.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
public class User implements UserDetails { //UserDetails를 상속 받아 인증 객체로 사용
                                            //사용자의 인증 정보와 권한 정보를 저장하는 메서드 제공
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password")
    private String password;

//    @Builder
//    public User(String email, String password, String auth){
//        this.email = email;
//        this.password = password;
//    }

    @Override // 권한 반환
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("user"));
    }

    @Override // 사용자 id를 반환(고유한 값)
    public String getUsername() {
        return email;
    }

    @Override // 사용자 패스워드 반환
    public String getPassword() {
        return password;
    }
    @Override // 계정 만료 여부 반환
    public boolean isAccountNonExpired() {
        // 만료되었는지 확인하는 로직
        return true; // true -> 만료되지 않음
    }

    @Override // 계정 잠금 여부 반환
    public boolean isAccountNonLocked() {
        // 계정 잠금되었는지 확인하는 로직
        return true; // true -> 잠금되지 않음
    }

    @Override //패스워드의 만료 여부 반환
    public boolean isCredentialsNonExpired() {
        // 패스워드가 만료되었는지 확인하는 로직
        return true; // true -> 만료되지 않음
    }

    @Override // 계정 사용 가능 여부 반환
    public boolean isEnabled() {
        // 계정이 사용 가능한지 확인하는 로직
        return true; // true -> 사용 가능
    }

    /* OAuth2 서비스 구현하기
    사용자 정보를 조회해 users 테이블에
    사용자 정보가 있다면 리소스 서버에서 제공해주는 이름으로 업데이트
    사용자 정보가 없다면 users 테이블에 새 사용자를 생성해 데이터베이스에 저장
    */
    // 사용자 이름
    @Column(name="nickname", unique=true)
    private String nickname;

    // 생성자에 nickname 추가 (위의 기존 생성자는 주석처리)
    @Builder
    public User(String email, String password, String nickname){
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }

    // 사용자 이름 변경
    public User update(String nickname){
        this.nickname = nickname;
        return this;
    }

}
