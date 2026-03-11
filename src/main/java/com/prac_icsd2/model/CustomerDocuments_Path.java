package com.prac_icsd2.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.prac_icsd2.enums.DocType;

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
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="DOCUMENTS_LOCALFILE_D")
public class CustomerDocuments_Path {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="docGen_local123")
	@SequenceGenerator(name="docGen_local123",sequenceName="DOCSEQ_local123",allocationSize=1)
	private Integer documentid;
	
	private String filetype;
	
	@Enumerated(EnumType.STRING)
	private DocType docname;
	
	@Column(name="file_path")
	private String filePath;
	private String fileName;
	
	private LocalDate uploadDate;
	
	@Builder.Default
	private Boolean status=true;
	
	@ManyToOne
	@JoinColumn(name="customerfk")
	@JsonIgnore
	private Customer customer;
	
	
}
