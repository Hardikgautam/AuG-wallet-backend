package com.prac_icsd2.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prac_icsd2.dto.CustomerLoginDTO;
import com.prac_icsd2.dto.CustomerRequestDto;
import com.prac_icsd2.dto.common.ApiResponse;
import com.prac_icsd2.dto.response.CustomerFnmLnmGenderDTO;
import com.prac_icsd2.enums.Gender;
import com.prac_icsd2.model.Address;
import com.prac_icsd2.model.Customer;
import com.prac_icsd2.serviceI.CustomerService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@CrossOrigin(value = "http://localhost:3000")
@RequiredArgsConstructor
@Slf4j
@RequestMapping(value = "/customer")
public class CustController {
	@Autowired
	CustomerService customerService;
	
	  
	  @GetMapping(value="/create") public Customer createcustomer() { Address addr = Address.builder() 
			  .addressid(3) 
			  .addressline1("Ram Nagar Gali NO.3")
			  .addressline2("Jatal Road")
			  .city("Panipat")
			  .state("Haryana")
			  .pincode("132105")
			  .build();
	 
	  
	  		Customer c1 = Customer.builder() 
	  				.firstName("Kartik")
	  				.lastName("Sharma")
	  				.emailId("sharmakartik090@gmail.com") 
	  				.contactNo("7988209039") 
	  				.address(addr)
	  				.gender(Gender.MALE) 
	  				.password("hardik") 
	  				.registerationDate(LocalDate.now())
	  				.build();
	  
	  customerService.saveCustomer(c1); return c1; }
	 

	@PostMapping(value = "/create") 
	public ResponseEntity<ApiResponse> createCustomer(@RequestBody @Valid CustomerRequestDto customerRequest) {
		log.info("inside create method of customer controller");
		System.out.println("Create Customer API called at " + LocalDate.now());
		System.out.println(customerRequest);
		Integer generatedCustomerId = customerService.createCustomer(customerRequest);
		ApiResponse apiResponse = new ApiResponse(HttpStatus.OK.value(), "Customer Created Successfully",
				generatedCustomerId);
		return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.OK);
	}

	@PostMapping(value="/isValidUser")
	public ResponseEntity<ApiResponse> isValidUser(@RequestBody  @Valid CustomerLoginDTO customerLogin)
	{
		log.info("authenticating user - valid or not ");
		//customerLogin.
		System.out.println(customerLogin);
		boolean res=customerService.isValidCustByEmailidAndPwd(customerLogin);
		ApiResponse apiresponse=new ApiResponse(HttpStatus.OK.value(), "user is validated", res);
		
		return new ResponseEntity<ApiResponse>	(apiresponse,HttpStatus.OK);
	}
	
	
	
	//********************************************************
	@GetMapping(value="/get/findByLastName/{lnm}")
	public  ResponseEntity<ApiResponse> findByLastNameDTOResponse(@PathVariable String lnm)
	{
		log.info("inside controller findByLastName lnm="+ lnm);
		List<CustomerFnmLnmGenderDTO> lst=customerService.findByLastName(lnm);
		if(lst.isEmpty())
		{
			log.info("no record found");
			ApiResponse apiresponse=new ApiResponse(HttpStatus.NOT_FOUND.value(), "no customer found ");
			return new ResponseEntity<ApiResponse>(apiresponse,HttpStatus.NOT_FOUND);
		}
		ApiResponse apiresponse=new ApiResponse(HttpStatus.FOUND.value(), "customer found ", lst);
		
		return new ResponseEntity<>(apiresponse,HttpStatus.OK);
	}
	@GetMapping(value="/get/findByFirstNameIgnoreCase/{fn}")
	public  ResponseEntity<ApiResponse> findByFirstNameIgnoreCase(@PathVariable String fn)
	{
		log.info("inside controller findByFirstNameIgnoreCase fn="+ fn);
		List<Customer> lst=customerService.findByFirstNameIgnoreCase(fn);
		if(lst.isEmpty())
		{
			log.info("no record found");
			ApiResponse apiresponse=new ApiResponse(HttpStatus.NOT_FOUND.value(), "no customer found ");
			return new ResponseEntity<ApiResponse>(apiresponse,HttpStatus.NOT_FOUND);
		}
		ApiResponse apiresponse=new ApiResponse(HttpStatus.FOUND.value(), "customer found ", lst);
		
		return new ResponseEntity<>(apiresponse,HttpStatus.OK);
	}
	@GetMapping(value="/get/findByFirstNameLike/{fn}")
	public  ResponseEntity<ApiResponse> findByFirstNameLike(@PathVariable String fn)
	{
		log.info("inside controller findByFirstNameLike fn="+ fn);
		List<Customer> lst=customerService.findByFirstNameLike(fn);
		if(lst.isEmpty())
		{
			log.info("no record found");
			ApiResponse apiresponse=new ApiResponse(HttpStatus.NOT_FOUND.value(), "no customer found ");
			return new ResponseEntity<ApiResponse>(apiresponse,HttpStatus.NOT_FOUND);
		}
		ApiResponse apiresponse=new ApiResponse(HttpStatus.FOUND.value(), "customer found ", lst);
		
		return new ResponseEntity<>(apiresponse,HttpStatus.OK);
	}
