package com.example.shop.service;

import com.example.shop.dto.CartDetailDTO;
import com.example.shop.dto.CartItemDTO;
import com.example.shop.entity.Cart;
import com.example.shop.entity.CartItem;
import com.example.shop.entity.Item;
import com.example.shop.entity.Members;
import com.example.shop.repository.CartItemRepository;
import com.example.shop.repository.CartRepository;
import com.example.shop.repository.ItemRepository;
import com.example.shop.repository.MembersRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class CartService {

    private final ItemRepository itemRepository;
    private final MembersRepository membersRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    public Long addCart(CartItemDTO cartItemDTO , String email){
        // 장바구니에 담을 때 기본적으로 아이템을 참조하는 장바구니 아이템들을 장바구니에 넣는다.
        // 장바구니는 회원과 1:1 관계

        // 내가 사는 아이템 찾기
        Item item = itemRepository.findById(cartItemDTO.getItemId())
                .orElseThrow(EntityNotFoundException::new);

        // 내가가 누구
        Members members = membersRepository.findByEmail(email);

        // 내 장바구니 찾기
        Cart cart = cartRepository.findByMembersEmail(email);

        if (cart == null){
            Cart saveCart = new Cart();

            // 장바구니가 없다면 만들어주자
            saveCart.setMembers(members);
            cart = cartRepository.save(saveCart);
        }
        // 장바구니에 담겨진 아이템 찾기
        CartItem cartItem = cartItemRepository.findByCartIdAndItemId(cart.getId() , item.getId());

        // 장바구니에 아이템이 담겨있지않다면
        if (cartItem == null){

            CartItem saveCartItem = new CartItem();
            saveCartItem.setCart(cart);                     // 장바구니
            saveCartItem.setItem(item);                     // 장바구니에 담길 아이템
            saveCartItem.setCount(cartItemDTO.getCount());  // 수량
            // 아이템 저장
            cartItem = cartItemRepository.save(saveCartItem);

            return cartItem.getId();

        }else {

            // 장바구니에 동일한 아이템이 있다면
            // 장바구니 아이템의 수량을 기존수량 + 입력받은 수량으로 수정해준다.
            cartItem.setCount(
                    cartItem.getCount() + cartItemDTO.getCount()

            );

            return cartItem.getId();
        }

    }

    // 주문목록
    public List<CartDetailDTO> CartList(String email){

        List<CartDetailDTO> cartDetailDTOList = cartItemRepository.findByCartDetailDTOList(email);

        return cartDetailDTOList;

    }




}
