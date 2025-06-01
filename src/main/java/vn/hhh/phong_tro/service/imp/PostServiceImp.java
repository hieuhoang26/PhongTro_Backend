package vn.hhh.phong_tro.service.imp;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.hhh.phong_tro.dto.request.post.PostFilterRequest;
import vn.hhh.phong_tro.dto.request.post.CreatePostRequest;
import vn.hhh.phong_tro.dto.request.post.UpdatePostRequest;
import vn.hhh.phong_tro.dto.response.PageResponse;
import vn.hhh.phong_tro.dto.response.post.PostList;
import vn.hhh.phong_tro.dto.response.post.PostDetailResponse;
import vn.hhh.phong_tro.exception.ResourceNotFoundException;
import vn.hhh.phong_tro.model.*;
import vn.hhh.phong_tro.repository.*;
import vn.hhh.phong_tro.repository.specification.PostSpecificationsBuilder;
import vn.hhh.phong_tro.service.*;
import vn.hhh.phong_tro.util.GeoUtil;
import vn.hhh.phong_tro.util.PostStatus;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static vn.hhh.phong_tro.util.GeoUtil.generateGeoHash;

@Service
@RequiredArgsConstructor
public class PostServiceImp implements PostService {
    final PostRepository postRepository;
    final CategoryRepository categoryRepository;
    final PostTypeRepository postTypeRepository;
    final WardRepository wardRepository;
    final ImageRepository imageRepository;
    final FavoriteService favoriteService;
    final CustomGeoHash customGeoHash;
    final VerifyService verifyService;

    final UserService userService;
    final S3Service s3Service;

    @Override
    public PostDetailResponse getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        List<Category> allCategories = categoryRepository.findAll();

        Set<Long> postCategoryIds = post.getCategories()
                .stream()
                .map(Category::getId)
                .collect(Collectors.toSet());
        List<String> categoryDTOs = postCategoryIds.stream()
                .map(String::valueOf)
                .toList();

        List<String> imageUrls = post.getImages()
                .stream()
                .map(PostImage::getImageUrl)
                .toList();



