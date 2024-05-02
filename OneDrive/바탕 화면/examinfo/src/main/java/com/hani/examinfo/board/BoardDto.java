package com.hani.examinfo.board;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardDto {
    @NotEmpty(message = "필수 항목입니다.")
    private String boardTitle;

    @NotEmpty(message = "필수 항목입니다.")
    private String description;
}
