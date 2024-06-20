package com.elice.slowslow.user.dto;

import com.elice.slowslow.user.User;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MembershipDTO {

    @NotBlank(message = "사용자 이름은 필수 입력 항목입니다.")
    @Size(max = 50, message = "사용자 이름은 최대 50자까지 입력 가능합니다.")
    private String username;

    @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
    @Size(min = 8, max = 100, message = "비밀번호는 최소 8자, 최대 100자까지 입력 가능합니다.")
    private String password;

    @NotBlank(message = "이름은 필수 입력 항목입니다.")
    @Size(max = 20, message = "이름은 최대 20자까지 입력 가능합니다.")
    private String name;

    @NotBlank(message = "전화번호는 필수 입력 항목입니다.")
    @Pattern(regexp = "^\\d+$", message = "전화번호는 숫자 형식으로 입력해주세요.")
    @Size(max = 50, message = "전화번호는 최대 50자까지 입력 가능합니다.")
    private String phoneNumber;
}