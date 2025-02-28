package com.example.shop.service;

import com.example.shop.dto.ImgDTO;
import com.example.shop.entity.ImgEntity;
import com.example.shop.entity.Item;
import com.example.shop.repository.ImgRepository;
import com.example.shop.repository.ItemRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class ImgService {

    private final ItemRepository itemRepository;
    private final ImgRepository imgRepository;
    private final FileService fileService;

    private ModelMapper modelMapper = new ModelMapper();

    // 사진을 DB에 저장
    public ImgDTO register(MultipartFile multipartFile , Long item_id , String repimgYn) throws IOException {

        // 물리적(사진)으로 저장 후 내용을 바탕으로 dto 만들어서 반환
        ImgDTO imgDTO = fileService.register(multipartFile);

        // 반환 값을 가지고 (imgDTO) DB 저장
        ImgEntity imgEntity = modelMapper.map(imgDTO, ImgEntity.class);

        // 참조대상 찾기
        Item item = itemRepository.findById(item_id).orElseThrow(EntityNotFoundException::new);

        // 대표 이미지 여부
        ImgEntity img = null;
        // 찾아온 대상이 대표이미지가 있다면
                if (repimgYn != null){
                    // 대표이미지 임을 명시 여부 대표이미지여부가 null이면 수행하지 않고
                    img = imgRepository.findByItemIdAndRepimgYn(item.getId(), repimgYn);
                }

        // 대표이미지가 현재 있다면
        if (img != null){
            // 경로에 기존 이미지가 있다면 찾아서 삭제
            String path = img.getImgUrl() + img.getImgName();
            fileService.del(path);

            img.setImgUrl(imgDTO.getImgUrl());
            img.setImgName(imgDTO.getImgName());
            img.setOriImgName(imgDTO.getOriImgName());

        }else {
            // 대표 이미지가 없다면
            // DB에가 대표이미지는 현재 없거나 상세이미지를 저장해야 하는 경우
            imgEntity.setRepimgYn(repimgYn);
            imgEntity.setItem(item); // 참조 대상

            imgEntity = imgRepository.save(imgEntity);

            imgDTO = modelMapper.map(imgEntity , ImgDTO.class);
        }

        return imgDTO;
    }

    public List<ImgDTO> read(Long item_id){

        List<ImgEntity> list = imgRepository.findByItemId(item_id);

        List<ImgDTO> imgDTOList = list.stream().map(imgEntity -> modelMapper.map(imgEntity, ImgDTO.class))
                                                    .collect(Collectors.toList());

        return imgDTOList;
    }

    public void delImg(Long num){
        // DB에서 파일의 경로 찾아와서 삭제
        ImgEntity imgEntity = imgRepository.findById(num).orElseThrow(EntityNotFoundException::new);

        if (imgEntity != null){
            String path = imgEntity.getImgUrl() + imgEntity.getImgName();
            // 물리적인 파일삭제

            fileService.del(path);

            // DB에 파일삭제 : 그리고 삭제
            imgRepository.delete(imgEntity);

        }

    }


}
