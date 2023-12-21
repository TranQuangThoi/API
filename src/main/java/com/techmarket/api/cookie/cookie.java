//package com.techmarket.api.cookie;
//
//import com.techmarket.api.dto.cart.CartDto;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.http.Cookie;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.net.URLDecoder;
//import java.net.URLEncoder;
//import java.nio.charset.StandardCharsets;
//import java.util.ArrayList;
//import java.util.List;
//
//@Component
//public class cookie {
//
//    public void saveCartInCookie(HttpServletRequest request , HttpServletResponse response, List<CartDto> cartItems) {
//        List<String> cartItemStrings = new ArrayList<>();
//        for (CartDto cartItem : cartItems) {
//            cartItemStrings.add(cartItem.getProductVariantId() + ":" + cartItem.getQuantity() + ":" +cartItem.getPrice() +":"
//                    + cartItem.getName() +":"+cartItem.getColor() +":" +cartItem.getImage());
//        }
//
//        String encodedCartValue = String.join(",", cartItemStrings);
//        //  để mã hóa giá trị của Cookie, đảm bảo rằng các ký tự đặc biệt như dấu , được mã hóa đúng cách  -> %c2 thay cho dấu ,
//        String encodedCookieValue = URLEncoder.encode(encodedCartValue, StandardCharsets.UTF_8);
//
//        Cookie[] existingCookies = request.getCookies();
//        boolean cookieExists = false;
//
//        if (existingCookies != null) {
//            for (Cookie existingCookie : existingCookies) {
//                if ("cart".equals(existingCookie.getName())) {
//                    // Nếu có cookie đã tồn tại, chỉ cập nhật giá trị
//                    encodedCookieValue = URLEncoder.encode(encodedCartValue, StandardCharsets.UTF_8);
//                    existingCookie.setValue(encodedCookieValue);
//                    existingCookie.setMaxAge(14 * 24 * 60 * 60);
//                    response.addCookie(existingCookie);
//                    cookieExists = true;
//                    break;
//                }
//            }
//        }
//
////        if (!cookieExists) {
////            Cookie cookie = new Cookie("cart", encodedCookieValue);
////            cookie.setMaxAge(7 * 24 * 60 * 60);
////            response.addCookie(cookie);
////        }
////        if (!cookieExists) {
////            String setCookieValue = "cart=" + encodedCookieValue +
////                    "; Max-Age=" + (14 * 24 * 60 * 60) +
//////                    "; Secure" +   // Set the Secure attribute
////                    "; HttpOnly" +
////                    "; SameSite=None";  // Set the SameSite attribute
////
////            response.setHeader("Set-Cookie", setCookieValue);
////        }
//        String setCookieValue = "cart=" + encodedCookieValue +
//                "; Max-Age=" + (14 * 24 * 60 * 60) +
//                "; HttpOnly" +
//                "; SameSite=Lax";
//        response.setHeader("Set-Cookie", setCookieValue);
//
//
//
//    }
//
//    public List<CartDto> getCartItemsFromCookie(HttpServletRequest request) {
//        List<CartDto> cartItems = new ArrayList<>();
//        Cookie[] cookies = request.getCookies();
//
//        if (cookies != null) {
//            for (Cookie cookie : cookies) {
//                if ("cart".equals(cookie.getName())) {
//                    try {
//                        String decodedValue = URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8);
//                        String[] items = decodedValue.split(",");
//                        for (String item : items) {
//                            String[] parts = item.split(":");
//                            Long productId = Long.parseLong(parts[0]);
//                            int quantity = Integer.parseInt(parts[1]);
//                            Double price = Double.parseDouble(parts[2]);
//                            String name = parts[3];
//                            String color = parts[4];
//                            String image = parts[5];
//                            cartItems.add(new CartDto(productId, quantity,price,name,color,image));
//                        }
//                    } catch (NumberFormatException e) {
//                        e.printStackTrace();
//                    }
//                    break;
//                }
//            }
//        }
//
//        return cartItems;
//    }
//
//    public void clearCartCookie(HttpServletRequest request, HttpServletResponse response) {
//        Cookie[] cookies = request.getCookies();
//
//        if (cookies != null) {
//            for (Cookie cookie : cookies) {
//                if ("cart".equals(cookie.getName())) {
//                    cookie.setValue("");
//                    cookie.setPath("/v1");
//                    cookie.setMaxAge(0);
//                    response.addCookie(cookie);
//                    break;
//                }
//            }
//        }
//    }
//
//}
