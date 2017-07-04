/**
 * 
 */
package com.niit.collaboration.restbackend.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.niit.collaboration.backend.dao.BlogDao;
import com.niit.collaboration.backend.dao.UserDao;
import com.niit.collaboration.backend.model.Blog;
import com.niit.collaboration.backend.model.BlogStatus;
import com.niit.collaboration.backend.model.User;

/**
 * @author Deepika
 *
 */
@RestController
public class BlogRestController {

	@Autowired
	BlogDao blogDao;

	@Autowired
	UserDao userDao;

	/**
	 * 
	 * @return blogs the list of newly created blogs
	 */
	@GetMapping(value = "/blog/new/")
	public ResponseEntity<List<Blog>> getNewBlogs() {
		List<Blog> blogs = blogDao.getNewBlogs();
		if (blogs.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<List<Blog>>(blogs, HttpStatus.OK);
	}

	/**
	 * 
	 * @return blogs the list of ADMIN approved blogs
	 */
	@GetMapping(value = "/blog/approved/")
	public ResponseEntity<List<Blog>> getApprovedBlogs() {
		List<Blog> blogs = blogDao.getApprovedBlogs();
		if (blogs.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<List<Blog>>(blogs, HttpStatus.OK);
	}

	/**
	 * 
	 * @param blogId
	 *            the blog's Id
	 * @return blog the blog's details
	 */
	@GetMapping(value = "/blog/{blogId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Blog> get(@PathVariable("blogId") long blogId) {
		Blog blog = blogDao.get(blogId);
		if (blog == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Blog>(blog, HttpStatus.OK);
	}

	/**
	 * 
	 * @param newBlog
	 *            the newly created blog
	 * @return Void success if created successfully
	 */
	@PostMapping(value = "/blog/")
	public ResponseEntity<Void> create(@RequestBody Blog newBlog, HttpSession session) {
		long loggedInUserId = (Long) session.getAttribute("loggedInUserId");
		User user = userDao.get(loggedInUserId);
		
		newBlog.setDateCreated(new Date());
		newBlog.setLastModified(new Date());
		newBlog.setStatus(BlogStatus.BLOG_NEW);
		newBlog.setUser(user);
		blogDao.create(newBlog);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	/**
	 * 
	 * @param blogId
	 *            the blog's Id to be updated
	 * @param ablog
	 *            the updated blog contents
	 * @return blog the updated blog
	 */
	@PutMapping(value = "/blog/{blogId}")
	public ResponseEntity<Blog> update(@PathVariable("blogId") long blogId, @RequestBody Blog ablog) {
		Blog blog = blogDao.get(blogId);
		if (blog == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		blog.setTitle(ablog.getTitle());
		blog.setContents(ablog.getContents());
		blog.setLastModified(new Date());
		blog.setStatus(ablog.getStatus());
		blogDao.update(blog);
		return new ResponseEntity<Blog>(blog, HttpStatus.OK);
	}

	/**
	 * 
	 * @param blogId
	 *            the blog to be approved
	 * @return blog the approved blog
	 */
	@PutMapping(value = "/blog/approve/{blogId}")
	public ResponseEntity<Blog> approve(@PathVariable("blogId") long blogId) {
		Blog blog = blogDao.get(blogId);
		if (blog == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		blog.setStatus(BlogStatus.BLOG_APPROVED);
		blogDao.update(blog);
		return new ResponseEntity<Blog>(blog, HttpStatus.OK);
	}

	/**
	 * 
	 * @param blogId
	 * @return
	 */
	@PutMapping(value = "/blog/reject/{blogId}")
	public ResponseEntity<Blog> reject(@PathVariable("blogId") long blogId) {
		Blog blog = blogDao.get(blogId);
		if (blog == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		blog.setStatus(BlogStatus.BLOG_REJECTED);
		blogDao.update(blog);
		return new ResponseEntity<Blog>(blog, HttpStatus.OK);
	}

	/**
	 * 
	 * @param blogId
	 *            the blog to be deleted
	 * @return blog the deleted blog
	 */
	@DeleteMapping(value = "/blog/{blogId}")
	public ResponseEntity<Blog> delete(@PathVariable("blogId") long blogId) {
		Blog blog = blogDao.get(blogId);
		if (blog == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		blogDao.remove(blog);
		return new ResponseEntity<Blog>(blog, HttpStatus.OK);
	}
}
