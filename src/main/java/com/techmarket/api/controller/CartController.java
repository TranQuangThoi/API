package com.techmarket.api.controller;

import com.techmarket.api.dto.ApiMessageDto;
import com.techmarket.api.dto.ErrorCode;
import com.techmarket.api.dto.ResponseListDto;
import com.techmarket.api.dto.cart.CartDto;
import com.techmarket.api.mapper.ProductVariantMapper;
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

        saveCartInCookie(response,cartItems);
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
    private void saveCartInCookie(HttpServletResponse response, List<CartDto> cartItems) {
        List<String> cartItemStrings = new ArrayList<>();
        for (CartDto cartItem : cartItems) {
            cartItemStrings.add(cartItem.getProductVariantId() + ":" + cartItem.getQuantity() + ":" +cartItem.getPrice() +":"
                    + cartItem.getName() +":"+cartItem.getColor() +":" +cartItem.getImage());
        }

        String encodedCartValue = String.join(",", cartItemStrings);

        //  để mã hóa giá trị của Cookie, đảm bảo rằng các ký tự đặc biệt như dấu , được mã hóa đúng cách  -> %c2 thay cho dấu ,
        String encodedCookieValue = URLEncoder.encode(encodedCartValue, StandardCharsets.UTF_8);

        Cookie cookie = new Cookie("cart", encodedCookieValue);
        cookie.setMaxAge(30 * 24 * 60 * 60); // Expire in 30 days
        response.addCookie(cookie);
    }
}
