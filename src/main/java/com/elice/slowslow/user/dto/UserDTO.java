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
    private String userName;
//    private String userId;
    private String userEmail;
    private String userPassword;
    private String userPhoneNumber;

    public static UserDTO toUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUserEmail(user.getEmail());
        userDTO.setUserPassword(user.getPassword());
        userDTO.setUserName(user.getName());
        userDTO.setUserPhoneNumber(user.getPhoneNumber());
        return userDTO;
    }
}
