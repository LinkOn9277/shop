package com.example.shop.contorller;

import com.example.shop.dto.CartItemDTO;
import com.example.shop.service.CartService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Log4j2
public class CartController {

    private final CartService cartService;

    @PostMapping("/cart")
    public ResponseEntity order(@Valid CartItemDTO cartItemDTO,
                                BindingResult bindingResult , Principal principal){

        log.info("들어온 값 : " + cartItemDTO);

        if (bindingResult.hasErrors()){
            log.info("장바구니 유효성 검사 : " + bindingResult.getAllErrors());

            List<FieldError> fieldErrorList =bindingResult.getFieldErrors();

            StringBuilder stringBuilder = new StringBuilder();

            for (FieldError error : fieldErrorList){
                // stringBuilder 객체 에러의 메시지를 담는다.
                stringBuilder.append(error.getDefaultMessage());
            }
            return new ResponseEntity<String>(stringBuilder.toString() , HttpStatus.BAD_REQUEST);
        }

        // 로그아웃이 되어서 principal 가 null
        if (principal == null){
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        String email = principal.getName();
        Long cartItemId = null;

        try {
            cartItemId = cartService.addCart(cartItemDTO , email);
        }catch (EntityNotFoundException e){
            return new ResponseEntity<String>(e.getMessage() , HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<Long>(cartItemId , HttpStatus.OK);
    }



}
