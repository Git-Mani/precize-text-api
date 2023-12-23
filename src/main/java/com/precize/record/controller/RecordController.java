package com.precize.record.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.precize.record.entity.User;
import com.precize.record.model.CandidateStatus;
import com.precize.record.model.UserData;
import com.precize.record.service.RecordService;

@RestController
@RequestMapping("/api/users")
public class RecordController {
	@Autowired
	private RecordService recordService;

	@GetMapping("/list")
	public ResponseEntity<List<User>> getData() {
		try {
			return ResponseEntity.status(HttpStatus.OK).body(recordService.getData());
		} catch (DataIntegrityViolationException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}

	}

	@PostMapping("/add")
	public ResponseEntity<String> addUser(@RequestBody User user) {
		try {
			
			return ResponseEntity.status(HttpStatus.OK).body(recordService.saveUser(user));
		} catch (DataIntegrityViolationException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while processing the request.");
		}

	}

	@GetMapping("/getRank")
	public ResponseEntity<String> getRank(@RequestParam String name) {
		try {
			return ResponseEntity.status(HttpStatus.OK).body("" + recordService.getRankByName(name));
		} catch (NoSuchElementException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Not Found");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while processing the request.");
		}
	}

	@PutMapping("/update/{name}")
	public ResponseEntity<String> updateSatScore(@PathVariable String name, @RequestBody UserData newData) {
		try {
			return ResponseEntity.status(HttpStatus.OK)
					.body(recordService.updateSatScoreByName(name, newData.getSatScore()));
		} catch (DataIntegrityViolationException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
		}
	}

	@DeleteMapping("/delete/{name}")
	public ResponseEntity<String> deleteUser(@PathVariable String name) {
		try {

			return ResponseEntity.status(HttpStatus.OK).body(recordService.deleteUserByName(name));
		} catch (NoSuchElementException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
		}
	}
}
