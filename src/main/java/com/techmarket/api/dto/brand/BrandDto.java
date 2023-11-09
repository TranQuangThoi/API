package com.techmarket.api.dto.brand;

import com.techmarket.api.dto.ABasicAdminDto;
import lombok.Data;

@Data
public class BrandDto extends ABasicAdminDto {

    private String name;
    private String image;
}
