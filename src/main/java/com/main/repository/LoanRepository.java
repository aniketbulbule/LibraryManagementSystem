package com.main.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.main.entity.Loan; 

public interface LoanRepository extends JpaRepository<Loan, Long> {
	List<Loan> findByUserIdAndStatus(Long userId, Loan.Status status);
 
	List<Loan> findByStatus(Loan.Status status);
}