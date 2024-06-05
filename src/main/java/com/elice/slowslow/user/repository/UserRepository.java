package com.elice.slowslow.user.repository;

import com.elice.slowslow.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    //이메일로 회원 정보 조회(select * from user where email=?)
    Optional<User> findByEmail(String email);

}
