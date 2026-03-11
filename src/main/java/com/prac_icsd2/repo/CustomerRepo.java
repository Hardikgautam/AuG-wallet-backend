package com.prac_icsd2.repo;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.prac_icsd2.dto.response.CustomerFnmLnmGenderDTO;
import com.prac_icsd2.enums.SubscriptionType;
import com.prac_icsd2.model.Customer;

public interface CustomerRepo extends JpaRepository<Customer,Integer> {
	public Optional<Customer> findByEmailIdAndPassword(String email, String password);
	public Optional<Customer> findByEmailId(String email);
	
	@Query("SELECT c.emailId, c.contactNo FROM Customer c")
	List<Object[]> findAllUniqueIdentifiers();
	
//	SELECT * FROM movie WHERE title LIKE '%in%';
	List<Customer> findByFirstNameContaining(String fnm);
	List<Customer> findByfirstNameContains(String fnm);
	List<Customer> findByfirstNameIsContaining(String fnm);
	//We can expect each of the three methods to return the same results.
	List<Customer> findByFirstNameLike(String fn);
	// where UPPER(x.firstname) = UPPER(?1)
	List<Customer> findByFirstNameIgnoreCase(String fn);
	
	List<CustomerFnmLnmGenderDTO> findByLastName(String lnm);
	Boolean existsByEmailIdAndPassword(String email, String password);
	

	

	List<Customer> findBySubscription_PlanExpiryDateAndSubscription_SubscriptionTypeNot(LocalDate date, SubscriptionType type);

	List<Customer>findBySubscription_PlanExpiryDateBeforeAndSubscription_SubscriptionTypeNot(LocalDate date, SubscriptionType type);
}
