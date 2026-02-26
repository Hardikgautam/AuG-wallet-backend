
  package com.prac_icsd2.repo;
  
  import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.prac_icsd2.model.TransactionModel;
  

  public interface TransactionRepo extends JpaRepository<TransactionModel,Integer> {
	  @Query("select t from TransactionModel t where t.fromAccount  = ?1 or t.toAccount = ?1")
	List<TransactionModel> findByAccountNumber(int accountNumber);
  
  }
 