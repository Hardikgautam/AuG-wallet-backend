package com.prac_icsd2.model;

import java.time.LocalDate;

import com.prac_icsd2.enums.SubscriptionType;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Subscription {

	@Id
	@SequenceGenerator(name = "subgenerator", sequenceName = "SUBTID_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "subgenerator")	
	private Integer subscriptionId;
	
	@Enumerated(EnumType.STRING)
	private SubscriptionType subscriptionType;
		
	private LocalDate planStartDate;
	private LocalDate planExpiryDate;
	
	
	
}
