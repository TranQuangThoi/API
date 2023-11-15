package com.techmarket.api.controller;

import com.techmarket.api.constant.UserBaseConstant;
import com.techmarket.api.dto.ApiMessageDto;
import com.techmarket.api.dto.ErrorCode;
import com.techmarket.api.dto.ResponseListDto;
import com.techmarket.api.dto.product.ProductDto;
import com.techmarket.api.form.product.CreateProductForm;
import com.techmarket.api.form.product.UpdateProductForm;
import com.techmarket.api.form.productVariant.CreateProductVariantForm;
import com.techmarket.api.form.productVariant.UpdateProductVariantForm;
import com.techmarket.api.mapper.ProductMapper;
import com.techmarket.api.mapper.ProductVariantMapper;
import com.techmarket.api.model.Brand;
import com.techmarket.api.model.Category;
import com.techmarket.api.model.Product;
import com.techmarket.api.model.ProductVariant;
import com.techmarket.api.model.criteria.ProductCriteria;
import com.techmarket.api.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/v1/product")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ProductController extends ABasicController{

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private ProductVariantMapper productVariantMapper;
    @Autowired
    private ProductVariantRepository productVariantRepository;
    @Autowired
    private ReviewRepository reviewRepository;

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('PR_L')")
    public ApiMessageDto<ResponseListDto<List<ProductDto>>> getList(@Valid ProductCriteria productCriteria, Pageable pageable) {

        ApiMessageDto<ResponseListDto<List<ProductDto>>> apiMessageDto = new ApiMessageDto<>();
        ResponseListDto<List<ProductDto>> responseListDto = new ResponseListDto<>();
        Page<Product> listProduct = productRepository.findAll(productCriteria.getSpecification(),pageable);
       List<ProductDto> productDtoList = productMapper.fromEntityToListProductDto(listProduct.getContent());
       for (ProductDto item : productDtoList)
       {
           List<ProductVariant> productVariantList = productVariantRepository.findAllByProductId(item.getId());
           item.setListProductVariant(productVariantMapper.fromEntityToListProVariantDto(productVariantList));
       }

        responseListDto.setContent(productDtoList);
        responseListDto.setTotalPages(listProduct.getTotalPages());
        responseListDto.setTotalElements(listProduct.getTotalElements());

        apiMessageDto.setData(responseListDto);
        apiMessageDto.setMessage("get list product success");

        return apiMessageDto;
    }
    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('PR_V')")
    public ApiMessageDto<ProductDto> getProduct(@PathVariable("id") Long id) {
        ApiMessageDto<ProductDto> apiMessageDto = new ApiMessageDto<>();

        Product product = productRepository.findById(id).orElse(null);
        if (product==null)
        {
            apiMessageDto.setResult(false);
            apiMessageDto.setMessage("Not found product");
            apiMessageDto.setCode(ErrorCode.PRODUCT_ERROR_NOT_FOUND);
            return apiMessageDto;
        }
        ProductDto productDto = productMapper.fromEntityToProductDto(product);
        List<ProductVariant> productVariantList = productVariantRepository.findAllByProductId(id);
        productDto.setListProductVariant(productVariantMapper.fromEntityToListProVariantDto(productVariantList));
        apiMessageDto.setData(productDto);
        apiMessageDto.setResult(true);
        apiMessageDto.setMessage("Get product success.");
        return  apiMessageDto;
    }
    @GetMapping(value = "/get-autoComplete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ProductDto> getProductAutocomplete(@PathVariable("id") Long id) {
        ApiMessageDto<ProductDto> apiMessageDto = new ApiMessageDto<>();

        Product product = productRepository.findById(id).orElse(null);
        if (product==null)
        {
            apiMessageDto.setResult(false);
            apiMessageDto.setMessage("Not found product");
            apiMessageDto.setCode(ErrorCode.PRODUCT_ERROR_NOT_FOUND);
            return apiMessageDto;
        }
        ProductDto productDto = productMapper.fromEntityToProductDto(product);
        List<ProductVariant> productVariantList = productVariantRepository.findAllByProductIdAndStatus(id,UserBaseConstant.STATUS_ACTIVE);
        productDto.setListProductVariant(productVariantMapper.fromEntityToListProVariantAutoDto(productVariantList));
        apiMessageDto.setData(productDto);
        apiMessageDto.setResult(true);
        apiMessageDto.setMessage("Get product success.");
        return  apiMessageDto;
    }
    @GetMapping(value = "/auto-complete",produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListDto<List<ProductDto>>> ListAutoComplete(ProductCriteria productCriteria)
    {
        ApiMessageDto<ResponseListDto<List<ProductDto>>> apiMessageDto = new ApiMessageDto<>();
        ResponseListDto<List<ProductDto>> responseListDto = new ResponseListDto<>();
        productCriteria.setStatus(UserBaseConstant.STATUS_ACTIVE);
        Pageable pageable = PageRequest.of(0,10);

        Page<Product> listProduct = productRepository.findAll(productCriteria.getSpecification(),pageable);
        responseListDto.setContent(productMapper.fromEntityToListProductAutoDto(listProduct.getContent()));
        responseListDto.setTotalPages(listProduct.getTotalPages());
        responseListDto.setTotalElements(listProduct.getTotalElements());

        apiMessageDto.setData(responseListDto);
        apiMessageDto.setMessage("get list product success");
        return apiMessageDto;
    }

    @PostMapping(value = "/create",produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('PR_C')")
    public ApiMessageDto<String> createProduct(@Valid @RequestBody CreateProductForm createProductForm, BindingResult bindingResult)
    {
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();

        Product productExist = productRepository.findByNameContainingIgnoreCase(createProductForm.getName());
        if (productExist!=null)
        {
            apiMessageDto.setResult(false);
            apiMessageDto.setMessage("product name already exists");
            apiMessageDto.setCode(ErrorCode.PRODUCT_ERROR_EXIST);
            return apiMessageDto;
        }
        Category categoryExist = categoryRepository.findById(createProductForm.getCategoryId()).orElse(null);
        if (categoryExist==null)
        {
            apiMessageDto.setResult(false);
            apiMessageDto.setMessage("Category not found");
            apiMessageDto.setCode(ErrorCode.CATEGORY_ERROR_NOT_FOUND);
            return apiMessageDto;
        }
        Product product = productMapper.fromCreateProductToEntity(createProductForm);
        product.setCategory(categoryExist);

        if (createProductForm.getBrandId()!=null)
        {
            Brand brandExist = brandRepository.findById(createProductForm.getBrandId()).orElse(null);
            if (brandExist==null)
            {
                apiMessageDto.setResult(false);
                apiMessageDto.setMessage("Brand not found");
                apiMessageDto.setCode(ErrorCode.BRAND_ERROR_NOT_FOUND);
                return apiMessageDto;
            }
            product.setBrand(brandExist);

        }
        productRepository.save(product);

        List<CreateProductVariantForm> createProductVariantForms = createProductForm.getListDetails();
        List<ProductVariant> productVariantList = productVariantMapper.fromCreateProVariantToEntityList(createProductVariantForms);
        int totalStock=0;
       for (ProductVariant item: productVariantList)
       {
           item.setProduct(product);
           totalStock+=item.getTotalStock();
       }
       productVariantRepository.saveAll(productVariantList);
       product.setTotalInStock(totalStock);
       productRepository.save(product);
        apiMessageDto.setMessage("create product success");
        return apiMessageDto;
    }

    @PutMapping(value = "/update",produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('PR_U')")
    public ApiMessageDto<String> updateProduct(@Valid @RequestBody UpdateProductForm updateProductForm, BindingResult bindingResult)
    {
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();

        Product product = productRepository.findById(updateProductForm.getId()).orElse(null);
        if (product==null)
        {
            apiMessageDto.setResult(false);
            apiMessageDto.setMessage("Product not found");
            apiMessageDto.setCode(ErrorCode.PRODUCT_ERROR_NOT_FOUND);
            return apiMessageDto;
        }
        if (!product.getName().equalsIgnoreCase(updateProductForm.getName()))
        {
            Product productName = productRepository.findByNameContainingIgnoreCase(updateProductForm.getName().toLowerCase());
            if (productName!=null)
            {
                apiMessageDto.setMessage("Product name already exists");
                apiMessageDto.setCode(ErrorCode.PRODUCT_ERROR_EXIST);
                return apiMessageDto;
            }
        }

        if (!product.getCategory().getId().equals(updateProductForm.getCategoryId()))
        {
            Category category = categoryRepository.findById(updateProductForm.getCategoryId()).orElse(null);
            if (category==null)
            {
                apiMessageDto.setResult(false);
                apiMessageDto.setMessage("Category not found");
                apiMessageDto.setCode(ErrorCode.CATEGORY_ERROR_NOT_FOUND);
                return apiMessageDto;
            }
            product.setCategory(category);
        }
        if (updateProductForm.getBrandId()!=null)
        {
            Brand brand = brandRepository.findById(updateProductForm.getBrandId()).orElse(null);
            if (brand==null)
            {
                apiMessageDto.setResult(false);
                apiMessageDto.setMessage("Brand not found");
                apiMessageDto.setCode(ErrorCode.BRAND_ERROR_NOT_FOUND);
                return apiMessageDto;
            }
            product.setBrand(brand);
        }

        List<UpdateProductVariantForm> updateProductVariantFormList = updateProductForm.getListDetails();
        List<ProductVariant> productVariantList = productVariantRepository.findAllByProductId(updateProductForm.getId());
        for (UpdateProductVariantForm up : updateProductVariantFormList)
        {
            for (int i=0 ; i<productVariantList.size();i++)
            {
                if(Objects.equals(up.getId(),productVariantList.get(i).getId()))
                {
                    productVariantMapper.fromUpdateToEntityProViant(up,productVariantList.get(i));
                    break;
                }
            }
        }
        productVariantRepository.saveAll(productVariantList);
        int totalStockProduct =0;
       for (ProductVariant item : productVariantList)
       {
           totalStockProduct += item.getTotalStock();
       }
        product.setTotalInStock(totalStockProduct);
        productMapper.fromUpdateToEntityProduct(updateProductForm,product);
        productRepository.save(product);

        apiMessageDto.setMessage("update product success");
        return apiMessageDto;
    }
    @DeleteMapping(value = "/delete/{id}")
    @PreAuthorize("hasRole('PR_D')")
    public ApiMessageDto<String> deleteProduct(@PathVariable("id") Long id)
    {
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        Product product = productRepository.findById(id).orElse(null);
        if (product==null)
        {
            apiMessageDto.setMessage("Not found product");
            apiMessageDto.setCode(ErrorCode.PRODUCT_ERROR_NOT_FOUND);
            return apiMessageDto;
        }
        reviewRepository.deleteAllByProductId(id);
        productVariantRepository.deleteAllByProductId(id);
        productRepository.delete(product);
        apiMessageDto.setMessage("Delete product success");
        return apiMessageDto;
    }

}
