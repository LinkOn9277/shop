package com.example.shop.service;

import com.example.shop.constant.OrderStatus;
import com.example.shop.dto.*;
import com.example.shop.entity.*;
import com.example.shop.exception.OutOfStockException;
import com.example.shop.repository.ItemRepository;
import com.example.shop.repository.MembersRepository;
import com.example.shop.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Log4j2
@Transactional
@RequiredArgsConstructor
public class OrderService { // 양방향

    private final OrderRepository orderRepository;

    private final MembersRepository membersRepository;

    private final ItemRepository itemRepository;

    // 상품
    public Long order(OrderDTO orderDTO , String email){
        // 참조 될 아이템 찾기
        Item item = itemRepository.findById(orderDTO.getItemId())
                .orElseThrow(EntityNotFoundException::new);

        // 참조 될 회원 찾기
        Members members = membersRepository.findByEmail(email);


        Orders orders = new Orders();
        // 부모인 orders set
        orders.setMembers(members);               // 누구의 주문
        orders.setOrderStatus(OrderStatus.ORDER); // 주문 상태

        // 담을 아이템 준비
        List<OrderItem> orderItemList = new ArrayList<>();

        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);                 // 입력받은 아이템
        orderItem.setCount(orderDTO.getCount()); // 입력받은 주문 수량
        orderItem.setOrderPrice(item.getPrice());

        orderItem.setOrders(orders);
        orderItemList.add(orderItem); // 다시 LIST로 담음

        if ( item.getStockNumber() - orderDTO.getCount() < 0){
            throw new OutOfStockException("상품 재고가 부족합니다. (현재수량 : " + item.getStockNumber() + ")");
        }

        // 주문수량 만큼 아이템 수량 변경
        item.setStockNumber(item.getStockNumber() - orderDTO.getCount()); // 주문한만큼 수량 변경


        orders.setOrderItems(orderItemList);

        Orders ordersA = orderRepository.save(orders);

        return ordersA.getId();
    }

    // 상품 주문내역
    public ResponesPageDTO getOrderList(String email, RequestPageDTO requestPageDTO){

        // 주문 목록
        Page<Orders> ordersPage = orderRepository.findOrders(email, requestPageDTO.getPageable("id"));

        // 주문목록 List
        List<Orders> ordersList = ordersPage.getContent();

//        ordersList.forEach(orders -> log.info(orders));
        // 주문목록 DTO 변환
        List<OrderHistDTO> orderHistDTOList = new ArrayList<>();

        for (Orders orders : ordersList){
            // 뷰페이지로 가는 객체 DTOList
            OrderHistDTO orderHistDTO = new OrderHistDTO(orders);

            // 주문 아이템들을 꺼내와서 DTO
            List<OrderItem> orderItemList = orders.getOrderItems();
            for (OrderItem entity : orderItemList){

                // 주문아이템의 아이템에 달려 있는 이미지들을 가져와서
                List<ImgEntity> imgEntityList = entity.getItem().getImgEntityList();

                for (ImgEntity imgEntity : imgEntityList){
                    // 대표이미지 라면 orderItemDTO로 변환
                    if (imgEntity.getRepimgYn() != null && imgEntity.getRepimgYn().equals("Y")){
                        OrderItemDTO orderItemDTO
                                = new OrderItemDTO(entity , imgEntity.getImgName());
                        orderHistDTO.addOrderItemDTO(orderItemDTO);

                    }
                }
            }
            orderHistDTOList.add(orderHistDTO);
        }
        return new ResponesPageDTO(requestPageDTO, orderHistDTOList, (int) ordersPage.getTotalElements());

    }




}
