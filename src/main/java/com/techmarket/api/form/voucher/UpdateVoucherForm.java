package com.techmarket.api.form.voucher;

import com.techmarket.api.validation.VoucherKind;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class UpdateVoucherForm {

    @NotNull(message = "id cant not be null")
    @ApiModelProperty(name = "id", required = true)
    private Long id;
    @NotEmpty(message = "title cant not be empty")
    @ApiModelProperty(name = "title", required = true)
    private String title;
    @ApiModelProperty(name = "content")
    private String content;
    @NotNull(message = "percent cant not be null")
    @ApiModelProperty(name = "percent", required = true)
    private Double percent;
    @ApiModelProperty(name = "expired")
    private Date expired;
    @VoucherKind
    @NotNull(message = "kind cant not be null")
    @ApiModelProperty(name = "kind", required = true)
    private Integer kind;
    @NotNull(message = "statusf cant not be null")
    @ApiModelProperty(name = "status", required = true)
    private Integer status;
    @NotNull(message = "amount cant not be null")
    @ApiModelProperty(name = "amount", required = true)
    private Integer amount;

}
