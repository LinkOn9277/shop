package com.example.shop.repository;

import com.example.shop.entity.Members;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MembersRepository extends JpaRepository<Members, Long> {
    // email 로 정보 찾기 pk로 찾을 수 있지만 session 에는 기본적으로
    // email 을 저장되기 때문에
    public Members findByEmail(String email);



}
