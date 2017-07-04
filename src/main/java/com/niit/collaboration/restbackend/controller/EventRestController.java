/**
 * 
 */
package com.niit.collaboration.restbackend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.niit.collaboration.backend.dao.EventDao;
import com.niit.collaboration.backend.model.Event;
import com.niit.collaboration.backend.model.EventStatus;

/**
 * @author Deepika
 *
 */
@RestController
public class EventRestController {

	@Autowired
	EventDao eventDao;
	
	/**
	 * 
	 * @return
	 */
	@GetMapping(value = "/event/")
	public ResponseEntity<List<Event>> getAll() {
		List<Event> events = eventDao.getAll();
		if (events.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<List<Event>>(events, HttpStatus.OK);
	}
	
	/**
	 * 
	 * @param eventId
	 * @return
	 */
	@GetMapping(value = "/event/{eventId}")
	public ResponseEntity<Event> get(@PathVariable("eventId") long eventId) {
		Event event = eventDao.get(eventId);
		if (event == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Event>(event, HttpStatus.OK);
	}
	
	/**
	 * 
	 * @param newEvent
	 * @return
	 */
	@PostMapping(value = "/event/")
	public ResponseEntity<Event> create(@RequestBody Event newEvent) {
		newEvent.setStatus(EventStatus.EVENT_NEW);
		eventDao.create(newEvent);
		return new ResponseEntity<Event>(newEvent, HttpStatus.CREATED);
	}
	
	/**
	 * 
	 * @param eventId
	 * @param evt
	 * @return
	 */
	@PutMapping(value = "/event/{eventId}")
	public ResponseEntity<Event> update(@PathVariable("eventId") long eventId, @RequestBody Event evt) {
		Event event = eventDao.get(eventId);
		if (event == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		event.setTitle(evt.getTitle());
		event.setDescription(evt.getDescription());
		event.setEventDate(evt.getEventDate());
		event.setStatus(evt.getStatus());
		eventDao.update(event);
		return new ResponseEntity<Event>(event, HttpStatus.OK);
	}
	
	/**
	 * 
	 * @param eventId
	 * @return
	 */
	@DeleteMapping(value = "/event/{eventId}")
	public ResponseEntity<Event> remove(@PathVariable("eventId") long eventId) {
		Event event = eventDao.get(eventId);
		if (event == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		event.setStatus(EventStatus.EVENT_DONE);
		eventDao.update(event);
		return new ResponseEntity<Event>(event, HttpStatus.OK);
	}
}
