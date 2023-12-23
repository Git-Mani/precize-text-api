package com.precize.record.service;

import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.precize.record.entity.User;
import com.precize.record.model.CandidateStatus;
import com.precize.record.repo.UserRepo;

@Service
public class RecordService {

	@Autowired
	private UserRepo userRepo;

	public String saveUser(User user) {

		if (userRepo.existsByNameIgnoreCase(user.getName())) {
			throw new DataIntegrityViolationException("User with name '" + user.getName() + "' already exists.");
		}
		// set status
		user.setStatus(user.getSatScore() > 30 ? CandidateStatus.PASSED : CandidateStatus.FAILED);
		userRepo.save(user);
		return "Record inserted";
	}

	public List<User> getData() {
		return userRepo.findAll();
	}

	public Integer getRankByName(String name) {
		Optional<User> userOptional = userRepo.findByName(name);
		if (userOptional.isPresent()) {
			User user = userOptional.get();
			List<User> users = userRepo.findAll();
			int count = 0;
			// count for duplicate score
			int duplicateCount = 0;
			// create list for proceed score numbers
			Set<Double> score = new HashSet<>();
			for (User userEntity : users) {
				if (user.getId() != userEntity.getId() && user.getSatScore() > userEntity.getSatScore()) {
					count++;
				}
				if (score.contains(userEntity.getSatScore())) {
					duplicateCount++;
				} else {
					score.add(userEntity.getSatScore());
				}

			}
			int rank = users.size() - count - duplicateCount;
			// handle boundary case
			return rank == 0 ? 1 : rank;
		} else {
			throw new NoSuchElementException();
		}
	}

	public String updateSatScoreByName(String name, Double newSatScore) {
		Optional<User> userOptional = userRepo.findByName(name);
		if (userOptional.isPresent()) {
			User user = userOptional.get();
			user.setSatScore(newSatScore);
			userRepo.save(user);
			return "Sat Score updated";
		} else {
			System.out.println("inside");
			throw new DataIntegrityViolationException("User not found with name: " + name);
		}
	}

	public String deleteUserByName(String name) {
		User user = userRepo.findByName(name).orElseThrow(() -> new NoSuchElementException("User not found"));
		userRepo.delete(user);
		return "Record Deleted successfully";
	}

}
