package com.example.shop.repository;

import com.example.shop.entity.Cart;
import com.example.shop.entity.Members;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Log4j2
class CartRepositoryTest {

    @Autowired
    CartRepository cartRepository;
    @Autowired
    MembersRepository membersRepository;


    @Test
    public void insertTest(){
        // 부모인 회원 테이블 → 특정 회원을 찾아 set 해줌
        // 그걸 통해서 부모를 가지고 있게 됨.
        // Members members = membersRepository.findByEmail("hong@a.a");
        Members members = membersRepository.findById(1L).get();
        // 참조 값을 넣으면 참조하고 없으면 null 들어감
        Cart cart = new Cart();
        cart.setMembers(members);
        cartRepository.save(cart);
    }

    @Test
    public void selectEmail(){
        // 부모인 회원 테이블 → 특정 회원을 찾아 set 해줌
        // 그걸 통해서 부모를 가지고 있게 됨.
        Cart cart = cartRepository.selectEmail("hong@a.a");
        if (cart == null){
            log.info("장바구니 생성 가능");
        }else {
            log.info("장바구니 못만듦");
        }
    }

    @Test
    @Transactional
    public void findById(){
        Cart cart = cartRepository.findById(1L).get();
        log.info(cart);
    }



}