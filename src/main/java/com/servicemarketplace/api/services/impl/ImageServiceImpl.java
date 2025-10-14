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

    @Override
    public void delete(String imagePath) {
        if (imagePath.equals(imageConfig.getDefaultImage())) {
            return;
        }
        //Crea el cliente de cloudinary
        Cloudinary cloudinary = new Cloudinary(imageConfig.getUrl());

        /*
        Previene que la imagen siga siendo accesible desde el cache de cloudinary
        despues de ser borrda
        */
        Map params = ObjectUtils.asMap(
            "invalidate", true
        );
        try {
            String imageName = parseImagePath(imagePath);
            // Elimina la imagen
            cloudinary.uploader().destroy(imageName, params);
        }
        catch (IOException e) {
            System.out.println("Ocurrio un error al borrar la imagen: " + e.getMessage());
        }
    }


    /**
     * Parsea el link de cloudinary para obtener el nombre de la imagen
     *
     * @return String - Nombre de la imagen
     */
    @Override
    public String parseImagePath(String imagePath) {
        // Corta desde /upload/ hasta el final
        String withoutUpload = imagePath.substring(imagePath.indexOf("/upload/") + 8);

        // Quita la parte de versión si existe (v + números + /)
        withoutUpload = withoutUpload.replaceFirst("^v[0-9]+/", "");

        // Quita extensión
        return withoutUpload.replaceFirst("\\.[^.]+$", "");
    }
}
