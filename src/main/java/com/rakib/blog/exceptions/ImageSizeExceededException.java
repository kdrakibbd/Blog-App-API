package com.rakib.blog.exceptions;

public class ImageSizeExceededException extends RuntimeException {
    public ImageSizeExceededException(String message) {
        super(message);
    }
}
