package com.techmarket.api.controller;

import com.techmarket.api.constant.UserBaseConstant;
import com.techmarket.api.dto.ApiMessageDto;
import com.techmarket.api.dto.ErrorCode;
import com.techmarket.api.dto.ResponseListDto;
import com.techmarket.api.dto.brand.BrandDto;
import com.techmarket.api.form.brand.CreateBrandForm;
import com.techmarket.api.form.brand.UpdateBrandForm;
import com.techmarket.api.mapper.BrandMapper;
import com.techmarket.api.model.Brand;
import com.techmarket.api.model.Product;
import com.techmarket.api.model.criteria.BrandCriteria;
import com.techmarket.api.repository.BrandRepository;
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
@RequestMapping("/v1/brand")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class BrandController extends ABasicController{

    @Autowired
    private BrandMapper brandMapper;
    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductVariantRepository productVariantRepository;



    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('BR_L')")
    public ApiMessageDto<ResponseListDto<List<BrandDto>>> getList(@Valid BrandCriteria brandCriteria, Pageable pageable) {

        ApiMessageDto<ResponseListDto<List<BrandDto>>> apiMessageDto = new ApiMessageDto<>();
        ResponseListDto<List<BrandDto>> responseListDto = new ResponseListDto<>();
        Page<Brand> listBrand = brandRepository.findAll(brandCriteria.getSpecification(),pageable);
        responseListDto.setContent(brandMapper.fromEntityToListBrandDto(listBrand.getContent()));
        responseListDto.setTotalPages(listBrand.getTotalPages());
        responseListDto.setTotalElements(listBrand.getTotalElements());

        apiMessageDto.setData(responseListDto);
        apiMessageDto.setMessage("get list success");

        return apiMessageDto;
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('BR_V')")
    public ApiMessageDto<BrandDto> getBrand(@PathVariable("id") Long id) {
        ApiMessageDto<BrandDto> apiMessageDto = new ApiMessageDto<>();

        Brand brand = brandRepository.findById(id).orElse(null);
        if (brand==null)
        {
            apiMessageDto.setResult(false);
            apiMessageDto.setMessage("Not found brand");
            apiMessageDto.setCode(ErrorCode.BRAND_ERROR_NOT_FOUND);
           return apiMessageDto;
        }

        apiMessageDto.setData(brandMapper.fromEntityToBrandDto(brand));
        apiMessageDto.setResult(true);
        apiMessageDto.setMessage("Get brand success.");
        return  apiMessageDto;
    }

    @GetMapping(value = "/auto-complete",produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListDto<List<BrandDto>>> ListAutoComplete(BrandCriteria brandCriteria ,Pageable pageable)
    {
        ApiMessageDto<ResponseListDto<List<BrandDto>>> apiMessageDto = new ApiMessageDto<>();
        ResponseListDto<List<BrandDto>> responseListDto = new ResponseListDto<>();
        brandCriteria.setStatus(UserBaseConstant.STATUS_ACTIVE);
//        Pageable pageable = PageRequest.of(0,10);

        Page<Brand> listBrand =brandRepository.findAll(brandCriteria.getSpecification(),pageable);
        responseListDto.setContent(brandMapper.fromEntityToListBrandDtoAuto(listBrand.getContent()));
        responseListDto.setTotalPages(listBrand.getTotalPages());
        responseListDto.setTotalElements(listBrand.getTotalElements());

        apiMessageDto.setData(responseListDto);
        apiMessageDto.setMessage("get list brand success");
        return apiMessageDto;
    }
    @PostMapping(value = "/create",produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('BR_C')")
    public ApiMessageDto<String> createStudent(@Valid @RequestBody CreateBrandForm createBrandForm, BindingResult bindingResult)
    {
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        Brand brand = brandRepository.findByNameContainingIgnoreCase(createBrandForm.getName().toLowerCase());
        if (brand!=null)
        {
            apiMessageDto.setResult(false);
            apiMessageDto.setMessage("brand already exists");
            apiMessageDto.setCode(ErrorCode.BRAND_ERROR_EXIST);
            return apiMessageDto;
        }

        brandRepository.save(brandMapper.fromCreateBrandToEntity(createBrandForm));
        apiMessageDto.setMessage("create brand success");
        return apiMessageDto;
    }
    @DeleteMapping(value = "/delete/{id}")
    @PreAuthorize("hasRole('BR_D')")
    public ApiMessageDto<String> deleteBrand(@PathVariable("id") Long id)
    {
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        Brand brandExist = brandRepository.findById(id).orElse(null);
        if (brandExist==null)
        {
            apiMessageDto.setMessage("not foun brand");
            apiMessageDto.setCode(ErrorCode.BRAND_ERROR_NOT_FOUND);
            return apiMessageDto;
        }
        List<Product> productList = productRepository.findAllByBrandId(id);
        for (Product item : productList)
        {
            productVariantRepository.deleteAllByProductId(item.getId());
        }
        productRepository.deleteAllByBrandId(id);
        brandRepository.delete(brandExist);
        apiMessageDto.setMessage("Delete brand success");
        return apiMessageDto;
    }
    @PutMapping(value = "/update",produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('BR_U')")
    public ApiMessageDto<String> updateBrand(@Valid @RequestBody UpdateBrandForm updateBrandForm, BindingResult bindingResult)
    {
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();

        Brand brand = brandRepository.findById(updateBrandForm.getId()).orElse(null);
        if (brand==null)
        {
            apiMessageDto.setResult(false);
            apiMessageDto.setMessage("brand not found");
            apiMessageDto.setCode(ErrorCode.BRAND_ERROR_NOT_FOUND);
            return apiMessageDto;
        }
        if (!brand.getName().equalsIgnoreCase(updateBrandForm.getName()))
        {
            Brand brandExist = brandRepository.findByNameContainingIgnoreCase(updateBrandForm.getName());
            if (brandExist!=null)
            {
                apiMessageDto.setMessage("brand already exists");
                apiMessageDto.setCode(ErrorCode.BRAND_ERROR_NAME_EXIST);
                return apiMessageDto;
            }
        }
        brandMapper.fromUpdateToEntityBrand(updateBrandForm,brand);
        brandRepository.save(brand);
        apiMessageDto.setMessage("update brand success");
        return apiMessageDto;
    }
}
