package com.prac_icsd2.dto;

import java.time.LocalDate;

import com.prac_icsd2.enums.AccountType;
import com.prac_icsd2.model.Customer;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountRequestDTO {
	
	@NotNull(message="Customer cannot be null")
	public Customer customer;
	@NotNull(message="AccountType is required")
	public AccountType accountType;
	
	@NotNull(message="openingbalance required")
	@Min(value = 1 ,message = "opening balance should be bw 1-1000000")
	@Max(value=1000000 ,message = "opening balance should be between 1-1000000")
	public Double openingBalance;
	public LocalDate openingDate;

	@NotBlank(message="description required")
	public String description;
	@Override
	public String toString() {
		return "AccountRequestDTO [customer=" + customer + ", typeofaccount=" + accountType + ", openingbalance="
				+ openingBalance + ", openingdate=" + openingDate + ", description=" + description + "]";
	}
	
	
}