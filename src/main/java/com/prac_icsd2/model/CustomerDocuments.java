package com.prac_icsd2.model;

import java.time.LocalDate;

import com.prac_icsd2.enums.DocType;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="DOCUMENTS")
public class CustomerDocuments{
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="docGen")
	@SequenceGenerator(name="docGen",sequenceName="DOCSEQ",allocationSize=1)
	private int documentId;
		
	private DocType documentName;
	
	private String filetype;
	
	private LocalDate uploadDate;
	
	
	@OneToOne
	@JoinColumn(name="customeridfk")
	private Customer customer;
	
	@Lob
	private byte[] filedata;
	
}