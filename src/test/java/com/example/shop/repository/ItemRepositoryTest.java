package com.example.shop.repository;

import com.example.shop.constant.ItemSellStatus;
import com.example.shop.entity.Item;
import com.example.shop.entity.QItem;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryFactory;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.JPQLQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Log4j2
class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    @PersistenceContext // EntityManager 빈을 주입 함
    EntityManager em;

    // 데이터 베이스 테스트
    // 상품등록 기능을 만들기 위한 테스트
    // 상품등록을 잘하는지 읽기 목록 등을 테스트하기 위해서
    // 더미값 만들기

    @Test // 등록
    public void insertTest(){
        log.info("테스트 진입");
        // save(); 저장
        // entity 저장, insert into table(컬럼명,컬럼명,컬럼명) values(값,값,값)
        for (int i = 0; i < 500; i++){
            Item item = new Item();

            item.setItemNm("테스트 상품["+ i +"]");
            item.setPrice(20000 + i);
            item.setItemDetail("이 상품은 머얼리 영국에서부터 시작되어...["+ i +"]");
            item.setStockNumber(100 + i);
            item.setItemSellStatus(ItemSellStatus.SELL);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            // 만들어진 더미객체를
            // entity로 만들어진 객체임으로 repository를 통해 저장이 가능하다

            Item result =
                    itemRepository.save(item);
            log.info("테스트 종료" + result);
        }



    }

    @Test // 조회
    public void readTest(){
        // 상품을 검색하자
        // 자신의 테이블에 있는 pk번호 사용할 것
        // select * from item where item_id = :pk번호;
        // findById(); 동일하다
        Optional<Item> optionalItem =
            itemRepository.findById(10L);

        // 예외처리 : 아이템의 pk번호가 없어서 못찾을 경우에 대해서
        try {
            Item item =
                optionalItem.orElseThrow(EntityNotFoundException::new);
            log.info("있음 : " + item);
        }catch (EntityNotFoundException e){
            log.info("아이템이 존재하지 않습니다.");
            log.info("테스트 종료");
            e.printStackTrace();
        }

    }

    @Test
    public void findByItemTest(){
        log.info("테스트 진입");
        // 상품명으로 검색
        // repository에 만든 기능
        // findByItemNm을 테스트
        // select * from item where item_nm = :파라미터;
        // select i from item i
        //      i.itemNm = :파라미터;
        String itemNm = "테스트 상품[0]";

        List<Item> itemList =
        itemRepository.findByItemNm(itemNm);

        itemList.forEach(item -> log.info(item));
        log.info("테스트 종료");
    }

    @Test
    public void priceFindTest(){
        // 입력한 가격보다 큰 금액의 상품 찾기
        log.info("테스트 진입");
        int price = 20000;

        List<Item> itemList =
        itemRepository.findByPriceGreaterThanEqual(price);

        itemList.forEach(item -> log.info(item));
        log.info("테스트 종료");
    }

    @Test
    public void findByItemNmOrItemDetailTest(){
        // 입력한 상품명 또는 상품 설명으로 찾기
        log.info("테스트 진입");
        String keyword = "테스트 상품[0]";

        List<Item> itemList =
            itemRepository.findByItemNmOrItemDetail(keyword, keyword);

        itemList.forEach(item -> log.info(item));
        log.info("테스트 종료");
    }

    @Test
    public void findByItemNmContainingOrItemDetailContainingTest(){
        // 입력한 상품명 또는 상품 설명으로 찾기
        log.info("테스트 진입");
        String keyword = "테스트 상품[0]";

        List<Item> itemList =
                // itemRepository.findByItemNmOrItemDetail(keyword, keyword);
                // itemRepository.selectItemNmItemDetail(keyword, keyword); // like문
                itemRepository.selectItemNmItemDetail(keyword); // like문

        itemList.forEach(item -> log.info(item));
        log.info("테스트 종료");
    }

    @Test
    @DisplayName("수정 테스트")
    public void updateTest(){
        // @Transactional 미사용
        // 조건은 있는 번호 pk를 지정
        log.info("테스트 집입");
//        Item item = new Item();
//        item.setId(503L); // 지정한 번호를
//        item.setItemNm("상품명 입력수정"); // 으로 변환
//        item.setPrice(30000); // 으로 변환
//        위 구문은 null 문제가 생기니
//        상품을 먼저 찾아온다.
        Item item =
                itemRepository.findById(503L).get();

        item.setItemNm("상품명 입력수정"); // 으로 변환
        item.setPrice(50000); // 으로 변환

        log.info("저장하려는 Entity" + item);

        // 저장하면 지정한 번호로 itemEntity의 객체의 값으로 수정 됨
//        Item result =
//        itemRepository.save(item);

//        log.info(result);
        log.info("테스트 종료");
    }

    @Test
    @Transactional
    @Rollback(value = false)
    public void updateTest2(){
        // @Transactional 사용
        log.info("테스트 집입");
        Item item =
                itemRepository.findById(10L).get();

        item.setItemNm("상품명 수정"); // 으로 변환
        item.setPrice(50000); // 으로 변환

        log.info("저장하려는 Entity" + item);
        log.info("테스트 종료");
    }

    @Test // 삭제
    public void delTest(){
        itemRepository.deleteById(503L);
    }

    @Test
    public void qdslTest(){ // 쿼리DSL
        log.info("테스트 진입");
        JPQLQueryFactory queryFactory = new JPAQueryFactory(em);

        // Q도메인을 사용
        QItem qItem = QItem.item; // select * from item

        // 입력받을 검색타입
        String types = "nd"; // 이름 n과 상세설명d
        String keyword = "1";
        String[] type = types.split("");
        log.info("type에 담겨있는 검색 타입들");
        log.info(Arrays.toString(type));

        JPQLQuery<Item> jpqlQuery =
                queryFactory.selectFrom(qItem);

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        for (String typeA : type){
            if (typeA.equals("n")){
                booleanBuilder.or(qItem.itemNm.like("%" + keyword + "%"));
            }else if (typeA.equals("d")){
                booleanBuilder.or(qItem.itemDetail.like("%" + keyword + "%"));
            }
        }
        log.info(booleanBuilder.toString());
        log.info(booleanBuilder.toString());
        log.info(booleanBuilder.toString());
        jpqlQuery.where(booleanBuilder);

        List<Item> itemList = jpqlQuery.fetch(); // 실행

        log.info("검색된 게시물 수 : " + jpqlQuery.fetchCount());

        itemList.forEach(item -> log.info(item));
        log.info("테스트 종료");
    }

    @Test // 판매자 테스트
    public void selectSellerTest(){
        // 상품판매자 이메일
        String email = "q@q.q";

        List<Item> itemList =
                itemRepository.findByCreateBy(email);

        itemList.forEach(a -> log.info(a));

    }


}