package com.techmarket.api.controller;

import com.techmarket.api.dto.ApiMessageDto;
import com.techmarket.api.dto.ErrorCode;
import com.techmarket.api.dto.productVariant.CreateProductVariant;
import com.techmarket.api.form.productVariant.CreateProductVariantForm;
import com.techmarket.api.mapper.ProductVariantMapper;
import com.techmarket.api.model.Images;
import com.techmarket.api.model.Product;
import com.techmarket.api.model.ProductVariant;
import com.techmarket.api.repository.ImageRepository;
import com.techmarket.api.repository.ProductRepository;
import com.techmarket.api.repository.ProductVariantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/v1/product-variant")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ProductVariantController extends ABasicController{

    @Autowired
    private ProductVariantRepository productVariantRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductVariantMapper productVariantMapper;
    @Autowired
    private ImageRepository imageRepository;

    @DeleteMapping(value = "/delete/{id}")
    @PreAuthorize("hasRole('PROV_D')")
    public ApiMessageDto<String> deleteProductVariant(@PathVariable("id") Long id)
    {
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        ProductVariant productVariant = productVariantRepository.findById(id).orElse(null);
        if (productVariant==null)
        {
            apiMessageDto.setMessage("Not found product variant");
            apiMessageDto.setCode(ErrorCode.PRODUCT_VARIANT_ERROR_NOT_FOUND);
            return apiMessageDto;
        }
        Product product = productRepository.findById(productVariant.getProduct().getId()).orElse(null);
        product.setTotalInStock(product.getTotalInStock()-productVariant.getTotalStock());
        productVariantRepository.delete(productVariant);
        apiMessageDto.setMessage("Delete product variant success");
        return apiMessageDto;
    }
    @PostMapping(value = "/create",produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('PROV_C')")
    public ApiMessageDto<String> createProductVariant(@Valid @RequestBody CreateProductVariantForm createProductVariantForm, BindingResult bindingResult)
    {
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();

        ProductVariant productVariantExist = productVariantRepository.findByColorAndProductId(createProductVariantForm.getColor(),createProductVariantForm.getProductId());
        if (productVariantExist!=null)
        {
            apiMessageDto.setMessage("This product variant already exists");
            apiMessageDto.setCode(ErrorCode.PRODUCT_VARIANT_ERROR_EXIST);
            return apiMessageDto;
        }
        Product productExist = productRepository.findById(createProductVariantForm.getProductId()).orElse(null);
        if (productExist==null)
        {
            apiMessageDto.setResult(false);
            apiMessageDto.setMessage("Product not found");
            apiMessageDto.setCode(ErrorCode.PRODUCT_ERROR_NOT_FOUND);
            return apiMessageDto;
        }
        ProductVariant productVariant = productVariantMapper.fromCreateProVariantToEntity(createProductVariantForm);
        productVariant.setProduct(productExist);
        productVariantRepository.save(productVariant);
        int totalStock = createProductVariantForm.getTotalStock() + productExist.getTotalInStock();
        productExist.setTotalInStock(totalStock);
        productRepository.save(productExist);

        List<String> listImage = createProductVariantForm.getFile();
        List<Images> saveImage = new ArrayList<>();
        for(String l : listImage)
        {
            Images images = new Images();
            images.setLink(l);
            images.setProductVariant(productVariant);
            saveImage.add(images);
        }
        imageRepository.saveAll(saveImage);

        apiMessageDto.setMessage("create product success");
        return apiMessageDto;
    }
}

