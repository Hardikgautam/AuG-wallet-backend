package com.prac_icsd2.dto.common;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class ApiResponse {

	

	private Integer code;
	
	
	private String message;
	
	private Object data;
	
    public ApiResponse(int code, String message) {
        this.code = code;
        this.message = message;
        this.data = null;
    } 
	
	
}
