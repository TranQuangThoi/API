package com.techmarket.api.controller;


import com.techmarket.api.dto.ApiMessageDto;
import com.techmarket.api.dto.ErrorCode;
import com.techmarket.api.dto.ResponseListDto;
import com.techmarket.api.dto.brand.BrandDto;
import com.techmarket.api.dto.cart.CartDto;
import com.techmarket.api.exception.UnauthorizationException;
import com.techmarket.api.form.brand.CreateBrandForm;
import com.techmarket.api.form.cart.CreateCartForm;
import com.techmarket.api.form.cart.UpdateCartForm;
import com.techmarket.api.form.cart.cartDetail.CreateCartDetailForm;
import com.techmarket.api.form.news.UpdateNewsForm;
import com.techmarket.api.mapper.CartDetailMapper;
import com.techmarket.api.mapper.CartMapper;
import com.techmarket.api.mapper.UserMapper;
import com.techmarket.api.model.*;
import com.techmarket.api.model.criteria.BrandCriteria;
import com.techmarket.api.repository.CartDetailRepository;
import com.techmarket.api.repository.CartRepository;
import com.techmarket.api.repository.ProductVariantRepository;
import com.techmarket.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/cart")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CartController extends ABasicController{

    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private CartDetailMapper cartDetailMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartDetailRepository cartDetailRepository;
    @Autowired
    private ProductVariantRepository productVariantRepository;

    @GetMapping(value = "/get-my-cart", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<CartDto> getList(@Valid BrandCriteria brandCriteria, Pageable pageable) {
        if (!isUser())
        {
            throw new UnauthorizationException("Please create an account to be able to shop and get more incentives");
        }
        ApiMessageDto<CartDto> apiMessageDto = new ApiMessageDto<>();
        CartDto cartDto = new CartDto();
        Long accountId = getCurrentUser();
        User user = userRepository.findByAccountId(accountId).orElse(null);
        if (user==null)
        {
            apiMessageDto.setResult(false);
            apiMessageDto.setMessage("Not found user");
            apiMessageDto.setCode(ErrorCode.USER_ERROR_NOT_FOUND);
            return apiMessageDto;
        }
        Cart cart = cartRepository.findCartByUserId(user.getId());
        if (cart==null)
        {
            Cart newCart = new Cart();
            newCart.setUser(user);
            cartRepository.save(newCart);
            cart = cartRepository.findCartByUserId(user.getId());
        }
        List<CartDetail> cartDetails = cartDetailRepository.findAllByCartId(cart.getId());
        cartDto =cartMapper.fromEntityToDto(cart);
        cartDto.setCartDetailDtos(cartDetailMapper.fromEntityToListCartDetailDto(cartDetails));
        apiMessageDto.setData(cartDto);
        apiMessageDto.setMessage("get list success");
        return apiMessageDto;
    }
    @PostMapping(value = "/add-product-into-cart",produces = MediaType.APPLICATION_JSON_VALUE)
//    @PreAuthorize("hasRole('BR_C')")
    public ApiMessageDto<String> addProductIntoCart(@Valid @RequestBody CreateCartDetailForm createCartDetailForm, BindingResult bindingResult)
    {
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();

        if (!isUser())
        {
            throw new UnauthorizationException("Please create an account to be able to shop and get more incentives");
        }
        Long accountId = getCurrentUser();
        User user = userRepository.findByAccountId(accountId).orElse(null);
        if (user==null)
        {
            apiMessageDto.setResult(false);
            apiMessageDto.setMessage("Not found user");
            apiMessageDto.setCode(ErrorCode.USER_ERROR_NOT_FOUND);
            return apiMessageDto;
        }
        Cart cart = cartRepository.findCartByUserId(user.getId());
        if (cart==null)
        {
            Cart newCart = new Cart();
            newCart.setUser(user);
            cartRepository.save(newCart);
            cart = cartRepository.findCartByUserId(user.getId());
        }
        ProductVariant productVariant = productVariantRepository.findById(createCartDetailForm.getVariantId()).orElse(null);
        if (productVariant==null)
        {
            apiMessageDto.setResult(false);
            apiMessageDto.setMessage("Not found productvariant");
            apiMessageDto.setCode(ErrorCode.PRODUCT_VARIANT_ERROR_NOT_FOUND);
            return apiMessageDto;
        }
        CartDetail cartDetail = new CartDetail();
        cartDetail.setCart(cart);
        cartDetail.setProductVariant(productVariant);
        cartDetail.setQuantity(createCartDetailForm.getQuantity());
        cartDetailRepository.save(cartDetail);
       apiMessageDto.setMessage("add product into cart success");
       return apiMessageDto;
    }
    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
//    @PreAuthorize("hasRole('NEWS_D')")
    public ApiMessageDto<String> deleteItemInCart(@PathVariable("id") Long id) {
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        CartDetail exitCartDetail = cartDetailRepository.findById(id).orElse(null);
        if (exitCartDetail == null) {
            apiMessageDto.setResult(false);
            apiMessageDto.setMessage("Not found product");
            apiMessageDto.setCode(ErrorCode.PRODUCT_VARIANT_ERROR_NOT_FOUND);
            return apiMessageDto;
        }
        cartDetailRepository.delete(exitCartDetail);
        apiMessageDto.setMessage("Delete product success");
        return apiMessageDto;
    }

    @Transactional
    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
//    @PreAuthorize("hasRole('NEWS_U')")
    public ApiMessageDto<String> update(@Valid @RequestBody UpdateCartForm updateCartForm, BindingResult bindingResult) {

        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();

        Long accountId = getCurrentUser();
        User user = userRepository.findByAccountId(accountId).orElse(null);
        Cart cart = cartRepository.findCartByUserId(user.getId());
        if (cart==null)
        {
            apiMessageDto.setResult(false);
            apiMessageDto.setMessage("There are no products to update");
            apiMessageDto.setCode(ErrorCode.CATEGORY_ERROR_NOT_FOUND);
            return apiMessageDto;
        }
        List<CartDetail> cartDetails = updateCartForm.getCartDetails();
        cartDetailRepository.saveAll(cartDetails);
        apiMessageDto.setMessage("update cart success");
        return apiMessageDto;
    }





}
