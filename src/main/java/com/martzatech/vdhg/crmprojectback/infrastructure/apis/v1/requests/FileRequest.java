package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@Getter
public class FileRequest {
    private  MultipartFile file;
    private String name;
    private String extension;
    private String text;
}
