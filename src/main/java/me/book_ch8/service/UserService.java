package me.book_ch8.service;

import lombok.RequiredArgsConstructor;
import me.book_ch8.domain.User;
import me.book_ch8.dto.AddUserRequest;
import me.book_ch8.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
//    private final BCryptPasswordEncoder bCryptPasswordEncoder;
//
//    //AddUserRequest 객체를 인자로 받는 회원 정보 추가 메서드
//    public Long save(AddUserRequest dto){
//        return userRepository.save(User.builder()
//                .email(dto.getEmail())
//                .password(bCryptPasswordEncoder.encode(dto.getPassword())) //패스워드 암호화
//                //패스워드 저장할 때 시큐리티를 설정하며 패스워드 인코딩용으로 등록한 빈을 사용해서 암호화한 후에 저장
//                .build()).getId();
//    }
//
//    // 전달 받은 유저 ID로 유저를 검색해서 전달하는 메서드
//    public User findById(Long userId){
//        return userRepository.findById(userId)
//                .orElseThrow(() -> new IllegalArgumentException("Unexpected token"));
//    }

    // 10장 (323pg)
    public Long save(AddUserRequest dto){
        // BCryptPasswordEncoder를 생성자를 사용해 직접 생성해서
        // 패스워드를 암호화할 수 있게 코드를 수정함
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return userRepository.save(User.builder()
                .email(dto.getEmail())
                .password(encoder.encode(dto.getPassword()))
                .build()).getId();
    }

    public User findById(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
    }

    public User findByEmail(String email){ // OAuth2에서 제공하는 이메일은 유일 값이므로 해당 메서드로 유저 찾을 수 있음
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
    }
}
