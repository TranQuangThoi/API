package com.techmarket.api.controller;

import com.techmarket.api.dto.ApiMessageDto;
import com.techmarket.api.dto.ErrorCode;
import com.techmarket.api.model.Product;
import com.techmarket.api.model.ProductVariant;
import com.techmarket.api.repository.ProductRepository;
import com.techmarket.api.repository.ProductVariantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/product-variant")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ProductVariantController extends ABasicController{

    @Autowired
    private ProductVariantRepository productVariantRepository;
    @Autowired
    private ProductRepository productRepository;

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
}