        return PostDetailResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .description(post.getDescription())
                .price(post.getPrice())
                .area(post.getArea())
                .address(post.getAddress())
                .type(Math.toIntExact(post.getType() != null ? post.getType().getId() : null))
                .isVip(post.getIsVip())
                .vipExpiryDate(post.getVipExpiryDate())
                .status(post.getStatus())
                .username(post.getUser().getName())
                .phone(post.getUser().getPhone())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .categories(categoryDTOs)
                .images(imageUrls)
                .wardId(Math.toIntExact(post.getPostAddress().getWard().getId()))
                .districtId(Math.toIntExact(post.getPostAddress().getWard().getDistrict().getId()))
                .cityId(Math.toIntExact(post.getPostAddress().getWard().getDistrict().getCity().getId()))
                .nameContact(post.getNameContact() != null ? post.getNameContact() : post.getUser().getName())
                .phoneContact(post.getPhoneContact() != null ? post.getPhoneContact() : post.getUser().getPhone())
                .build();


    }

    @Override
    @Transactional
    public String createPost(CreatePostRequest request) {


        User user = userService.getById(request.getUserId());
        boolean isVerified = verifyService.checkIfUserVerified(user.getId());
        Long postCount = postRepository.countByUserId(user.getId());

        if (!isVerified && postCount >= 2 && !Objects.equals(user.getRole().getName(), "ADMIN")) {
            throw new RuntimeException("Tài khoản chưa xác thực CCCD, chỉ được đăng tối đa 2 bài.");
        }


        PostType type = postTypeRepository.findById(request.getTypeId())
                .orElseThrow(() -> new RuntimeException("Type not found"));

        Ward ward = wardRepository.findById(Long.valueOf(request.getWardId()))
                .orElseThrow(() -> new RuntimeException("Ward not found"));

        Set<Category> categoryEntities = request.getCategories().stream()
                .map(id -> categoryRepository.findById(Long.valueOf(id))
                        .orElseThrow(() -> new RuntimeException("Category not found: " + id)))
                .collect(Collectors.toSet());

        Post post = new Post();
        post.setUser(user);
        post.setTitle(request.getTitle());
        post.setDescription(request.getDescription());
        post.setPrice(request.getPrice());
        post.setArea(request.getArea());
        post.setAddress(request.getFullAddress());
        post.setType(type);
        post.setIsVip(request.getIsVip());
        post.setVipExpiryDate(request.getVipExpiryDate());
        post.setStatus(request.getStatus() != null ? request.getStatus() : PostStatus.PENDING);
        post.setCategories(categoryEntities);
        post.setNameContact(request.getNameContact());
        post.setPhoneContact(request.getPhoneContact());
        // Save để có postId
        Post savedPost = postRepository.save(post);

        // Save PostAddress
        PostAddress address = new PostAddress();
        address.setPost(savedPost);
        address.setWard(ward);
        address.setDetailAddress(request.getDetailAddress());
        address.setLatitude(request.getLatitude());
        address.setLongitude(request.getLongitude());
        address.setGeoHash(generateGeoHash(request.getLatitude(),request.getLongitude()));
        savedPost.setPostAddress(address);

        // Upload images
        List<PostImage> postImages = new ArrayList<>();
        if (request.getImages() != null) {
            for (int i = 0; i < request.getImages().length; i++) {
                MultipartFile file = request.getImages()[i];

                String url = s3Service.upload(file);

                PostImage image = new PostImage();
                image.setPost(savedPost);
                image.setImageUrl(url);
//                image.setIsThumbnail(i == 0);
                postImages.add(image);
            }
        }
        // Upload video (nếu có)
        if (request.getVideo() != null && !request.getVideo().isEmpty()) {
            String videoUrl = s3Service.upload(request.getVideo());
            PostImage video = new PostImage();
            video.setPost(savedPost);
            video.setImageUrl(videoUrl);
            postImages.add(video);
        } else if (request.getVideoLink() != null) {
            savedPost.setDescription(savedPost.getDescription() + "\nVideo: " + request.getVideoLink());
        }
        savedPost.setImages(postImages);
        postRepository.save(savedPost);
        return savedPost.getId().toString();
    }


    @Override
    public void updatePost(Long postId, UpdatePostRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        if (request.getTitle() != null) post.setTitle(request.getTitle());
        if (request.getDescription() != null) post.setDescription(request.getDescription());
        if (request.getPrice() != null) post.setPrice(request.getPrice());
        if (request.getArea() != null) post.setArea(request.getArea());
        if (request.getFullAddress() != null) post.setAddress(request.getFullAddress());
        if (request.getTypeId() != null) {
            PostType type = postTypeRepository.findById(request.getTypeId())
                    .orElseThrow(() -> new RuntimeException("Type not found"));
            post.setType(type);
        }
        if (request.getIsVip() != null) post.setIsVip(request.getIsVip() ? 1 : 0);
        if (request.getVipExpiryDate() != null) post.setVipExpiryDate(request.getVipExpiryDate());
        if (request.getStatus() != null) post.setStatus(request.getStatus());

        // Update address
        if (post.getPostAddress() != null && request.getWardId() != null) {
            Ward ward = wardRepository.findById(request.getWardId())
                    .orElseThrow(() -> new RuntimeException("Ward not found"));
            post.getPostAddress().setWard(ward);
        }
        if (request.getDetailAddress() != null) {
            post.getPostAddress().setDetailAddress(request.getDetailAddress());
        }

        // Update categories
        if (request.getCategoryIds() != null) {
            Set<Category> categories = request.getCategoryIds().stream()
                    .map(id -> categoryRepository.findById(id)
                            .orElseThrow(() -> new RuntimeException("Category not found: " + id)))
                    .collect(Collectors.toSet());
            post.setCategories(categories); // remove cu, theem mới
        }

        // Update images
        if (request.getNewImages() != null || request.getImageUrlsToKeep() != null) {
            List<PostImage> updatedImages = new ArrayList<>();
            List<PostImage> currentImages = post.getImages();

            // Giữ lại các ảnh cũ nếu có trong danh sách
            if (request.getImageUrlsToKeep() != null) {
                updatedImages.addAll(currentImages.stream()
                        .filter(img -> request.getImageUrlsToKeep().contains(img.getImageUrl()))
                        .collect(Collectors.toList()));
            }

            // Thêm ảnh mới nếu có
            if (request.getNewImages() != null) {
                for (MultipartFile file : request.getNewImages()) {
                    String url = s3Service.upload(file);
                    PostImage image = new PostImage();
                    image.setPost(post);
                    image.setImageUrl(url);
                    image.setIsThumbnail(false);
                    updatedImages.add(image);
                }
            }

            // Gắn lại danh sách ảnh
            for (PostImage img : currentImages) {
                if (!updatedImages.contains(img)) {
                    imageRepository.delete(img); // xóa ảnh không còn giữ
                }
            }

            // Cập nhật lại danh sách mới
            post.setImages(updatedImages);

            // Gán ảnh thumbnail mới nếu có
            if (!updatedImages.isEmpty()) {
                updatedImages.get(0).setIsThumbnail(true);
            }
        }

        // Video cập nhật
        if (request.getVideo() != null && !request.getVideo().isEmpty()) {
            String videoUrl = s3Service.upload(request.getVideo());
            post.setDescription(post.getDescription() + "\n[Video Upload] " + videoUrl);
        } else if (request.getVideoLink() != null) {
            post.setDescription(post.getDescription() + "\n[Video Link] " + request.getVideoLink());
        }

        postRepository.save(post);
    }

    @Override
    public void changePostStatus(Long postId, PostStatus status) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        post.setStatus(status);
        post.setUpdatedAt(LocalDateTime.now());
        postRepository.save(post);
    }


    @Override
    public void deletePost(Long id) {
        if (!postRepository.existsById(id)) {
            throw new ResourceNotFoundException("Post not found");
        }
        postRepository.deleteById(id);

    }

    @Override
    public PageResponse getPostsByUserAndStatus(Long userId, PostStatus status, int page, int size, String sortDirection) {
        Sort sort = Sort.by("createdAt");
        sort = "asc".equalsIgnoreCase(sortDirection) ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Post> postPage;

        if (userId != null && status != null) {
            postPage = postRepository.findByUserIdAndStatus(userId, status, pageable);
        } else if (userId != null) {
            postPage = postRepository.findByUserId(userId, pageable);
        } else if (status != null) {
            postPage = postRepository.findByStatus(status, pageable);
        } else {
            postPage = postRepository.findAll(pageable);
        }
        List<PostList> res = postPage.stream().map(post -> {

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
                    .address(post.getAddress())
                    .images(imageUrls)
                    .isVip(post.getIsVip())
                    .username(post.getUser().getName())
                    .phone(post.getUser().getPhone())
                    .createdAt(post.getCreatedAt())
                    .vipExpiryDate(post.getVipExpiryDate())
                    .status(post.getStatus())
                    .build();
        }).toList();

        return PageResponse.builder()
                .page(pageable.getPageNumber())
                .size(pageable.getPageSize())
                .total(postPage.getTotalPages())
                .items(res)
                .build();

    }

    @Override
    public PageResponse<?> advanceSearch(PostFilterRequest filter, Pageable pageable, Integer userId) {
//        System.out.println("Sorting by: " + filter.getSortBy() + " " + filter.getSortDirection());
//        System.out.println("Page: " + pageable.getPageNumber() + ", Size: " +  pageable.getPageSize());

        Set<Long> likedPostIds = (userId != null) ? favoriteService.getFavoritePostIds(Long.valueOf(userId)) : Collections.emptySet();

        Sort sort = Sort.unsorted();
        if (filter.getSortBy() != null && filter.getSortDirection() != null) {
            Sort.Direction direction = filter.getSortDirection().equalsIgnoreCase("desc") ?
                    Sort.Direction.DESC : Sort.Direction.ASC;
            sort = Sort.by(direction, filter.getSortBy());
        }
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);

        PostSpecificationsBuilder builder = new PostSpecificationsBuilder(filter);
        Page<Post> posts = postRepository.findAll(builder.build(), sortedPageable);

