package com.precize.record.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.precize.record.entity.User;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {

	Optional<User> findByName(String name);

	boolean existsByNameIgnoreCase(String name);

	Integer countBySatScoreGreaterThanEqual(Double satScore);

	@Query("SELECT COUNT(u) + 1 FROM User u WHERE u.satScore > :satScore "
			+ "OR (u.satScore = :satScore AND u.name != :name)")
	int calculateRankBySatScoreAndName(Double satScore, String name);
}
