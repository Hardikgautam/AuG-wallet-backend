package com.prac_icsd2.dto;

import com.prac_icsd2.enums.DocType;
import com.prac_icsd2.model.Customer;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotNull;

public class CustDocDTO {

	@NotNull(message="Customer can not be null DTO")
	public Customer customer;
	
	@NotNull(message="Docname can not be null DTO")
	private DocType docname;
	
	@NotNull(message="FileType can not be null DTO")
	private String filetype;
	
	@Lob
	private byte[] filedata;
	
	
	
}
