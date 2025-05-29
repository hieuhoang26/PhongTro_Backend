package vn.hhh.phong_tro.service;



import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.hhh.phong_tro.model.FavoritePost;
import vn.hhh.phong_tro.model.Post;
import vn.hhh.phong_tro.model.User;
import vn.hhh.phong_tro.model.key.FavoritePostId;
import vn.hhh.phong_tro.repository.FavoritePostRepository;
import vn.hhh.phong_tro.repository.PostRepository;
import vn.hhh.phong_tro.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final FavoritePostRepository favoritePostRepository;

    @Transactional
    public void addFavorite(Long userId, Long postId) {
        boolean exists = favoritePostRepository.existsByUserIdAndPostId(userId, postId);
        if (exists) return;

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        FavoritePost favoritePost = new FavoritePost();
        favoritePost.setId(new FavoritePostId(userId, postId));
        favoritePost.setUser(user);
        favoritePost.setPost(post);
        favoritePost.setCreatedAt(LocalDateTime.now());

        favoritePostRepository.save(favoritePost);
    }

    @Transactional
    public boolean isLikedPost(Long userId, Long postId){
        return favoritePostRepository.existsByUserIdAndPostId(userId, postId);
    }


    @Transactional
    public void removeFavorite(Long userId, Long postId) {
        FavoritePostId id = new FavoritePostId(userId, postId);
        if (favoritePostRepository.existsById(id)) {
            favoritePostRepository.deleteById(id);
        }
    }
    @Transactional(readOnly = true)
    public List<Post> getFavoritePosts(Long userId) {
        return favoritePostRepository.findAllByUserId(userId)
                .stream()
                .map(FavoritePost::getPost)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Set<Long> getFavoritePostIds(Long userId) {
        return favoritePostRepository.findAllByUserId(userId)
                .stream()
                .map(fav -> fav.getPost().getId())
                .collect(Collectors.toSet());
    }
}
