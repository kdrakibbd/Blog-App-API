package com.rakib.blog.services.impl;

import com.rakib.blog.entities.Category;
import com.rakib.blog.entities.Post;
import com.rakib.blog.entities.User;
import com.rakib.blog.exceptions.ImageSizeExceededException;
import com.rakib.blog.exceptions.ResourceNotFoundException;
import com.rakib.blog.exceptions.UnauthorizedException;
import com.rakib.blog.mappers.PostMapper;
import com.rakib.blog.payloads.PaginatedResponse;
import com.rakib.blog.payloads.PostRequest;
import com.rakib.blog.payloads.PostResponse;
import com.rakib.blog.repository.CategoryRepo;
import com.rakib.blog.repository.PostRepo;
import com.rakib.blog.repository.UserRepo;
import com.rakib.blog.services.ImageService;
import com.rakib.blog.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.rakib.blog.config.AppConstants.POST_FOLDER;
import static com.rakib.blog.config.AppConstants.POST_MAX_IMAGE_SIZE;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepo postRepo;

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private ImageService imageService;

    @Override
    public PostResponse createPost(PostRequest request, Integer userid, Integer categoryId, MultipartFile image) throws Exception {
        User user = this.userRepo.findById(userid)
                .orElseThrow(() -> new ResourceNotFoundException("User", "user id", userid));
        Category category = this.categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "category id", categoryId));

        Post post = this.postMapper.toEntity(request);
        post.setCreatedAt(LocalDateTime.now());
        post.setUser(user);
        post.setCategory(category);

        if (image != null && !image.isEmpty()) {
            if (image.getSize() > POST_MAX_IMAGE_SIZE) {
                throw new ImageSizeExceededException("Image size must not exceed " + (POST_MAX_IMAGE_SIZE / (1024 * 1024)) + " MB");
            }
            String imageUrl = this.imageService.uploadImage(image, POST_FOLDER);
            post.setImageUrl(imageUrl);
        }

        Post saved = this.postRepo.save(post);
        return this.postMapper.toResponse(saved);
    }

    @Override
    public PostResponse updatePost(PostRequest request, Integer categoryId, Integer postId, Integer userId, MultipartFile image) throws Exception {
        Post post = this.postRepo.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "Id", postId));
        Category category = this.categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "Id", categoryId));

        if (!post.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("You are not authorized to update this post");
        }

        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setUpdatedAt(LocalDateTime.now());
        post.setCategory(category);

        if (image != null && !image.isEmpty()) {
            if (image.getSize() > POST_MAX_IMAGE_SIZE) {
                throw new ImageSizeExceededException("Image size must not exceed " + (POST_MAX_IMAGE_SIZE / (1024 * 1024)) + " MB");
            }
            if (post.getImageUrl() != null) {
                this.imageService.deleteImage(post.getImageUrl(), POST_FOLDER);
            }
            String imageUrl = this.imageService.uploadImage(image, POST_FOLDER);
            post.setImageUrl(imageUrl);
        }

        Post updated = this.postRepo.save(post);
        return this.postMapper.toResponse(updated);
    }

    @Override
    public void deletePost(Integer postId, Integer userId) {
        Post post = this.postRepo.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "Id", postId));
        User user = this.userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));

        if (post.getUser().getId().equals(userId) || user.getRole().equals("ADMIN")) {
            if (post.getImageUrl() != null && !post.getImageUrl().isEmpty()) {
                try {
                    this.imageService.deleteImage(post.getImageUrl(), POST_FOLDER);
                } catch (Exception e) {
                    throw new RuntimeException("Failed to delete image: " + e.getMessage());
                }
            }
            this.postRepo.delete(post);
        } else {
            throw new UnauthorizedException("You are not authorized to delete this post");
        }
    }

    @Override
    public PaginatedResponse<PostResponse> getAllPost(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        return buildPaginatedResponse(this.postRepo.findAll(pageable));
    }

    @Override
    public PostResponse getPostById(Integer postId) {
        Post post = this.postRepo.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "Id", postId));
        return this.postMapper.toResponse(post);
    }

    @Override
    public PaginatedResponse<PostResponse> getPostByCategory(Integer categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        Category category = this.categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "Id", categoryId));
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        return buildPaginatedResponse(this.postRepo.findByCategory(category, pageable));
    }

    @Override
    public PaginatedResponse<PostResponse> getPostsByUser(Integer userId, Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        User user = this.userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        return buildPaginatedResponse(this.postRepo.findByUser(user, pageable));
    }

    @Override
    public PaginatedResponse<PostResponse> searchPost(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        return buildPaginatedResponse(this.postRepo.findByTitleContaining(keyword, pageable));
    }

    @Override
    public PaginatedResponse<PostResponse> getMyPosts(Integer userId, Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        User user = this.userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        return buildPaginatedResponse(this.postRepo.findByUser(user, pageable));
    }

    private PaginatedResponse<PostResponse> buildPaginatedResponse(Page<Post> page) {
        List<PostResponse> responses = page.getContent().stream()
                .map(this.postMapper::toResponse)
                .collect(Collectors.toList());
        return PaginatedResponse.of(responses, page.getNumber(), page.getSize(),
                page.getTotalElements(), page.getTotalPages(), page.isLast());
    }
}
