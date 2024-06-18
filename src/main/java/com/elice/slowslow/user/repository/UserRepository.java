package com.elice.slowslow.user.repository;

import com.elice.slowslow.user.User;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    Boolean existsByUsername(String username);

    List<User> findByDeletedIsFalse();

}
