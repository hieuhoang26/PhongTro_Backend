package vn.hhh.phong_tro.service.imp;

import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import vn.hhh.phong_tro.exception.ResourceNotFoundException;
import vn.hhh.phong_tro.model.Blog;
import vn.hhh.phong_tro.repository.BlogRepository;
import vn.hhh.phong_tro.service.BlogService;
import vn.hhh.phong_tro.util.BlogStatus;
import vn.hhh.phong_tro.util.SlugUtil;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BlogServiceImpl implements BlogService {

    private final BlogRepository blogRepository;

    @Override
    public Blog createBlog(Blog blog) {
        // Tự tạo slug nếu chưa có
        if (blog.getSlug() == null || blog.getSlug().isEmpty()) {
            blog.setSlug(SlugUtil.toSlug(blog.getTitle()));
        }
        return blogRepository.save(blog);
    }

    @Override
    public Blog updateBlog(Long id, Blog updatedBlog) {
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Blog not found"));
        blog.setTitle(updatedBlog.getTitle());
        blog.setSlug(updatedBlog.getSlug());
        blog.setDescription(updatedBlog.getDescription());
        blog.setContent(updatedBlog.getContent());
        blog.setStatus(updatedBlog.getStatus());
        blog.setThumbnail(updatedBlog.getThumbnail());
        return blogRepository.save(blog);
    }

    @Override
    public void deleteBlog(Long id) {
        blogRepository.deleteById(id);
    }

    @Override
    public List<Blog> getAllPublishedBlogs() {
        return blogRepository.findAllByStatusOrderByCreatedAtDesc(BlogStatus.PUBLISHED);
    }

    @Override
    public Blog getBlogBySlug(String slug) {
        return blogRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Blog not found"));
    }
}

