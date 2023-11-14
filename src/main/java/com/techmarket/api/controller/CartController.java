package com.techmarket.api.controller;

import com.techmarket.api.cookie.cookie;
import com.techmarket.api.dto.ApiMessageDto;
import com.techmarket.api.dto.ErrorCode;
import com.techmarket.api.dto.ResponseListDto;
import com.techmarket.api.dto.cart.CartDto;
import com.techmarket.api.model.Product;
import com.techmarket.api.model.ProductVariant;
import com.techmarket.api.repository.ProductRepository;
import com.techmarket.api.repository.ProductVariantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CartController {

    @Autowired
    private ProductVariantRepository productVariantRepository;
    @Autowired
    private cookie cookie;
    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/")
    public ApiMessageDto<ResponseListDto<List<CartDto>>> viewCart(HttpServletRequest request) {
        List<CartDto> cartItems = cookie.getCartItemsFromCookie(request);
        ApiMessageDto<ResponseListDto<List<CartDto>>> apiMessageDto = new ApiMessageDto<>();
        ResponseListDto<List<CartDto>> responseListDto = new ResponseListDto<>();

        responseListDto.setContent(cartItems);
        apiMessageDto.setData(responseListDto);
        apiMessageDto.setMessage("get cart success");
        return apiMessageDto;
    }
    @PostMapping("/")
    public ApiMessageDto<String> addToCart(@RequestParam Long productVariantId, @RequestParam int quantity, HttpServletRequest request, HttpServletResponse response) {

        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        List<CartDto> cartItems = cookie.getCartItemsFromCookie(request);

        ProductVariant productVariant = productVariantRepository.findById(productVariantId).orElse(null);
        if (productVariant==null)
        {
            apiMessageDto.setMessage("Not found product");
            apiMessageDto.setCode(ErrorCode.PRODUCT_VARIANT_ERROR_NOT_FOUND);
            return apiMessageDto;
        }
        Double price ;
        if (productVariant.getPrice()!=null && productVariant.getPrice()!=0)
        {

            price=productVariant.getPrice();
        }else {
            price=productVariant.getProduct().getPrice();
        }
        Optional<CartDto> existingItem = cartItems.stream()
                .filter(item -> item.getProductVariantId().equals(productVariantId))
                .findFirst();

        if (existingItem.isPresent()) {
            existingItem.get().setQuantity(existingItem.get().getQuantity() + quantity);
            existingItem.get().setPrice(existingItem.get().getQuantity() * price);
            if (productVariant.getTotalStock() < existingItem.get().getQuantity())
            {
                apiMessageDto.setMessage("Số lượng hàng trong kho không đủ");
                return apiMessageDto;
            }
        } else {
            cartItems.add(new CartDto(productVariantId, quantity,price*quantity,
                    productVariant.getProduct().getName(),productVariant.getColor(),productVariant.getProduct().getImage()));
            if (productVariant.getTotalStock() < quantity)
            {
                apiMessageDto.setMessage("Số lượng hàng trong kho không đủ");
                return apiMessageDto;
            }
        }

        cookie.saveCartInCookie(request,response,cartItems);
        apiMessageDto.setMessage("Product added to cart success");
        return apiMessageDto;
    }

    @DeleteMapping("/")
    public ApiMessageDto<String> removeFromCart(@RequestParam Long productVariantId, HttpServletRequest request, HttpServletResponse response) {
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        List<CartDto> cartItems = cookie.getCartItemsFromCookie(request);

        Optional<CartDto> existingItem = cartItems.stream()
                .filter(item -> item.getProductVariantId().equals(productVariantId))
                .findFirst();

        if (existingItem.isPresent()) {
            cartItems.remove(existingItem.get());
            cookie.saveCartInCookie(request,response, cartItems);
            apiMessageDto.setMessage("Product removed from cart successfully");
        } else {
            apiMessageDto.setMessage("Product not found in cart");
            apiMessageDto.setCode(ErrorCode.PRODUCT_VARIANT_ERROR_NOT_FOUND);
        }

        return apiMessageDto;
    }
    @PutMapping("/")
    public ApiMessageDto<String> updateCartItemQuantity(@RequestParam Long productVariantId, @RequestParam int quantity, HttpServletRequest request, HttpServletResponse response) {
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        List<CartDto> cartItems = cookie.getCartItemsFromCookie(request);

        Optional<CartDto> existingItem = cartItems.stream()
                .filter(item -> item.getProductVariantId().equals(productVariantId))
                .findFirst();

        if (existingItem.isPresent()) {
            ProductVariant productVariant = productVariantRepository.findById(productVariantId).orElse(null);
            if (productVariant.getTotalStock()<= quantity)
            {
                apiMessageDto.setMessage("Số lượng hàng trong kho không đủ");
                return apiMessageDto;
            }
            Product product = productRepository.findById(productVariant.getProduct().getId()).orElse(null);
            Double price=0.0;
            if (productVariant.getPrice()!=null && productVariant.getPrice()!=0)
            {
                price = productVariant.getPrice() * quantity;
            }else {
                price = product.getPrice() * quantity;
            }
            existingItem.get().setQuantity(quantity);
            existingItem.get().setPrice(price);
            cookie.saveCartInCookie(request,response, cartItems);
            apiMessageDto.setMessage("Cart item quantity updated successfully");
        } else {
            apiMessageDto.setMessage("Product not found in cart");
            apiMessageDto.setCode(ErrorCode.PRODUCT_VARIANT_ERROR_NOT_FOUND);
        }
        return apiMessageDto;
    }


}
