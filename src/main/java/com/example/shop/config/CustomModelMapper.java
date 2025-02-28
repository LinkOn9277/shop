package com.example.shop.config;

import org.modelmapper.ModelMapper;

public class CustomModelMapper extends ModelMapper {


    @Override // 파라미터 null 값 처리
    public <D> D map(Object source, Class<D> destinationType) {

        if (source == null){
            return null;
        }

        return super.map(source, destinationType);
    }
}
