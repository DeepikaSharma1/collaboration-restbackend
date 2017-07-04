/**
 * 
 */
package com.niit.collaboration.restbackend.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.niit.collaboration.backend.dao.FriendDao;
import com.niit.collaboration.backend.dao.UserDao;
import com.niit.collaboration.backend.model.Friend;
import com.niit.collaboration.backend.model.RequestStatus;

/**
 * @author Deepika
 *
 */
@RestController
public class FriendRestController {

	@Autowired
	FriendDao friendDao;
	
	@Autowired
	UserDao userDao;
	
	/**
	 * 
	 * @param toUserId
	 * @param session
	 * @return
	 */
	@GetMapping(value = "/friend/send/{toUserId}")
	public ResponseEntity<Friend> sendRequest(@PathVariable("toUserId") long toUserId, HttpSession session) {
		long loggedInUserId = (Long) session.getAttribute("loggedInUserId");
		Friend frnd = new Friend();
		
		frnd.setUser(userDao.get(loggedInUserId));
		frnd.setFriend(userDao.get(toUserId));
		frnd.setStatus(RequestStatus.REQ_NEW);
		frnd.setOnline(false);
		
		friendDao.create(frnd);
		
		return new ResponseEntity<>(frnd, HttpStatus.OK);
	}
	
	/**
	 * 
	 * @param session
	 * @return
	 */
	@GetMapping(value = "/friend/friends/")
	public ResponseEntity<List<Friend>> getFriends(HttpSession session) {
		long loggedInUserId = (Long) session.getAttribute("loggedInUserId");
		List<Friend> friends = friendDao.getFriends(loggedInUserId);
		return new ResponseEntity<>(friends, HttpStatus.OK);
	}
	
	/**
	 * 
	 * @param session
	 * @return
	 */
	@GetMapping(value = "/friend/requests/")
	public ResponseEntity<List<Friend>> getRequests(HttpSession session) {
		long loggedInUserId = (Long) session.getAttribute("loggedInUserId");
		List<Friend> requests = friendDao.getRequests(loggedInUserId);
		return new ResponseEntity<>(requests, HttpStatus.OK);
	}
	
	/**
	 * 
	 * @param userId
	 * @param session
	 * @return
	 */
	@GetMapping(value = "/friend/accept/{userId}")
	public ResponseEntity<Friend> accept(@PathVariable("userId") long userId, HttpSession session) {
		long loggedInUserId = (Long) session.getAttribute("loggedInUserId");
		Friend frnd1 = friendDao.get(loggedInUserId, userId);
		Friend frnd2 = friendDao.get(userId, loggedInUserId);
		
		frnd1.setStatus(RequestStatus.REQ_ACCEPTED);
		frnd2.setStatus(RequestStatus.REQ_ACCEPTED);
		
		friendDao.update(frnd1);
		friendDao.update(frnd2);
		return new ResponseEntity<>(frnd1, HttpStatus.OK);
	}
	
	/**
	 * 
	 * @param userId
	 * @param session
	 * @return
	 */
	@GetMapping(value = "/friend/reject/{userId}")
	public ResponseEntity<Friend> reject(@PathVariable("userId") long userId, HttpSession session) {
		long loggedInUserId = (Long) session.getAttribute("loggedInUserId");
		Friend frnd1 = friendDao.get(loggedInUserId, userId);
		Friend frnd2 = friendDao.get(userId, loggedInUserId);
		
		frnd1.setStatus(RequestStatus.REQ_REJECTED);
		frnd2.setStatus(RequestStatus.REQ_REJECTED);
		
		friendDao.update(frnd1);
		friendDao.update(frnd2);
		return new ResponseEntity<>(frnd1, HttpStatus.OK);
	}
}
