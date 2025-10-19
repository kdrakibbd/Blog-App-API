package com.rakib.blog.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {
    String uploadImage(MultipartFile image, String folder) throws IOException;
    String deleteImage(String imageUrl, String folder) throws IOException;
}