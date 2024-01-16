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
import com.techmarket.api.model.*;
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

import javax.transaction.Transactional;
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
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartDetailRepository cartDetailRepository;

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
    @GetMapping(value = "/get-product-related/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<List<ProductDto>> getProductRelated(@PathVariable("id") Long id) {
        ApiMessageDto<List<ProductDto>> apiMessageDto = new ApiMessageDto<>();

        Product product = productRepository.findById(id).orElse(null);
        if (product==null)
        {
            apiMessageDto.setResult(false);
            apiMessageDto.setMessage("Not found product");
            apiMessageDto.setCode(ErrorCode.PRODUCT_ERROR_NOT_FOUND);
            return apiMessageDto;
        }
        List<Product> productList = product.getRelatedProducts();
        List<ProductDto> productDtoList = new ArrayList<>();
        for (Product item : productList)
        {
            ProductDto productDto = productMapper.fromEntityToProductDto(item);
            List<ProductVariant> productVariantList = productVariantRepository.findAllByProductIdAndStatus(item.getId(),UserBaseConstant.STATUS_ACTIVE);
            productDto.setListProductVariant(productVariantMapper.fromEntityToListProVariantAutoDto(productVariantList));
            productDtoList.add(productDto);
        }

        apiMessageDto.setData(productDtoList);
        apiMessageDto.setResult(true);
        apiMessageDto.setMessage("Get product success.");
        return  apiMessageDto;
    }
    @GetMapping(value = "/auto-complete",produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<List<ProductDto>> ListAutoComplete(ProductCriteria productCriteria)
    {
        ApiMessageDto<List<ProductDto>> apiMessageDto = new ApiMessageDto<>();
//        ResponseListDto<List<ProductDto>> responseListDto = new ResponseListDto<>();
        List<Product> list = productRepository.findAll(productCriteria.getSpecification());
        productCriteria.setStatus(UserBaseConstant.STATUS_ACTIVE);

        List<ProductDto> listProduct = productMapper.fromEntityToListProductDto(list);
        for (ProductDto item : listProduct)
        {
            List<ProductVariant> productVariantList = productVariantRepository.findAllByProductIdAndStatus(item.getId(),UserBaseConstant.STATUS_ACTIVE);
            item.setListProductVariant(productVariantMapper.fromEntityToListProVariantDto(productVariantList));
        }
        apiMessageDto.setData(listProduct);
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
        if (createProductForm.getRelatedProducts()!=null)
        {
            List<Product> related = new ArrayList<>();
            for(int i=0;i< createProductForm.getRelatedProducts().length;i++){
                Product productExited = productRepository.findById(createProductForm.getRelatedProducts()[i]).orElse(null);
                if(productExited != null){
                    related.add(productExited);
                }
            }
            product.setRelatedProducts(related);
        }
        product.setCategory(categoryExist);
        if (createProductForm.getSaleOff()==null)
        {
            product.setSaleOff(0.0);
        }else {
            product.setSaleOff(createProductForm.getSaleOff());
        }


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
        if (updateProductForm.getRelatedProducts()!=null){
            List<Product> related = new ArrayList<>();
            for(int i=0;i< updateProductForm.getRelatedProducts().length;i++){
                Product productExited = productRepository.findById(updateProductForm.getRelatedProducts()[i]).orElse(null);
                if(productExited != null){
                    related.add(productExited);
                }
            }
            product.setRelatedProducts(related);
        }
        productRepository.save(product);

        apiMessageDto.setMessage("update product success");
        return apiMessageDto;
    }
    @Transactional
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
        List<ProductVariant> productList = productVariantRepository.findAllByProductId(id);
        for (ProductVariant item : productList)
        {
            cartDetailRepository.deleteAllByProductVariantId(item.getId());
        }
        product.getRelatedProducts().clear();
        productRepository.save(product);
        cartDetailRepository.deleteAllByProduct(product.getId());
        reviewRepository.deleteAllByProductId(id);
        productVariantRepository.deleteAllByProductId(id);
        productRepository.delete(product);
        apiMessageDto.setMessage("Delete product success");
        return apiMessageDto;
    }

    @GetMapping(value = "/get-product-top10",produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<List<ProductDto>> top10BestSellingt()
    {
        ApiMessageDto<List<ProductDto>> apiMessageDto = new ApiMessageDto<>();
        List<Product> listProduct = productRepository.findTop10ProductsBySoldAmount(PageRequest.of(0,10));

        apiMessageDto.setData(productMapper.fromEntityToListProductAutoDto(listProduct));
        apiMessageDto.setMessage("get top 10 best saler");
        return apiMessageDto;
    }
    @GetMapping(value = "/get-product-not-selling-well",produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('PR_NSW')")
    public ApiMessageDto<ResponseListDto<List<ProductDto>>> getProductNotSellingWell(@RequestParam("soldAmount") Integer soldAmount, Pageable pageable)
    {
        ApiMessageDto<ResponseListDto<List<ProductDto>>> apiMessageDto = new ApiMessageDto<>();
        ResponseListDto<List<ProductDto>> responseListDto = new ResponseListDto<>();

        Page<Product> productList = productRepository.filterProductsBySalesVolume(soldAmount,pageable);

        responseListDto.setContent(productMapper.fromEntityToListProductDto(productList.getContent()));
        responseListDto.setTotalPages(productList.getTotalPages());
        responseListDto.setTotalElements(productList.getTotalElements());
        apiMessageDto.setData(responseListDto);
        apiMessageDto.setMessage("get product Not Selling Well success");
        return apiMessageDto;
    }
    @GetMapping(value = "/get-product-selling-well",produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('PR_SW')")
    public ApiMessageDto<ResponseListDto<List<ProductDto>>> getProductSellingWell(@RequestParam("soldAmount") Integer soldAmount, Pageable pageable)
    {
        ApiMessageDto<ResponseListDto<List<ProductDto>>> apiMessageDto = new ApiMessageDto<>();
        ResponseListDto<List<ProductDto>> responseListDto = new ResponseListDto<>();

        Page<Product> productList = productRepository.filterProductsBySalesVolumeWell(soldAmount,pageable);

        responseListDto.setContent(productMapper.fromEntityToListProductDto(productList.getContent()));
        responseListDto.setTotalPages(productList.getTotalPages());
        responseListDto.setTotalElements(productList.getTotalElements());
        apiMessageDto.setData(responseListDto);
        apiMessageDto.setMessage("get product Selling Well success");
        return apiMessageDto;
    }
}
