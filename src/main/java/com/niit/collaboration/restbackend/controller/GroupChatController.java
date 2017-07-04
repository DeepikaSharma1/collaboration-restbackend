/**
 * 
 */
package com.niit.collaboration.restbackend.controller;

import java.util.Date;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.niit.collaboration.backend.model.GroupText;
import com.niit.collaboration.backend.model.Message;

/**
 * 
 * @author Deepika
 *
 */
@Controller
public class GroupChatController {

	/**
	 * 
	 * @param message
	 * @return
	 */
	@MessageMapping("/groupChat")
	@SendTo("/topic/message")
	public GroupText sendMessage(Message message) {
		return new GroupText(message, new Date());
	}
}
