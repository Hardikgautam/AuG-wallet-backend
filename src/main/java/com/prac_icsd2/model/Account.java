package com.prac_icsd2.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.prac_icsd2.enums.AccountType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="ACCOUNT")


public class Account {

	 @Id
	 @GeneratedValue(strategy=GenerationType.SEQUENCE,generator="agen")
	 @SequenceGenerator(name = "agen", sequenceName = "ASEQ", allocationSize = 1)	 //@Column(name = "ACCOUNTNUMBER")
	 private int accountnumber;
	
	 @ManyToOne
	 @JoinColumn(name="customerFk")
	 @JsonIgnore
	 private Customer customer;
	 @Column
	 @Enumerated(EnumType.STRING) 
	 private AccountType accountType;

	 @Column
	 private Double openingBalance;
	 private LocalDate openingDate;
	 private String description;
	
}
