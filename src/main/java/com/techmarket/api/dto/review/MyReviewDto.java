package com.techmarket.api.dto.review;

import com.techmarket.api.dto.ABasicAdminDto;
import lombok.Data;

@Data
public class MyReviewDto extends ABasicAdminDto {
    private Long id;
    private String message;
    private String productName;
    private Long  productId;
    private Integer star;
}
