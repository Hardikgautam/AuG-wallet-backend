package com.prac_icsd2.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.prac_icsd2.enums.Gender;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
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
public class Address {
	 @Id
	 @GeneratedValue(strategy=GenerationType.SEQUENCE,generator="adgen")
	 @SequenceGenerator(name = "adgen", sequenceName = "ADSEQ", allocationSize = 1)
	 private int addressid;
	 private String addressline1;
	 private String addressline2;
	 private String city;
	 private String state;
	 private String pincode;
}