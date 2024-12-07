package com.techmarket.api.controller;


import com.techmarket.api.dto.ApiMessageDto;
import com.techmarket.api.dto.images.ImagesDto;
import com.techmarket.api.mapper.ImageMapper;
import com.techmarket.api.model.Images;
import com.techmarket.api.repository.ImageRepository;
import com.techmarket.api.repository.ProductVariantRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/v1/image")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class ImageController extends ABasicController{

    @Autowired
    private  ProductVariantRepository productVariantRepository;

    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private ImageMapper imageMapper;

    @GetMapping(value = "/get-by-variantId", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<List<ImagesDto>> getListImageByKind(@RequestParam("id") long id) {
        ApiMessageDto<List<ImagesDto>> apiMessageDto = new ApiMessageDto<>();

        List<Images> imagesList = imageRepository.findAllByProductVariantId(id);

        List<ImagesDto> imagesDtoList = imageMapper.fromEntityToListImageDto(imagesList);
        apiMessageDto.setData(imagesDtoList);
        apiMessageDto.setMessage("get images success");
        return apiMessageDto;
    }

}
