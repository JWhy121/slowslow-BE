package com.elice.slowslow.user.dto;

import com.elice.slowslow.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class UserDTO {
    private Long id;
    private String name;
    private String username;
    private String userPassword;
    private String userPhoneNumber;
}
