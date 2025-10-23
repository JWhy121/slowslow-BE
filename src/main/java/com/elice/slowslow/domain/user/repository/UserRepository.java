package com.elice.slowslow.domain.user.repository;

import com.elice.slowslow.domain.user.User;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    Boolean existsByUsername(String username);

    List<User> findByDeletedIsFalse();

}