//	List<Customer> findByFirstNameContaining(String fnm);
//	List<Customer> findByfirstNameContains(String fnm);
//	List<Customer> findByfirstNameIsContaining(String fnm);
	
	@GetMapping(value="/get/findByFirstNameContaining/{fn}")
	public  ResponseEntity<ApiResponse> findByFirstNameContaining(@PathVariable String fn)
	{
		log.info("inside controller findByFirstNameContaining fn="+ fn);
		List<Customer> lst=customerService.findByFirstNameContaining(fn);
		if(lst.isEmpty())
		{
			log.info("no record found");
			ApiResponse apiresponse=new ApiResponse(HttpStatus.NOT_FOUND.value(), "no customer found ");
			return new ResponseEntity<ApiResponse>(apiresponse,HttpStatus.NOT_FOUND);
		}
		ApiResponse apiresponse=new ApiResponse(HttpStatus.FOUND.value(), "customer found ", lst);
		
		return new ResponseEntity<>(apiresponse,HttpStatus.OK);
	}
	@GetMapping(value="/get/findByfirstNameContains/{fn}")
	public  ResponseEntity<ApiResponse> findByfirstNameContains(@PathVariable String fn)
	{
		log.info("inside controller findByfirstNameContains fn="+ fn);
		List<Customer> lst=customerService.findByfirstNameContains(fn);
		if(lst.isEmpty())
		{
			log.info("no record found");
			ApiResponse apiresponse=new ApiResponse(HttpStatus.NOT_FOUND.value(), "no customer found ");
			return new ResponseEntity<ApiResponse>(apiresponse,HttpStatus.NOT_FOUND);
		}
		ApiResponse apiresponse=new ApiResponse(HttpStatus.FOUND.value(), "customer found ", lst);
		
		return new ResponseEntity<>(apiresponse,HttpStatus.OK);
	}
	
	@GetMapping(value="/get/findByfirstNameIsContaining/{fn}")
	public  ResponseEntity<ApiResponse> findByfirstNameIsContaining(@PathVariable String fn)
	{
		log.info("inside controller findByfirstNameIsContaining fn="+ fn);
		List<Customer> lst=customerService.findByfirstNameIsContaining(fn);
		if(lst.isEmpty())
		{
			log.info("no record found");
			ApiResponse apiresponse=new ApiResponse(HttpStatus.NOT_FOUND.value(), "no customer found ");
			return new ResponseEntity<ApiResponse>(apiresponse,HttpStatus.NOT_FOUND);
		}
		ApiResponse apiresponse=new ApiResponse(HttpStatus.FOUND.value(), "customer found ", lst);
		
		return new ResponseEntity<>(apiresponse,HttpStatus.OK);
	}

	


}
