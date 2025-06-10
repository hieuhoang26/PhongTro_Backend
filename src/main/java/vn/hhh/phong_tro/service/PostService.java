package vn.hhh.phong_tro.service;

import org.springframework.data.domain.Pageable;
import vn.hhh.phong_tro.dto.request.post.PostFilterRequest;
import vn.hhh.phong_tro.dto.request.post.CreatePostRequest;
import vn.hhh.phong_tro.dto.request.post.UpdatePostRequest;
import vn.hhh.phong_tro.dto.response.PageResponse;
import vn.hhh.phong_tro.dto.response.post.PostDetailResponse;
import vn.hhh.phong_tro.dto.response.post.PostList;
import vn.hhh.phong_tro.model.Post;
import vn.hhh.phong_tro.util.PostStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface PostService {
//    void addPost()
    PostDetailResponse getPostById(Long id);
    String createPost(CreatePostRequest dto);
    void updatePost(Long id, UpdatePostRequest dto);

    void changePostStatus(Long id, PostStatus status);

//    void renewVip(Long postId, Integer isVip,  LocalDateTime vipExpiryDate);
    void deletePost(Long id);

     PageResponse getPostsByUserAndStatus(Long userId, PostStatus status, int page, int size, String sortDirection);

    PageResponse<?> advanceSearch(PostFilterRequest filter, Pageable pageable, Integer userId);

    List<PostList> getNearby(Double lat, Double lng, Integer typeId);

    List<PostList> getLatest(Integer n);

    List<PostList> getNearDistrict(Integer type ,Integer districtId, Integer postId);

    Post getById(Long id);

    void save(Post post);


}
