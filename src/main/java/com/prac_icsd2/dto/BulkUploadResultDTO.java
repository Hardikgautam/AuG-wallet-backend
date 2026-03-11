package com.prac_icsd2.dto;


import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BulkUploadResultDTO {
	private int totalRows;
	private int successCount;
	private int failureCount;
	private List<RowErrorDTO> errors;
	
	@Data
	@Builder
	public static class RowErrorDTO{
		private int rowNumber;
		private String emailid;
		private List<String> errors;
	}
}