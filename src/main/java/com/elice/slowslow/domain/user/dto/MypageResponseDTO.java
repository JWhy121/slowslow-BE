package com.elice.slowslow.domain.user.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MypageResponseDTO {

    private Long id;

    private String username;

    private String name;

    private String phoneNumber;
}