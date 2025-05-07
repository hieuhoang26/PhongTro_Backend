package vn.hhh.phong_tro.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import vn.hhh.phong_tro.dto.response.post.PostList;
import vn.hhh.phong_tro.model.Post;
import vn.hhh.phong_tro.model.PostImage;
import vn.hhh.phong_tro.service.FavoriteService;
import vn.hhh.phong_tro.util.Uri;

import java.util.List;
import java.util.Set;

@Controller
@RequiredArgsConstructor
@RequestMapping(Uri.Favor)
@Slf4j
public class FavoriteController {
    private final FavoriteService favoriteService;


    // Thích bài đăng
    @PostMapping()
    public ResponseEntity<?> likePost(@RequestParam Long postId, @RequestParam Long userId) {
        favoriteService.addFavorite(userId, postId);
        return ResponseEntity.ok().body("Post liked");
    }

    // Bỏ thích bài đăng
    @DeleteMapping()
    public ResponseEntity<?> unlikePost(@RequestParam Long postId, @RequestParam Long userId) {
        favoriteService.removeFavorite(userId, postId);
        return ResponseEntity.ok().body("Post unliked");
    }

    // Lấy danh sách bài đăng đã thích
    @GetMapping
    public ResponseEntity<?> getLikedPosts(@RequestParam Long userId) {
        List<Post> likedPosts = favoriteService.getFavoritePosts(userId);
        List<PostList> res = likedPosts.stream().map(post -> {
            String fullAddress = post.getAddress();
            String shortAddress = "";
            String[] parts = fullAddress.split(", ");
            if (parts.length >= 2) {
                shortAddress = parts[parts.length - 2] + ", " + parts[parts.length - 1];
            }
            List<String> imageUrls = post.getImages()
                    .stream()
                    .map(PostImage::getImageUrl)
                    .toList();

            return PostList.builder()
                    .id(post.getId())
                    .title(post.getTitle())
                    .description(post.getDescription())
                    .price(post.getPrice())
                    .area(post.getArea())
                    .address(shortAddress)
                    .images(imageUrls)
                    .isLike(true)
                    .isVip(post.getIsVip())
                    .username(post.getUser().getName())
                    .phone(post.getUser().getPhone())
                    .createdAt(post.getCreatedAt())
                    .build();
        }).toList();
        return ResponseEntity.ok(res);
    }




}
