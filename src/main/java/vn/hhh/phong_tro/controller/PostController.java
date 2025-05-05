package vn.hhh.phong_tro.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vn.hhh.phong_tro.dto.request.post.CreatePostRequest;
import vn.hhh.phong_tro.dto.request.post.PostFilterRequest;
import vn.hhh.phong_tro.dto.request.post.UpdatePostRequest;
import vn.hhh.phong_tro.dto.response.PageResponse;
import vn.hhh.phong_tro.dto.response.ResponseData;
import vn.hhh.phong_tro.dto.response.ResponseError;
import vn.hhh.phong_tro.dto.response.post.PostDetailResponse;
import vn.hhh.phong_tro.service.PostService;
import vn.hhh.phong_tro.util.PostStatus;
import vn.hhh.phong_tro.util.Uri;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@RestController
@Validated
@Slf4j
@Tag(name = "Post Controller")
@RequestMapping(Uri.Post)
@RequiredArgsConstructor
public class PostController {

    final PostService postService;

    @GetMapping("/status")
    public ResponseEntity<PageResponse> getPostsByUserAndStatus(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) PostStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "desc") String sort // asc hoặc desc
    ) {
        return ResponseEntity.ok(postService.getPostsByUserAndStatus(userId, status, page, size, sort));
    }
    @Operation(summary = "Advance search", description = "Return list ")
    @GetMapping("/filter")
    public ResponseData<?> advanceSearch(
            Pageable pageable,
            @RequestParam(required = false) Long typeId,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Float minArea,
            @RequestParam(required = false) Float maxArea,
            @RequestParam(required = false) Long cityId,
            @RequestParam(required = false) Long districtId,
            @RequestParam(required = false) Long wardId,
            @RequestParam(required = false) String categoryIds, // nhận dạng chuỗi "1,2"
            @RequestParam(required = false) Integer isVip ,
            @RequestParam(required = false) String sortBy ,
            @RequestParam(required = false) String sortDirection
    ) {
        PostFilterRequest filterRequest = new PostFilterRequest();
        filterRequest.setTypeId(typeId);
        filterRequest.setMinPrice(minPrice);
        filterRequest.setMaxPrice(maxPrice);
        filterRequest.setMinArea(minArea);
        filterRequest.setMaxArea(maxArea);
        filterRequest.setCityId(cityId);
        filterRequest.setDistrictId(districtId);
        filterRequest.setWardId(wardId);
        filterRequest.setIsVip(isVip);
        filterRequest.setSortBy(sortBy);
        filterRequest.setSortDirection(sortDirection);

        if (categoryIds != null && !categoryIds.isBlank()) {
            List<Long> categoryIdList = Arrays.stream(categoryIds.split(","))
                    .map(String::trim)
                    .map(Long::valueOf)
                    .toList();
            filterRequest.setCategoryIds(categoryIdList);
        }

        return new ResponseData<>(HttpStatus.OK.value(), "Filter", postService.advanceSearch(filterRequest, pageable));
    }

    @Operation(summary = "Create post", description = "")
    @PostMapping()
    public ResponseData<?> createPost(@ModelAttribute CreatePostRequest request) {
        try {
            return new ResponseData<>(HttpStatus.OK.value(), "Create post success", postService.createPost(request));
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }
    @Operation(summary = "Update post", description = "")
    @PutMapping("/{postId}")
    public ResponseData<?> updatePost(@PathVariable Integer postId,@ModelAttribute UpdatePostRequest request) {
        try {
            postService.updatePost(Long.valueOf(postId),request);
            return new ResponseData<>(HttpStatus.OK.value(), "Update post success");
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @Operation(summary = "Get detail of post", description = "")
    @GetMapping("/{postId}")
    public ResponseData<?> updateStatus(@PathVariable Integer postId) {
        try {
            PostDetailResponse post = postService.getPostById(Long.valueOf(postId));
            return new ResponseData<>(HttpStatus.OK.value(), "Get detail of post successfully", post);
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }
    @Operation(summary = "Change Status of post", description = "")
    @PatchMapping("/{postId}")
    public ResponseData<?> changePostStatus(
            @PathVariable Long postId,
            @RequestBody PostStatus status) {
        try {
            postService.changePostStatus(postId, status);
            return new ResponseData<>(HttpStatus.OK.value(), "change status post successfully");
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }


    @Operation(summary = "Delete user permanently", description = "Send a request via this API to delete post permanently")
    @DeleteMapping("/{postId}")
    public ResponseData<?> deleteUser(@PathVariable @Min(value = 1, message = "userId must be greater than 0") long postId) {
        try {
            postService.deletePost(postId);
            return new ResponseData<>(HttpStatus.NO_CONTENT.value(), "Delete post successfully");
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Delete post fail");
        }
    }
}