/**
 * 
 */
package com.niit.collaboration.restbackend.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.niit.collaboration.backend.model.PrivateText;

/**
 * @author Deepika
 *
 */
@Controller
public class PrivateChatController {

	@Autowired
	private SimpMessagingTemplate template;
	
	/**
	 * 
	 * @param message
	 */
	@MessageMapping("/privateChat")
	public void sendMessage(PrivateText message) {
		message.setTime(new Date());
		template.convertAndSend("/queue/message/" + message.getUsername(), message);
		template.convertAndSend("/queue/message/" + message.getFriendName(), message);
	}
}
