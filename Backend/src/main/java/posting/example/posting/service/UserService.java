package posting.example.posting.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import posting.example.posting.domain.Role;
import posting.example.posting.domain.User;
import posting.example.posting.repository.UserRepository;
import posting.example.posting.service.dto.UserDto;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepo;

    // 회원 가입
    @Transactional
    public User register(@Valid UserDto dto){
        // 중복 사용자 체크
        if (userRepo.findByUsername(dto.getUsername()).isPresent()) {
            throw new IllegalArgumentException("이미 사용 중인 사용자 이름입니다.");
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(encoder.encode(dto.getPassword()));
        user.setRole(Role.USER);

        return userRepo.save(user);
    }

    // 사용자 조회 (로그인 처리)
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(userRepo.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(username + " 존재하지 않는 사용자 이름입니다.")));
    }
}
