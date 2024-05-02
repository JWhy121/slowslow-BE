package com.hani.examinfo.comment;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDto {


    @NotEmpty(message = "필수 항목입니다.")
    private String author;

    @NotEmpty(message = "필수 항목입니다.")
    private String content;
}
