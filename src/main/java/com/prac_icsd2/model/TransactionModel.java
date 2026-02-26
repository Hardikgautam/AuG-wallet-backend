package com.prac_icsd2.model;

import java.time.LocalDate;

import com.prac_icsd2.enums.TransactionType;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionModel {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="tragen")
	@SequenceGenerator(
	        name = "tragen", 
	        sequenceName = "traseq", // Matches your SQL: CREATE SEQUENCE traseq
	        allocationSize = 1       // Matches your SQL: INCREMENT BY 1
	    )	private int transactionid;
	@Enumerated(EnumType.STRING)
	private TransactionType transactionType;
	
	private LocalDate transactiondate;
	private double amount;
	private String description;
	@ManyToOne
	@JoinColumn(name="fromacc")
	private Account fromAccount;
	@ManyToOne
	@JoinColumn(name="toacc")
	private Account toAccount;
}