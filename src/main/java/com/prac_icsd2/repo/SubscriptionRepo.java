package com.prac_icsd2.repo;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.prac_icsd2.enums.SubscriptionType;
import com.prac_icsd2.model.Subscription;

import jakarta.transaction.Transactional;

public interface SubscriptionRepo extends JpaRepository<Subscription,Integer>{
		
//	@Modifying
//	@Transactional
//	@Query("UPDATE Subscription s SET s.subscriptionType = :status "+
//			"WHERE s.planExpiryDate < :today AND s.subscriptionType != :status"
//			)
//	int bulkMarkExpired(@Param("today") LocalDate today,@Param("status")SubscriptionType status);
//	
	@Modifying
	@Transactional
	@Query("UPDATE Subscription s SET s.subscriptionType = :status " +
	       "WHERE s.planExpiryDate < :today AND s.subscriptionType != :status")
	int bulkMarkExpired(@Param("today") LocalDate today, @Param("status") SubscriptionType status);

}
