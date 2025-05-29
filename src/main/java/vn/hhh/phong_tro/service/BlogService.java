package vn.hhh.phong_tro.service;

import vn.hhh.phong_tro.model.Blog;

import java.util.List;

public interface BlogService {
    Blog createBlog(Blog blog);
    Blog updateBlog(Long id, Blog updatedBlog);
    void deleteBlog(Long id);
    List<Blog> getAllPublishedBlogs();
    Blog getBlogBySlug(String slug);
}

