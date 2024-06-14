package com.elice.slowslow.user.dto;

import com.elice.slowslow.user.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MembershipDTO {

    private String username;

    private String password;

    private String name;

    private String phoneNumber;
}