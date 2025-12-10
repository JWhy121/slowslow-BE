package com.elice.slowslow.domain.user;

import com.elice.slowslow.global.audit.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Table(name = "user")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Builder
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    @Email(message = "이메일 형식이 유효하지 않습니다.")
    private String username;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false, length = 20)
    private String name;

    @Column(length = 50)
    private Provider provider;

    @Column(name = "phone_number", nullable = false, length = 50)
    @Pattern(regexp = "^\\d+$", message = "전화번호는 숫자 형식으로 입력해주세요.")
    private String phoneNumber;

    @Column(name = "deleted")
    private boolean deleted = false;

    @Enumerated(EnumType.STRING)
    private RoleType role = RoleType.ROLE_USER;

    public enum RoleType {
        ROLE_ADMIN, ROLE_USER
    }

    public enum Provider {
        KAKAO, GOOGLE, NAVER
    }

}
