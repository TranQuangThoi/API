package com.techmarket.api.dto.product;

import com.techmarket.api.dto.ABasicAdminDto;
import lombok.Data;


@Data
public class RateProductDto extends ABasicAdminDto {
    private String name;
    private String image;

}
