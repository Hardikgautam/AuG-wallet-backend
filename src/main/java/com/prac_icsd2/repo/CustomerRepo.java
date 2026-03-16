package com.prac_icsd2.repo;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.prac_icsd2.dto.response.CustomerFnmLnmGenderDTO;
import com.prac_icsd2.enums.SubscriptionType;
import com.prac_icsd2.model.Customer;

public interface CustomerRepo extends JpaRepository<Customer, Integer> {

    Optional<Customer> findByEmailIdAndPassword(String email, String password);
    Optional<Customer> findByEmailId(String email);

    @Query("SELECT c.emailId, c.contactNo FROM Customer c")
    List<Object[]> findAllUniqueIdentifiers();

    List<Customer> findByFirstNameContaining(String fnm);
    List<Customer> findByfirstNameContains(String fnm);
    List<Customer> findByfirstNameIsContaining(String fnm);
    List<Customer> findByFirstNameLike(String fn);
    List<Customer> findByFirstNameIgnoreCase(String fn);
    List<CustomerFnmLnmGenderDTO> findByLastName(String lnm);
    Boolean existsByEmailIdAndPassword(String email, String password);

    List<Customer> findBySubscription_PlanExpiryDateAndSubscription_SubscriptionTypeNot(LocalDate date, SubscriptionType type);
    List<Customer> findBySubscription_PlanExpiryDateBeforeAndSubscription_SubscriptionTypeNot(LocalDate date, SubscriptionType type);

    
    
    @Query("SELECT DISTINCT c FROM Customer c " +
           "LEFT JOIN FETCH c.accounts " +
           "LEFT JOIN FETCH c.address " +
           "LEFT JOIN FETCH c.subscription " +
           "WHERE c.customerId = :id")
    Optional<Customer> findByIdWithAllDetails(@Param("id") Integer id);

    
    
    
    
    
    
    
    
    @Query("SELECT c FROM Customer c LEFT JOIN c.address a " +
           "WHERE (:search IS NULL OR :search = '' OR " +
           "LOWER(c.firstName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(c.lastName)  LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(c.emailId)   LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(c.contactNo) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Customer> findAllWithSearch(@Param("search") String search, Pageable pageable);
}