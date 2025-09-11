package com.servicemarketplace.api.services.impl;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.servicemarketplace.api.config.CustomConfig.ImageConfig;
import com.servicemarketplace.api.services.ImageService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService{

    private final ImageConfig imageConfig;

    @Override
    public String upload(MultipartFile image) {
        if (image == null) {
            return imageConfig.getDefaultImage();
        }

        Map params = ObjectUtils.asMap(
            "use_filename", true,
            "unique_filename", false,
            "overwrite", true,
            "folder", imageConfig.getFolder()
        );

        //Crea el cliente de cloudinary
        Cloudinary cloudinary = new Cloudinary(imageConfig.getUrl());
        try {
            //Transfiere la imagen a un archivo temporal
            File tempFile = File.createTempFile("upload-", image.getOriginalFilename());
            image.transferTo(tempFile);

            //Obtiene los datos de carga de la imagen
            Map uploadResult = cloudinary.uploader().upload(tempFile, params);
            tempFile.delete();

            //Devuelve la url de la imagen
            return uploadResult.get("secure_url").toString();
        }
        catch (IOException e) {
            return null;
        }
    }
}
