package com.rakib.blog.services.impl;

import com.rakib.blog.config.AppConstants;
import com.rakib.blog.entities.Category;
import com.rakib.blog.entities.Post;
import com.rakib.blog.entities.User;
import com.rakib.blog.exceptions.ImageSizeExceededException;
import com.rakib.blog.exceptions.ResourceNotFoundException;
import com.rakib.blog.exceptions.UnauthorizedException;
import com.rakib.blog.payloads.ApiResponse;
import com.rakib.blog.payloads.PostDto;
import com.rakib.blog.payloads.PostResponse;
import com.rakib.blog.repository.CategoryRepo;
import com.rakib.blog.repository.PostRepo;
import com.rakib.blog.repository.UserRepo;
import com.rakib.blog.services.ImageService;
import com.rakib.blog.services.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepo postRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private ImageService imageService;

    @Override
    public ApiResponse createPost(PostDto postDto, Integer userid, Integer categoryId, MultipartFile image) throws Exception {

        User user = this.userRepo.findById(userid)
                .orElseThrow(()->new ResourceNotFoundException("user", "user id", userid));
        Category category = this.categoryRepo.findById(categoryId)
                .orElseThrow(()->new ResourceNotFoundException("Category", "category id", categoryId));

        Post post = new Post();
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setCreatedAt(LocalDateTime.now());
        post.setUser(user);
        post.setCategory(category);

        if (image != null && !image.isEmpty()) {
            if (image.getSize() > AppConstants.POST_MAX_IMAGE_SIZE) {
                throw new ImageSizeExceededException("Image size must not exceed " + (AppConstants.POST_MAX_IMAGE_SIZE / (1024 * 1024)) + " MB");
            }
            String imageUrl = this.imageService.uploadImage(image, AppConstants.POST_FOLDER);
            post.setImageUrl(imageUrl);
        }

        this.postRepo.save(post);

        return new ApiResponse("Post created successfully", true);
    }

    @Override
    public ApiResponse updatePost(PostDto postDto, Integer categoryId, Integer postId, Integer userId, MultipartFile image) throws Exception {
        Post post = this.postRepo.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "Id", postId));
        Category category = this.categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category", "Id", categoryId));

        if (!post.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("You are not authorized to update this post");
        }

        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setUpdatedAt(LocalDateTime.now());
        post.setCategory(category);

        if (image != null && !image.isEmpty()) {
            if (image.getSize() > AppConstants.POST_MAX_IMAGE_SIZE) {
                throw new ImageSizeExceededException("Image size must not exceed " + (AppConstants.POST_MAX_IMAGE_SIZE / (1024 * 1024)) + " MB");
            }
            if (post.getImageUrl() != null) {
                this.imageService.deleteImage(post.getImageUrl(), AppConstants.POST_FOLDER);
            }

            String imageUrl = this.imageService.uploadImage(image, AppConstants.POST_FOLDER);
            post.setImageUrl(imageUrl);
        }

        this.postRepo.save(post);

        return new ApiResponse("Post updated successfully", true);
    }

    @Override
    public ApiResponse deletePost(Integer postId, Integer userId) {
        Post post = this.postRepo.findById(postId).orElseThrow(()-> new ResourceNotFoundException("Post", "Id", postId));
        User user = this.userRepo.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User", "Id", userId));

        if (post.getUser().getId().equals(userId) || user.getRole().equals("ADMIN")) {
            if (post.getImageUrl() != null && !post.getImageUrl().isEmpty()) {
                try {
                    this.imageService.deleteImage(post.getImageUrl(), AppConstants.POST_FOLDER);
                } catch (IOException e) {
                    throw new RuntimeException("Failed to delete image: " + e.getMessage());
                }
            }
            this.postRepo.delete(post);
            return new ApiResponse("Post deleted successfully", true);
        } else {
            throw new UnauthorizedException("You are not authorized to delete this post");
        }
    }

    @Override
    public PostResponse getAllPost(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {

        Sort sort = null;

        if (sortDir.equalsIgnoreCase("asc")) {
            sort = Sort.by(sortBy).ascending();
        } else {
            sort = Sort.by(sortBy).descending();
        }

        Pageable p = PageRequest.of(pageNumber, pageSize, sort);
        Page<Post> pagePost = this.postRepo.findAll(p);
        List<Post> allPosts = pagePost.getContent();

        List<PostDto> postDtos = allPosts.stream().map((post) -> this.modelMapper.map(post, PostDto.class)).collect(Collectors.toList());

        PostResponse postResponse = new PostResponse();
        postResponse.setContent(postDtos);
        postResponse.setPageNumber(pagePost.getNumber());
        postResponse.setPageSize(pagePost.getSize());
        postResponse.setTotalElements(pagePost.getTotalElements());
        postResponse.setTotalPages(pagePost.getTotalPages());
        postResponse.setLastPage(pagePost.isLast());

        return postResponse;
    }

    @Override
    public PostDto getPostById(Integer postId) {
        Post post = this.postRepo.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "Id", postId));
        return this.modelMapper.map(post, PostDto.class);
    }

    @Override
    public PostResponse getPostByCategory(Integer categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {

        Category category = this.categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "Id", categoryId));

        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<Post> pagePosts = this.postRepo.findByCategory(category, pageable);

        List<PostDto> postDtos = pagePosts.getContent()
                .stream()
                .map(post -> this.modelMapper.map(post, PostDto.class))
                .collect(Collectors.toList());

        // Prepare response
        PostResponse postResponse = new PostResponse();
        postResponse.setContent(postDtos);
        postResponse.setPageNumber(pagePosts.getNumber());
        postResponse.setPageSize(pagePosts.getSize());
        postResponse.setTotalElements(pagePosts.getTotalElements());
        postResponse.setTotalPages(pagePosts.getTotalPages());
        postResponse.setLastPage(pagePosts.isLast());

        return postResponse;
    }

    @Override
    public PostResponse getPostsByUser(Integer userId, Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {

        User user = this.userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));

        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<Post> pagePosts = this.postRepo.findByUser(user, pageable);

        List<PostDto> postDtos = pagePosts.getContent()
                .stream()
                .map(post -> this.modelMapper.map(post, PostDto.class))
                .collect(Collectors.toList());

        PostResponse postResponse = new PostResponse();
        postResponse.setContent(postDtos);
        postResponse.setPageNumber(pagePosts.getNumber());
        postResponse.setPageSize(pagePosts.getSize());
        postResponse.setTotalElements(pagePosts.getTotalElements());
        postResponse.setTotalPages(pagePosts.getTotalPages());
        postResponse.setLastPage(pagePosts.isLast());

        return postResponse;
    }

    @Override
    public PostResponse searchPost(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<Post> pagePosts = this.postRepo.findByTitleContaining(keyword, pageable);

        List<PostDto> postDtos = pagePosts.getContent()
                .stream()
                .map(post -> this.modelMapper.map(post, PostDto.class))
                .collect(Collectors.toList());

        PostResponse postResponse = new PostResponse();
        postResponse.setContent(postDtos);
        postResponse.setPageNumber(pagePosts.getNumber());
        postResponse.setPageSize(pagePosts.getSize());
        postResponse.setTotalElements(pagePosts.getTotalElements());
        postResponse.setTotalPages(pagePosts.getTotalPages());
        postResponse.setLastPage(pagePosts.isLast());

        return postResponse;
    }

    @Override
    public PostResponse getMyPosts(Integer userId, Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        User user = this.userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));

        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<Post> pagePosts = this.postRepo.findByUser(user, pageable);

        List<PostDto> postDtos = pagePosts.getContent()
                .stream()
                .map(post -> this.modelMapper.map(post, PostDto.class))
                .collect(Collectors.toList());

        PostResponse postResponse = new PostResponse();
        postResponse.setContent(postDtos);
        postResponse.setPageNumber(pagePosts.getNumber());
        postResponse.setPageSize(pagePosts.getSize());
        postResponse.setTotalElements(pagePosts.getTotalElements());
        postResponse.setTotalPages(pagePosts.getTotalPages());
        postResponse.setLastPage(pagePosts.isLast());

        return postResponse;
    }
}
