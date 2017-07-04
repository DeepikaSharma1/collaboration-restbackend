/**
 * 
 */
package com.niit.collaboration.restbackend.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.niit.collaboration.backend.dao.JobDao;
import com.niit.collaboration.backend.dao.UserDao;
import com.niit.collaboration.backend.model.Job;
import com.niit.collaboration.backend.model.JobStatus;
import com.niit.collaboration.backend.model.JobsApplied;
import com.niit.collaboration.backend.model.User;

/**
 * @author Deepika
 *
 */
@RestController
public class JobRestController {

	@Autowired
	JobDao jobDao;
	
	@Autowired
	UserDao userDao;
	
	/**
	 * 
	 * @return
	 */
	@GetMapping(value = "/job/")
	public ResponseEntity<List<Job>> getAll() {
		List<Job> jobs = jobDao.getAll();
		if (jobs.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<List<Job>>(jobs, HttpStatus.OK);
	}
	
	/**
	 * 
	 * @param jobId
	 * @return
	 */
	@GetMapping(value = "/job/{jobId}")
	public ResponseEntity<Job> get(@PathVariable("jobId") long jobId) {
		Job job = jobDao.get(jobId);
		if (job == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Job>(job, HttpStatus.OK);
	}
	
	/**
	 * 
	 * @param session
	 * @return
	 */
	@GetMapping(value = "/job/applied/")
	public ResponseEntity<List<JobsApplied>> getJobsApplied(HttpSession session) {
		long loggedInUserId = (Long) session.getAttribute("loggedInUserId");
		List<JobsApplied> jobsApplied = jobDao.getAll(loggedInUserId);
		if (jobsApplied.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<List<JobsApplied>>(jobsApplied, HttpStatus.OK);
	}
	
	/**
	 * 
	 * @param newJob
	 * @return
	 */
	@PostMapping(value = "/job/")
	public ResponseEntity<Job> create(@RequestBody Job newJob) {
		newJob.setPostDate(new Date());
		newJob.setStatus(JobStatus.JOB_NEW);
		jobDao.create(newJob);
		return new ResponseEntity<Job>(newJob, HttpStatus.CREATED);
	}
	
	/**
	 * 
	 * @param jobId
	 * @param session
	 * @return
	 */
	@PostMapping(value = "/job/apply/{jobId}")
	public ResponseEntity<Job> apply(@PathVariable("jobId") long jobId, HttpSession session) {
		long loggedInUserId = (Long) session.getAttribute("loggedInUserId");
		Job job = jobDao.get(jobId);
		User user = userDao.get(loggedInUserId);
		
		JobsApplied apply = new JobsApplied();
		apply.setJob(job);
		apply.setUser(user);
		apply.setDateApplied(new Date());
		apply.setStatus(JobStatus.JOB_APPLIED);
		jobDao.saveJobsApplied(apply);
		return new ResponseEntity<Job>(job, HttpStatus.CREATED);
	}
	
	/**
	 * 
	 * @param jobId
	 * @param ajob
	 * @return
	 */
	@PutMapping(value = "/job/{jobId}")
	public ResponseEntity<Job> update(@PathVariable("jobId") long jobId, @RequestBody Job ajob) {
		Job job = jobDao.get(jobId);
		if (job == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		job.setTitle(ajob.getTitle());
		job.setDescription(ajob.getDescription());
		job.setPostDate(ajob.getPostDate());
		job.setStatus(ajob.getStatus());
		jobDao.update(job);
		return new ResponseEntity<Job>(job, HttpStatus.OK);
	}
}
