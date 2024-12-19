package com.techmarket.api.dto.voucher;

import com.techmarket.api.dto.ABasicAdminDto;
import lombok.Data;

import javax.persistence.Column;
import java.util.Date;

@Data
public class VoucherDto extends ABasicAdminDto {

    private Long id;
    private String title;
    private String content;
    private Integer percent;
    private Date expired;
    private Integer kind;
    private Integer amount;
    private Integer priceMax;

}
