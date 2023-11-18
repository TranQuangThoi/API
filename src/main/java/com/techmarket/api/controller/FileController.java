package com.techmarket.api.controller;

import com.techmarket.api.dto.ApiMessageDto;
import com.techmarket.api.dto.file.UploadFileDto;
import com.techmarket.api.form.file.UploadFileForm;
import com.techmarket.api.service.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/v1/file")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class FileController {

    @Autowired
    private CloudinaryService cloudinaryService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<UploadFileDto> upload(@Valid UploadFileForm uploadFileForm, BindingResult bindingResult) {
        ApiMessageDto<UploadFileDto> apiMessageDto = new ApiMessageDto<>();

        try {
            String filePath = cloudinaryService.upload(uploadFileForm.getFile());
            UploadFileDto uploadFileDto = new UploadFileDto();
            uploadFileDto.setFilePath(filePath);

            apiMessageDto.setData(uploadFileDto);
            apiMessageDto.setMessage("File uploaded successfully");
        } catch (IOException e) {
            apiMessageDto.setMessage("Error uploading file: " + e.getMessage());
        }

        return apiMessageDto;
    }
}
