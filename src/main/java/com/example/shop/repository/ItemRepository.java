package com.example.shop.repository;

import com.example.shop.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> , ItemSearch {

    public List<Item> findByItemNm(String itemNm); // 읽기 가능 조건 : 상품명으로 검색

    public List<Item> findByPriceGreaterThanEqual(int price); // 가격으로 검색

    public List<Item> findByItemNmOrItemDetail(String itemNm, String Detail); // 상품명과 상품 상세설명으로 검색

    public List<Item> findByCreateBy(String email); // 자신이 판매하고 있는 상품목록보기

    public List<Item> findByItemNmContainingOrItemDetailContaining(String itemNm, String Detail); // Like문 이용해서 상품명 or 상세설명 포함된 글자 해당되는 제품찾기

    @Query("select i from Item  i where i.itemNm like '%'||:itemNm||'%' or i.itemDetail like concat('%', :itemDetail,'%') ")
    public List<Item> selectItemNmItemDetail(String itemNm, String itemDetail); // 상동

    @Query("select i from Item  i where i.itemNm like '%'||:keyword||'%' or i.itemDetail like concat('%', :keyword,'%') ")
    public List<Item> selectItemNmItemDetail( String keyword); // 상동


}
