package com.example.shop.dto;

import com.example.shop.constant.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class MembersDTO {

    private Long num;

    @NotBlank(message = "이름은 필수 입력입니다.")
    @Length(min = 2, max = 20, message = "이름은 2자이상, 20자 이하로 입력해주세요.")
    private String name;

    @NotBlank
    @Email(message = "이메일 형식으로 입력해주세요.")
    private String email; // 이메일은 유일하다

    @NotBlank(message = "비밀번호는 필수 입력값 입니다.")
    @Length(min = 8, max = 16, message = "비밀번호는 8자 이상, 16자 이하로 입력해주세요.")
    private String password;

    @NotBlank(message = "주소는 필수 입력값 입니다.")
    private String address;

    private Role role;
}
