package com.techmarket.api.dto.review;

import com.techmarket.api.dto.ABasicAdminDto;
import com.techmarket.api.dto.user.UserAutoCompleteDto;
import lombok.Data;

@Data
public class ReviewDto extends ABasicAdminDto {
    private Long id;
    private String message;
    private UserAutoCompleteDto userDto;
    private Integer star;
}