//        Set<Long> likedPostIds = (userId != null) ? favoriteService.getFavoritePostIds(userId) : Collections.emptySet();

        List<PostList> res = posts.stream().map(post -> {
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
            boolean isLiked;
            if (userId != null) {
                isLiked = likedPostIds.contains(post.getId());
            } else {
                isLiked = false;
            }


            return PostList.builder()
                    .id(post.getId())
                    .title(post.getTitle())
                    .description(post.getDescription())
                    .price(post.getPrice())
                    .area(post.getArea())
                    .address(shortAddress)
                    .images(imageUrls)
                    .isLike(isLiked)
                    .isVip(post.getIsVip())
                    .username(post.getUser().getName())
                    .phone(post.getUser().getPhone())
                    .createdAt(post.getCreatedAt())
                    .build();
        }).toList();

        return PageResponse.builder()
                .page(pageable.getPageNumber())
                .size(pageable.getPageSize())
                .total(posts.getTotalPages())
                .items(res)
                .build();
    }

    @Override
    public List<PostList> getNearby(Double lat, Double lng, Integer typeId) {
//        List<Post> posts = postRepository.findNearbyPostsByType(lat, lng, Long.valueOf(typeId));
        Set<String> geoHashes = GeoUtil.getGeoHashSearchAreas(lat, lng, 5);
        System.out.println(geoHashes);
//        List<Post> posts = postRepository.findNearbyPostsByType(geoHashes, Long.valueOf(typeId));
        List<Post> posts = customGeoHash.findNearbyPostsByGeoHashPrefixes(geoHashes, Long.valueOf(typeId));

        List<PostList> res = posts.stream().map(post -> {
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
                    .address(post.getAddress())
                    .images(imageUrls)
                    .isVip(post.getIsVip())
                    .username(post.getUser().getName())
                    .phone(post.getUser().getPhone())
                    .longitude(post.getPostAddress().getLongitude())
                    .latitude(post.getPostAddress().getLatitude())
                    .createdAt(post.getCreatedAt())
                    .build();
        }).toList();
        return res;
    }

    @Override
    public List<PostList> getLatest(Integer n) {
        List<Post> posts = postRepository.findLatestPosts(PageRequest.of(0, n));
        List<PostList> res = posts.stream().map(post -> {
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
                    .address(post.getAddress())
                    .images(imageUrls)
                    .isVip(post.getIsVip())
                    .username(post.getUser().getName())
                    .phone(post.getUser().getPhone())
                    .longitude(post.getPostAddress().getLongitude())
                    .latitude(post.getPostAddress().getLatitude())
                    .createdAt(post.getCreatedAt())
                    .build();
        }).toList();
        return res;
    }

    @Override
    public Post getById(Long id) {
        return postRepository.findById(id).orElse(null);
    }

    @Override
    public void save(Post post) {
        postRepository.save(post);
    }


}
