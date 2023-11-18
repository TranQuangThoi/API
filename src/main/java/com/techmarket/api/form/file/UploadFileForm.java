package com.techmarket.api.form.file;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@Data
@ApiModel
public class UploadFileForm {

    @NotNull(message = "file is required")
    @ApiModelProperty(name = "file", required = true)
    private MultipartFile file;
}
