package com.task.serviceA.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import com.task.serviceA.entity.User;

@Repository
@EnableJpaRepositories
public interface UserRepository extends JpaRepository<User, Long> {
	
    @Query("SELECT u FROM User u WHERE u.notified = false")
    List<User> findByNotifiedFalse();

	User findByemail(String email);

}