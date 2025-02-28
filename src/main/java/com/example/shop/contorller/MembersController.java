package com.example.shop.contorller;

import com.example.shop.dto.MembersDTO;
import com.example.shop.service.MembersService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/user")
public class MembersController {

    private final MembersService membersService;

    @GetMapping("/signUp")
    public String singUp(MembersDTO membersDTO){
        log.info("회원가입 get 진입 : " + membersDTO);
        log.info("회원가입 get 진입 : " + membersDTO);

        return "user/signUp";
    }

    @PostMapping("/signUp")
    public String singUpPost(@Valid MembersDTO membersDTO, BindingResult bindingResult){
        log.info("회원가입 Post 진입 : " + membersDTO);
        log.info("회원가입 Post 진입 : " + membersDTO);

        if (bindingResult.hasErrors()){

            log.info("유효성 검사한 에러 발생!!!!");
            log.info(bindingResult.hasErrors());

            return "user/signUp";
        }


        try {
            membersService.signUp(membersDTO);
        }catch (IllegalStateException e){
            return "user/signUp";
        }
        log.info("회원가입 Post 종료");
        log.info("회원가입 Post 종료");
        return "redirect:/user/signUp";
    }

    @GetMapping("/login")
    public String login(){
        log.info("login 페이지 진입");


        return "user/login";
    }






}
