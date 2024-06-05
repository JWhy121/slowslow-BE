package com.elice.slowslow.user.dto;

import com.elice.slowslow.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MembershipDto {

    private String username;

    private String password;

    private String name;

    private String phoneNumber;

    private User.RoleType role;
}