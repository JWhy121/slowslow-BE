package com.elice.slowslow.user.repository;

import com.elice.slowslow.user.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    //이메일로 회원 정보 조회(select * from user where email=?)
    Optional<User> findByEmail(String email);
}
