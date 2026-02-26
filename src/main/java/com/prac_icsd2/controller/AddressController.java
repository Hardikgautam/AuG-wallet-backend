package com.prac_icsd2.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prac_icsd2.dto.common.ApiResponse;
import com.prac_icsd2.serviceI.AddressService;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@Data
@RequestMapping("/address")
@CrossOrigin(value = "http://localhost:3000")
public class AddressController {


	private final AddressService addressService;
	@GetMapping(value="/findByAddressLine2IsNotNull")
	public ResponseEntity<ApiResponse> findByAddressline2IsNotNull()
	{
		ApiResponse apiresponse=new ApiResponse(HttpStatus.OK.value(), "addwith not nullvalue", addressService.findByAddressline2IsNotNull());
		return new ResponseEntity<ApiResponse>(apiresponse,HttpStatus.OK);
	}
	@GetMapping(value="/findByAddressLine2IsNull")
	public ResponseEntity<ApiResponse> findByAddressline2IsNull()
	{
		ApiResponse apiresponse=new ApiResponse(HttpStatus.OK.value(), "addwith nullvalue", addressService.findByAddressline2IsNull());
		return new ResponseEntity<ApiResponse>(apiresponse,HttpStatus.OK);
	}

}
