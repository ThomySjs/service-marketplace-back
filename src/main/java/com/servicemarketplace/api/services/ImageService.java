package com.servicemarketplace.api.services;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    String upload(MultipartFile image);
}
