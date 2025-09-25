package com.JtoP.Spring.boundedContext.answer.input;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnswerForm {
    @NotEmpty(message = "내용은 필수항목입니다.")
    @Size(max = 20000, message = "내용은 20000자 이하로 입력해주세요.")
    private String content;
}
