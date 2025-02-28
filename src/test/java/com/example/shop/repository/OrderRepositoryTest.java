package com.example.shop.repository;

import com.example.shop.config.CustomModelMapper;
import com.example.shop.constant.OrderStatus;
import com.example.shop.dto.ImgDTO;
import com.example.shop.dto.ItemDTO;
import com.example.shop.entity.*;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Log4j2
class OrderRepositoryTest {

    @Autowired
    OrderRepository orderRepository;
    @Autowired
    MembersRepository membersRepository;
    @Autowired
    OrderItemRepository orderItemRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    ImgRepository imgRepository;
    @Autowired
    ModelMapper modelMapper;


    @Test
    public void insertTest(){
        // 주문을 등록하려면 부모인 맴버 필요
        Members members = membersRepository.findByEmail("hong@a.a");

        Orders orders = new Orders();
        orders.setMembers(members); // 참조하는 테이블
        orders.setOrderStatus(OrderStatus.ORDER);

        orderRepository.save(orders);
    }

    @Test
    public void orderIteminsertTest(){
        for (int i = 0; i < 50000; i++){
            // 어떤 아이템을 샀는가
            Item itemA = itemRepository.findById(546L).get();

            // 누구를 참조하는가
            Orders orders = orderRepository.findById(1L).get();

            OrderItem item = new OrderItem();   // 참조하는 부모 엔티티 추가
            item.setOrders(orders);
            item.setItem(itemA);
            item.setCount(2);
            item.setOrderPrice(itemA.getPrice() * item.getCount());

            orderItemRepository.save(item);
        }
    }

    @Test
    @Transactional
    public void readOrdersTest(){
        // 양방향 OneToMany
        Orders orders = orderRepository.findById(1L).get();

    }

    @Test
    @Transactional
    @Rollback(value = false)
    public void cascadeTest(){
        // 주문을 등록하려면 부모인 맴버 필요
        Members members = membersRepository.findByEmail("hong@a.a");

        Orders orders = new Orders();
        orders.setMembers(members); // 참조하는 테이블
        orders.setOrderStatus(OrderStatus.ORDER);

        // 주문테이블에 주문 아이템을 같이 가져간다.
        Item itemA = itemRepository.findById(546L).get();
        OrderItem orderItem = new OrderItem();
        orderItem.setOrders(orders);
        orderItem.setItem(itemA);
        orderItem.setCount(2);
        orderItem.setOrderPrice(itemA.getPrice() * orderItem.getCount());

        // 부모 없음
        List<OrderItem> orderItemList = new ArrayList<>();
        orderItemList.add(orderItem); // 상품추가

        // 부몬엔티티에 자식 엔티티를 넣어준다.
        orders.setOrderItems(orderItemList);

        // 부모만 저장한다.
        orderRepository.save(orders);
    }

    @Test
    @Transactional
    public void mapperTest(){

        ModelMapper modelMapper = new ModelMapper();
        // modelMapper.map(null , ItemDTO.class);

        ModelMapper modelMapperA = new CustomModelMapper();
        // ItemDTO itemDTO = modelMapperA.map(null , ItemDTO.class);

        ImgEntity imgEntity = imgRepository.findById(13L).get();
        ImgDTO imgDTO = modelMapper.map(imgEntity , ImgDTO.class);

        log.info(imgDTO);

    }

    @Test
    public void mapper2Test(){

        modelMapper.map(null , ImgDTO.class);

    }

    @Test
    @Transactional
    public  void findOrdersTest(){

        Pageable pageable = PageRequest.of(0,10);

        Page<Orders> ordersPage = orderRepository.findByMembersEmail("hong@a.a",pageable);

        ordersPage.forEach(orders -> log.info(orders));

    }



}