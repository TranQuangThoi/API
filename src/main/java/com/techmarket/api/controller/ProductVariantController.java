package com.techmarket.api.controller;

import com.techmarket.api.constant.UserBaseConstant;
import com.techmarket.api.dto.ApiMessageDto;
import com.techmarket.api.dto.ErrorCode;
import com.techmarket.api.dto.ResponseListDto;
import com.techmarket.api.dto.productVariant.ProductVariantDto;
import com.techmarket.api.form.product.CreateProductForm;
import com.techmarket.api.form.product.UpdateProductForm;
import com.techmarket.api.form.productVariant.CreateProductVariantForm;
import com.techmarket.api.form.productVariant.UpdateProductVariantForm;
import com.techmarket.api.mapper.ProductVariantMapper;
import com.techmarket.api.model.Brand;
import com.techmarket.api.model.Category;
import com.techmarket.api.model.Product;
import com.techmarket.api.model.ProductVariant;
import com.techmarket.api.model.criteria.ProductVariantCriteria;
import com.techmarket.api.repository.ProductRepository;
import com.techmarket.api.repository.ProductVariantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/product-variant")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ProductVariantController extends ABasicController{

    @Autowired
    private ProductVariantMapper productVariantMapper;
    @Autowired
    private ProductVariantRepository productVariantRepository;
    @Autowired
    private ProductRepository productRepository;


    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('PROV_L')")
    public ApiMessageDto<ResponseListDto<List<ProductVariantDto>>> getList(@Valid ProductVariantCriteria productVariantCriteria, Pageable pageable) {

        ApiMessageDto<ResponseListDto<List<ProductVariantDto>>> apiMessageDto = new ApiMessageDto<>();
        ResponseListDto<List<ProductVariantDto>> responseListDto = new ResponseListDto<>();
        Page<ProductVariant> listProVariant = productVariantRepository.findAll(productVariantCriteria.getSpecification(),pageable);
        responseListDto.setContent(productVariantMapper.fromEntityToListProVariantDto(listProVariant.getContent()));
        responseListDto.setTotalPages(listProVariant.getTotalPages());
        responseListDto.setTotalElements(listProVariant.getTotalElements());

        apiMessageDto.setData(responseListDto);
        apiMessageDto.setMessage("get list product variant success");
        return apiMessageDto;
    }
    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('PROV_V')")
    public ApiMessageDto<ProductVariantDto> getProduct(@PathVariable("id") Long id) {

        ApiMessageDto<ProductVariantDto> apiMessageDto = new ApiMessageDto<>();
        ProductVariant productVariant = productVariantRepository.findById(id).orElse(null);
        if (productVariant==null)
        {
            apiMessageDto.setResult(false);
            apiMessageDto.setMessage("Not found product variant");
            apiMessageDto.setCode(ErrorCode.PRODUCT_VARIANT_ERROR_NOT_FOUND);
            return apiMessageDto;
        }

        apiMessageDto.setData(productVariantMapper.fromEntityToProVariantDto(productVariant));
        apiMessageDto.setResult(true);
        apiMessageDto.setMessage("Get product variant success.");
        return  apiMessageDto;
    }

    @GetMapping(value = "/auto-complete",produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListDto<List<ProductVariantDto>>> ListAutoComplete(ProductVariantCriteria productVariantCriteria)
    {
        ApiMessageDto<ResponseListDto<List<ProductVariantDto>>> apiMessageDto = new ApiMessageDto<>();
        ResponseListDto<List<ProductVariantDto>> responseListDto = new ResponseListDto<>();
        productVariantCriteria.setStatus(UserBaseConstant.STATUS_ACTIVE);
        productVariantCriteria.setTotalInStock(UserBaseConstant.STATUS_ACTIVE);
        Pageable pageable = PageRequest.of(0,20);
        Page<ProductVariant> listProduct = productVariantRepository.findAll(productVariantCriteria.getSpecification(),pageable);
        responseListDto.setContent(productVariantMapper.fromEntityToListProVariantAutoDto(listProduct.getContent()));
        responseListDto.setTotalPages(listProduct.getTotalPages());
        responseListDto.setTotalElements(listProduct.getTotalElements());

        apiMessageDto.setData(responseListDto);
        apiMessageDto.setMessage("get list product variant success");
        return apiMessageDto;
    }
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
        apiMessageDto.setMessage("create product success");
        return apiMessageDto;
    }
    @PutMapping(value = "/update",produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('PROV_U')")
    public ApiMessageDto<String> updateProductVariant(@Valid @RequestBody UpdateProductVariantForm updateProductVariantForm, BindingResult bindingResult)
    {
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();

        ProductVariant productVariant = productVariantRepository.findById(updateProductVariantForm.getId()).orElse(null);
        if (productVariant==null)
        {
            apiMessageDto.setResult(false);
            apiMessageDto.setMessage("Product variant not found");
            apiMessageDto.setCode(ErrorCode.PRODUCT_VARIANT_ERROR_NOT_FOUND);
            return apiMessageDto;
        }
        if (!productVariant.getColor().equalsIgnoreCase(updateProductVariantForm.getColor()))
        {
            ProductVariant productVariantExist = productVariantRepository.findByColorAndProductId(updateProductVariantForm.getColor(),updateProductVariantForm.getProductId());
            if (productVariantExist!=null)
            {
                apiMessageDto.setResult(false);
                apiMessageDto.setMessage("Product variant already exist");
                apiMessageDto.setCode(ErrorCode.PRODUCT_VARIANT_ERROR_EXIST);
                return apiMessageDto;
            }
        }

        Product productExist = productRepository.findById(updateProductVariantForm.getProductId()).orElse(null);
        if (productExist==null)
        {
            apiMessageDto.setResult(false);
            apiMessageDto.setMessage("Product not found");
            apiMessageDto.setCode(ErrorCode.PRODUCT_ERROR_NOT_FOUND);
            return apiMessageDto;
        }
        if (updateProductVariantForm.getPrice()==null)
        {
            productVariant.setPrice(0.0);
        }
        productVariantMapper.fromUpdateToEntityProViant(updateProductVariantForm,productVariant);
        productVariant.setProduct(productExist);
        productVariantRepository.save(productVariant);
        apiMessageDto.setMessage("update product variant success");
        return apiMessageDto;
    }
}
