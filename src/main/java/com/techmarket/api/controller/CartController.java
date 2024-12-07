package com.techmarket.api.controller;


import com.techmarket.api.dto.ApiMessageDto;
import com.techmarket.api.dto.ErrorCode;
import com.techmarket.api.dto.cart.CartDto;
import com.techmarket.api.dto.cart.cartDetail.CartDetailDto;
import com.techmarket.api.exception.UnauthorizationException;
import com.techmarket.api.form.cart.UpdateCartForm;
import com.techmarket.api.form.cart.cartDetail.CreateCartDetailForm;
import com.techmarket.api.form.cart.cartDetail.UpdateCartDetailForm;
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
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasRole('CART_V')")
    public ApiMessageDto<CartDto> getList(@Valid BrandCriteria brandCriteria, Pageable pageable) {
        ApiMessageDto<CartDto> apiMessageDto = new ApiMessageDto<>();
        CartDto cartDto ;
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
        System.out.println(cart.getId());
        if (cart==null)
        {
            Cart newCart = new Cart();
            newCart.setUser(user);
            cartRepository.save(newCart);
            cart = cartRepository.findCartByUserId(user.getId());
        }
        List<CartDetail> cartDetails = cartDetailRepository.findAllByCartId(cart.getId());
        cartDto =cartMapper.fromEntityToDto(cart);
        List<CartDetailDto> cartDetailDtoList = cartDetailMapper.fromEntityToListCartDetailDto(cartDetails);
        for (CartDetailDto item : cartDetailDtoList)
        {
            ProductVariant productVariant = productVariantRepository.findById(item.getVariantId()).orElse(null);
            if (productVariant==null)
            {
                throw new RuntimeException("not found product");
            }
            if (item.getQuantity()>productVariant.getTotalStock() && productVariant.getTotalStock()!=0)
            {
                item.setQuantity(productVariant.getTotalStock());
            }
            Double price = ((item.getPrice()- (item.getPrice()*productVariant.getProduct().getSaleOff())/100))*item.getQuantity();
            item.setTotalPriceSell(price);

        }
        cartDto.setCartDetailDtos(cartDetailDtoList);
        apiMessageDto.setData(cartDto);
        apiMessageDto.setMessage("get list success");
        return apiMessageDto;
    }
    @PostMapping(value = "/add-product-into-cart",produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('CART_C')")
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
        CartDetail cartDetailExisted = cartDetailRepository.findByProductVariantIdAndCartId(createCartDetailForm.getVariantId(),cart.getId());
        if (cartDetailExisted!=null)
        {
            cartDetailExisted.setQuantity(cartDetailExisted.getQuantity()+createCartDetailForm.getQuantity());
            cartDetailRepository.save(cartDetailExisted);
        }else {
            CartDetail cartDetail = new CartDetail();
            cartDetail.setCart(cart);
            cartDetail.setProductVariant(productVariant);
            cartDetail.setQuantity(createCartDetailForm.getQuantity());
            cartDetailRepository.save(cartDetail);
        }
        cart.setTotalProduct(cartDetailRepository.countCartDetailByCartId(cart.getId()));
        cartRepository.save(cart);
       apiMessageDto.setMessage("add product into cart success");
       return apiMessageDto;
    }
    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('CART_D')")
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
        Cart cart = cartRepository.findById(exitCartDetail.getCart().getId()).orElse(null);
        cart.setTotalProduct(cartDetailRepository.countCartDetailByCartId(cart.getId()));
        cartRepository.save(cart);
        apiMessageDto.setMessage("Delete product success");
        return apiMessageDto;
    }
    @DeleteMapping(value = "/deleteAll", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('CART_DLA')")
    public ApiMessageDto<String> deleteAll() {
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
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
        cartDetailRepository.deleteAllByCartId(cart.getId());
        apiMessageDto.setMessage("delete All success");
        return apiMessageDto;

    }

    @Transactional
    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('CART_U')")
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
        List<UpdateCartDetailForm> cartDetailsList = updateCartForm.getCartDetails();
        for (UpdateCartDetailForm item : cartDetailsList)
        {
            CartDetail cartDetail = cartDetailRepository.findById(item.getCartDetailId()).orElse(null);
            cartDetailMapper.updateCartDetail(item,cartDetail);
            cartDetailRepository.save(cartDetail);
        }
        apiMessageDto.setMessage("update cart success");
        return apiMessageDto;
    }

    @PutMapping(value = "/update-item-inCart", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('CART_U')")
    public ApiMessageDto<String> updateItemInCart(@Valid @RequestBody UpdateCartDetailForm updateCartDetailForm, BindingResult bindingResult) {

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
      CartDetail cartDetail = cartDetailRepository.findById(updateCartDetailForm.getCartDetailId()).orElse(null);
      cartDetail.setQuantity(updateCartDetailForm.getQuantity());
      cartDetailRepository.save(cartDetail);
        apiMessageDto.setMessage("update cart success");
        return apiMessageDto;
    }




}
