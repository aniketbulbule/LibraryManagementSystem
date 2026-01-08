package com.main.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.main.entity.Reservation; 

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
	List<Reservation> findByBookIdAndFulfilledFalse(Long bookId);

	List<Reservation> findByUserIdAndFulfilledFalse(Long userId);
} 