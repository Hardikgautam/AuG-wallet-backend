package com.prac_icsd2.dto;

import java.time.LocalDate;

import com.prac_icsd2.enums.TransactionType;
import com.prac_icsd2.model.Account;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDepositRequestDTO {
	@Min(value=1,message="customer id should not be 0 ")
	private Integer  customerid;
	@Min(value=1,message="Account number should not be 0 ")
	private Integer  accountnumber;
	@Min(value = 1 ,message = "amount should be bw 1-1000000")
	@Max(value=10000 ,message = "amount should be between 1-10000")
	private double amount;
	@Enumerated(EnumType.STRING)
	private TransactionType transactionType;
	private LocalDate transactiondate;
	@NotNull(message="fromAccount details are required")
	private Account fromAccount;	
	@NotNull(message="toAccount details are required")
	private Account toAccount;
}


	
	

