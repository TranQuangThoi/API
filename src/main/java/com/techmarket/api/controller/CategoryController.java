package com.techmarket.api.controller;


import com.techmarket.api.constant.UserBaseConstant;
import com.techmarket.api.dto.ApiMessageDto;
import com.techmarket.api.dto.ErrorCode;
import com.techmarket.api.dto.ResponseListDto;
import com.techmarket.api.dto.category.CategoryDto;
import com.techmarket.api.form.category.CreateCategoryForm;
import com.techmarket.api.form.category.UpdateCategoryForm;
import com.techmarket.api.mapper.CategoryMapper;
import com.techmarket.api.model.Category;
import com.techmarket.api.model.Product;
import com.techmarket.api.model.ProductVariant;
import com.techmarket.api.model.criteria.CategoryCriteria;
import com.techmarket.api.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/v1/category")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class CategoryController extends ABasicController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private NewsRepository newsRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductVariantRepository productVariantRepository;
    @Autowired
    private ImageRepository imageRepository;




    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('CA_L')")
    public ApiMessageDto<ResponseListDto<CategoryDto>> listCategory(@Valid CategoryCriteria categoryCriteria, Pageable pageable) {
        ApiMessageDto<ResponseListDto<CategoryDto>> apiMessageDto = new ApiMessageDto<>();

        Page<Category> serviceCategories = categoryRepository.findAll(CategoryCriteria.findCategoryByCriteria(categoryCriteria),pageable);
        ResponseListDto<CategoryDto> responseListDto = new ResponseListDto(categoryMapper.fromEntityToDtoList(serviceCategories.getContent()),serviceCategories.getTotalElements(), serviceCategories.getTotalPages());
        apiMessageDto.setData(responseListDto);
        apiMessageDto.setMessage("Get category list success");
        return  apiMessageDto;
    }

    @GetMapping(value = "/list_category_by_brand/{brandId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListDto<CategoryDto>> listCategoryByBrand(@PathVariable("brandId") Long brandId,@Valid CategoryCriteria categoryCriteria, Pageable pageable) {
        ApiMessageDto<ResponseListDto<CategoryDto>> apiMessageDto = new ApiMessageDto<>();

        Page<Category> serviceCategories = categoryRepository.listCategoryByBrandId(brandId,pageable);
        ResponseListDto<CategoryDto> responseListDto = new ResponseListDto(categoryMapper.fromEntityToDtoList(serviceCategories.getContent()),serviceCategories.getTotalElements(), serviceCategories.getTotalPages());
        apiMessageDto.setData(responseListDto);
        apiMessageDto.setMessage("Get category for brand success");
        return  apiMessageDto;
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('CA_V')")
    public ApiMessageDto<CategoryDto> getCategory(@PathVariable("id") Long id) {
        ApiMessageDto<CategoryDto> apiMessageDto = new ApiMessageDto<>();
        Category serviceCategory = categoryRepository.findById(id).orElse(null);
        if(serviceCategory == null){
            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.CATEGORY_ERROR_NOT_FOUND);
            return apiMessageDto;
        }

        apiMessageDto.setData(categoryMapper.fromEntityToCategoryDto(serviceCategory));
        apiMessageDto.setResult(true);
        apiMessageDto.setMessage("Get category success.");
        return  apiMessageDto;
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('CA_D')")
    public ApiMessageDto<String> deleteCategory(@PathVariable("id") Long id) {
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        Category category = categoryRepository.findById(id).orElse(null);
        if (category == null) {
            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.CATEGORY_ERROR_NOT_FOUND);
            return apiMessageDto;
        }
        List<Product> productList = productRepository.findAllByCategoryId(id);
        for (Product item : productList)
            {
                productVariantRepository.deleteAllByProductId(item.getId());
                imageRepository.deleteAllByProductId(item.getId());
            }
        productRepository.deleteAllByCategoryId(id);

        newsRepository.deleteAllByCategoryId(id);
        categoryRepository.deleteById(id);
        apiMessageDto.setMessage("Delete category success");
        return apiMessageDto;
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('CA_C')")
    public ApiMessageDto<String> createCategory(@Valid @RequestBody CreateCategoryForm createCategoryForm, BindingResult bindingResult) {
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        Category categoryCheckName = categoryRepository.findByName(createCategoryForm.getName());
        if (categoryCheckName != null) {
            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.CATEGORY_ERROR_EXIST);
            return apiMessageDto;
        }
        categoryRepository.save(categoryMapper.fromCreateCategory(createCategoryForm));
        apiMessageDto.setMessage("Create category success");
        return apiMessageDto;
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('CA_U')")
    public ApiMessageDto<String> updateCategory(@Valid @RequestBody UpdateCategoryForm updateCategoryForm, BindingResult bindingResult) {
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        Category categoryCheckName = categoryRepository.findByName(updateCategoryForm.getName());
        if (categoryCheckName != null && !Objects.equals(categoryCheckName.getId(),updateCategoryForm.getCategoryId())) {
            apiMessageDto.setResult(false);
            apiMessageDto.setMessage("Update category fail");
            apiMessageDto.setCode(ErrorCode.CATEGORY_ERROR_EXIST);
            return apiMessageDto;
        }

        Category category = categoryRepository.findById(updateCategoryForm.getCategoryId()).orElse(null);
        if (category == null) {
            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.CATEGORY_ERROR_NOT_FOUND);
            return apiMessageDto;
        }

        categoryMapper.mappingForUpdateServiceCategory(updateCategoryForm, category);
        categoryRepository.save(category);
        apiMessageDto.setMessage("Update category success");
        return apiMessageDto;
    }

    @GetMapping(value = "/auto-complete", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListDto<List<CategoryDto>>> autoCompleteCategoryAuto(@Valid CategoryCriteria categoryCriteria, Pageable pageable) {
        ApiMessageDto<ResponseListDto<List<CategoryDto>>> apiMessageDto = new ApiMessageDto<>();
        ResponseListDto<List<CategoryDto>> responseListDto = new ResponseListDto<>();
        Page<Category> categories = categoryRepository.findAll(CategoryCriteria.findCategoryByCriteria(categoryCriteria), pageable);
        List<CategoryDto> addressAdminDtos = categoryMapper.fromCategoryToComplteDtoList(categories.getContent());

        responseListDto.setContent(addressAdminDtos);
        responseListDto.setTotalPages(categories.getTotalPages());
        responseListDto.setTotalElements(categories.getTotalElements());

        apiMessageDto.setData(responseListDto);
        apiMessageDto.setMessage("Get category success");
        return apiMessageDto;
    }
}
