package com.rakib.blog.services.impl;

import com.rakib.blog.exceptions.ImageFormatException;
import com.rakib.blog.exceptions.ResourceNotFoundException;
import com.rakib.blog.services.ImageService;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryServiceImpl implements ImageService {

    @Autowired
    Cloudinary cloudinary;

    @Override
    public String uploadImage(MultipartFile image, String folder) throws IOException {

        if (!isImage(image)) {
            throw new ImageFormatException("Uploaded file is not an image!");
        }

        Map<?, ?> uploadResult = this.cloudinary.uploader()
                .upload(image.getBytes(), ObjectUtils.asMap(
                        "folder", folder,
                        "quality", "auto:low",
                        "format" , "jpg"
                ));

        return uploadResult.get("secure_url").toString();
    }

    @Override
    public String deleteImage(String imageUrl, String folder) throws IOException {

        if (imageUrl == null || imageUrl.isEmpty()) {
            throw new ResourceNotFoundException("Image doesn't found");
        }

        String publicId = folder+"/"+extractPublicId(imageUrl);

        Map destroy = this.cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());

        return "Image deleted successfully";
    }

    private boolean isImage(MultipartFile image) {
        try (var is = image.getInputStream()) {
            return new Tika().detect(is).startsWith("image/");
        } catch (IOException e) {
            return false;
        }
    }

    private String extractPublicId(String imageUrl) {
        return imageUrl.substring(imageUrl.lastIndexOf("/") + 1, imageUrl.lastIndexOf("."));
    }
}
