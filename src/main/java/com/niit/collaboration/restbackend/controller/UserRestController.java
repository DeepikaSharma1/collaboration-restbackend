/**
 * 
 */
package com.niit.collaboration.restbackend.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.niit.collaboration.backend.dao.FriendDao;
import com.niit.collaboration.backend.dao.UserDao;
import com.niit.collaboration.backend.model.Role;
import com.niit.collaboration.backend.model.User;

/**
 * @author Deepika
 *
 */
@RestController
public class UserRestController {

	@Autowired
	UserDao userDao;
	
	@Autowired
	FriendDao friendDao;
	
	/**
	 * 
	 * @return
	 */
	@GetMapping(value = "/user/all/")
	public ResponseEntity<List<User>> getAll() {
		System.out.println("Inside UserRestController::getAll()");
		List<User> users = userDao.getAll();
		if (users.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(users, HttpStatus.OK);
	}
	
	/**
	 * 
	 * @param session
	 * @return
	 */
	@GetMapping(value = "/user/others/")
	public ResponseEntity<List<User>> getAllExcept(HttpSession session) {
		long loggedInUserId = (Long) session.getAttribute("loggedInUserId");
		List<User> users = userDao.getAllExcept(loggedInUserId);
		if (users.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(users, HttpStatus.OK);
	}
	
	/**
	 * 
	 * @param userId
	 * @return
	 */
	@GetMapping(value = "/user/userId/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<User> get(@PathVariable("userId") long userId) {
		User user = userDao.get(userId);
		if (user == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(user, HttpStatus.OK);
	}
	
	/**
	 * 
	 * @param username
	 * @return
	 */
	@GetMapping(value = "/user/username/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<User> get(@PathVariable("username") String username) {
		User user = userDao.get(username);
		if (user == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(user, HttpStatus.OK);
	}
	
	/**
	 * 
	 * @param newUser
	 * @return
	 */
	@PostMapping(value = "/user/")
	public ResponseEntity<Void> create(@RequestBody User newUser) {
		boolean exists = userDao.exists(newUser);
		if (exists) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}
		
		newUser.setRole(Role.USER);
		newUser.setEnabled(false);
		userDao.create(newUser);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	/**
	 * 
	 * @param user
	 * @param session
	 * @return
	 */
	@PostMapping(value = "/user/login/")
	public ResponseEntity<User> login(@RequestBody User user, HttpSession session) {
		boolean result = userDao.authenticate(user.getUsername(), user.getPassword());
		if (result) {
			User loggedInUser = userDao.get(user.getUsername());
			long loggedInUserId = loggedInUser.getUserId();
			userDao.setOnline(loggedInUserId);
			friendDao.setOnline(loggedInUserId);
			session.setAttribute("loggedInUserId", loggedInUserId);
			session.setAttribute("loggedInUser", loggedInUser);
			return new ResponseEntity<>(loggedInUser, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
	/**
	 * 
	 * @param session
	 * @return
	 */
	@PutMapping(value = "/user/logout/")
	public ResponseEntity<User> logout(HttpSession session) {
		long loggedInUserId = (Long) session.getAttribute("loggedInUserId");
		userDao.setOffline(loggedInUserId);
		friendDao.setOffline(loggedInUserId);
		session.removeAttribute("loggedInUserId");
		session.removeAttribute("loggedInUser");
		session.invalidate();
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	/**
	 * 
	 * @param userId
	 * @param auser
	 * @return
	 */
	@PutMapping(value = "/user/{userId}")
	public ResponseEntity<User> update(@PathVariable("userId") long userId, @RequestBody User auser) {
		User user = userDao.get(userId);
		if (user == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		user.setUsername(auser.getUsername());
		user.setPassword(auser.getPassword());
		user.setEmail(auser.getEmail());
		user.setEnabled(auser.isEnabled());
		user.setOnline(auser.isOnline());
		user.setRole(auser.getRole());
		userDao.update(user);
		return new ResponseEntity<>(user, HttpStatus.OK);
	}
	
	/**
	 * 
	 * @param userId
	 * @return
	 */
	@PutMapping(value = "/user/enable/{userId}")
	public ResponseEntity<User> enable(@PathVariable("userId") long userId) {
		User user = userDao.get(userId);
		if (user == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		user.setEnabled(true);
		userDao.update(user);
		return new ResponseEntity<>(user, HttpStatus.OK);
	}
	
	/**
	 * 
	 * @param userId
	 * @return
	 */
	@PutMapping(value = "/user/disable/{userId}")
	public ResponseEntity<User> disable(@PathVariable("userId") long userId) {
		User user = userDao.get(userId);
		if (user == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		user.setEnabled(false);
		userDao.update(user);
		return new ResponseEntity<>(user, HttpStatus.OK);
	}
	
	/**
	 * 
	 * @param userId
	 * @return
	 */
	@PutMapping(value = "/user/makeAdmin/{userId}")
	public ResponseEntity<User> makeAdmin(@PathVariable("userId") long userId) {
		User user = userDao.get(userId);
		if (user == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		user.setRole(Role.ADMIN);;
		userDao.update(user);
		return new ResponseEntity<>(user, HttpStatus.OK);
	}
}
