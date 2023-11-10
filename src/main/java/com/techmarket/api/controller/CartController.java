package com.techmarket.api.controller;

import com.techmarket.api.dto.ApiMessageDto;
import com.techmarket.api.dto.ErrorCode;
import com.techmarket.api.dto.ResponseListDto;
import com.techmarket.api.dto.cart.CartDto;
import com.techmarket.api.model.ProductVariant;
import com.techmarket.api.repository.ProductVariantRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/cart")
public class CartController {

    @Autowired
    private ProductVariantRepository productVariantRepository;

    @GetMapping("/get")
    public ApiMessageDto<ResponseListDto<List<CartDto>>> viewCart(HttpServletRequest request) {
        List<CartDto> cartItems = getCartItemsFromCookie(request);
        ApiMessageDto<ResponseListDto<List<CartDto>>> apiMessageDto = new ApiMessageDto<>();
        ResponseListDto<List<CartDto>> responseListDto = new ResponseListDto<>();

        responseListDto.setContent(cartItems);
        apiMessageDto.setData(responseListDto);
        apiMessageDto.setMessage("get cart success");
        return apiMessageDto;
    }
    @PostMapping("/add/{productVariantId}")
    public ApiMessageDto<String> addToCart(@PathVariable Long productVariantId, @RequestParam int quantity, HttpServletRequest request, HttpServletResponse response) {

        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        List<CartDto> cartItems = getCartItemsFromCookie(request);

        ProductVariant productVariant = productVariantRepository.findById(productVariantId).orElse(null);
        if (productVariant==null)
        {
            apiMessageDto.setMessage("Not found product");
            apiMessageDto.setCode(ErrorCode.PRODUCT_VARIANT_ERROR_NOT_FOUND);
            return apiMessageDto;
        }
        Double price ;
        if (productVariant.getPrice()!=null)
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
        } else {
            cartItems.add(new CartDto(productVariantId, quantity,price*quantity,
                    productVariant.getProduct().getName(),productVariant.getColor(),productVariant.getProduct().getImage()));
        }

        saveCartInCookie(request,response,cartItems);
        apiMessageDto.setMessage("Product added to cart success");
        return apiMessageDto;

    }
    private List<CartDto> getCartItemsFromCookie(HttpServletRequest request) {
        List<CartDto> cartItems = new ArrayList<>();
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("cart".equals(cookie.getName())) {
                    try {
                        String decodedValue = URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8);
                        String[] items = decodedValue.split(",");
                        for (String item : items) {
                            String[] parts = item.split(":");
                            Long productId = Long.parseLong(parts[0]);
                            int quantity = Integer.parseInt(parts[1]);
                            Double price = Double.parseDouble(parts[2]);
                            String name = parts[3];
                            String color = parts[4];
                            String image = parts[5];
                            cartItems.add(new CartDto(productId, quantity,price,name,color,image));
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }

        return cartItems;
    }

    @DeleteMapping("/delete/{productVariantId}")
    public ApiMessageDto<String> removeFromCart(@PathVariable Long productVariantId, HttpServletRequest request, HttpServletResponse response) {
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        List<CartDto> cartItems = getCartItemsFromCookie(request);

        Optional<CartDto> existingItem = cartItems.stream()
                .filter(item -> item.getProductVariantId().equals(productVariantId))
                .findFirst();

        if (existingItem.isPresent()) {
            cartItems.remove(existingItem.get());
            saveCartInCookie(request,response, cartItems);
            apiMessageDto.setMessage("Product removed from cart successfully");
        } else {
            apiMessageDto.setMessage("Product not found in cart");
            apiMessageDto.setCode(ErrorCode.PRODUCT_VARIANT_ERROR_NOT_FOUND);
        }

        return apiMessageDto;
    }
    @PutMapping("/update/{productVariantId}")
    public ApiMessageDto<String> updateCartItemQuantity(@PathVariable Long productVariantId, @RequestParam int quantity, HttpServletRequest request, HttpServletResponse response) {
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        List<CartDto> cartItems = getCartItemsFromCookie(request);

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
            existingItem.get().setQuantity(quantity);
            existingItem.get().setPrice(existingItem.get().getQuantity() * existingItem.get().getPrice());
            saveCartInCookie(request,response, cartItems);
            apiMessageDto.setMessage("Cart item quantity updated successfully");
        } else {
            apiMessageDto.setMessage("Product not found in cart");
            apiMessageDto.setCode(ErrorCode.PRODUCT_VARIANT_ERROR_NOT_FOUND);
        }
        return apiMessageDto;
    }
    private void saveCartInCookie(HttpServletRequest request ,HttpServletResponse response, List<CartDto> cartItems) {
        List<String> cartItemStrings = new ArrayList<>();
        for (CartDto cartItem : cartItems) {
            cartItemStrings.add(cartItem.getProductVariantId() + ":" + cartItem.getQuantity() + ":" +cartItem.getPrice() +":"
                    + cartItem.getName() +":"+cartItem.getColor() +":" +cartItem.getImage());
        }

        String encodedCartValue = String.join(",", cartItemStrings);
        //  để mã hóa giá trị của Cookie, đảm bảo rằng các ký tự đặc biệt như dấu , được mã hóa đúng cách  -> %c2 thay cho dấu ,
        String encodedCookieValue = URLEncoder.encode(encodedCartValue, StandardCharsets.UTF_8);

        Cookie[] existingCookies = request.getCookies();
        boolean cookieExists = false;

        if (existingCookies != null) {
            for (Cookie existingCookie : existingCookies) {
                if ("cart".equals(existingCookie.getName())) {
                    // Nếu có cookie đã tồn tại, chỉ cập nhật giá trị
                    encodedCookieValue = URLEncoder.encode(encodedCartValue, StandardCharsets.UTF_8);
                    existingCookie.setValue(encodedCookieValue);
                    existingCookie.setMaxAge(30 * 24 * 60 * 60);
                    response.addCookie(existingCookie);
                    cookieExists = true;
                    break;
                }
            }
        }

        if (!cookieExists) {
            Cookie cookie = new Cookie("cart", encodedCookieValue);
            cookie.setMaxAge(7 * 24 * 60 * 60);
            response.addCookie(cookie);
        }
    }
}
