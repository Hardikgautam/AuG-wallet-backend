package com.prac_icsd2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prac_icsd2.dto.TransactionDepositRequestDTO;
import com.prac_icsd2.dto.common.ApiResponse;
import com.prac_icsd2.model.TransactionModel;
import com.prac_icsd2.serviceI.TransactionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/transaction")
@CrossOrigin(value = "*")
@Slf4j
@RequiredArgsConstructor
public class TransactionController {
	
	@Autowired
	TransactionService transactionService;
	
	@PostMapping(value="/depositAmountInAccount")
	public ResponseEntity<ApiResponse> depoistAmountInAccount(@RequestBody @Valid  TransactionDepositRequestDTO tdReq)
	{
		TransactionModel trans=transactionService.depositAmountInAccount(tdReq);
		ApiResponse apiresponse =new ApiResponse(HttpStatus.OK.value(),"transaction  completed successfully",trans.getTransactionid());
		return new ResponseEntity<ApiResponse>(apiresponse,HttpStatus.OK);
	
	}
	
	@PostMapping(value="/withDrawAmountInAccount")
	public ResponseEntity<ApiResponse> withDrawAmountInAccount(@RequestBody @Valid  TransactionDepositRequestDTO tdReq)
	{
		//query
		//log.debug("inside depois1111t amount in acccount - inside transaction controller");//it is not working
		log.info("inside withdraw amount in acccount - inside transaction controller");
		System.out.println(tdReq);
		TransactionModel trans=transactionService.WithDrawAmountfromAccount(tdReq);
		ApiResponse apiresponse =new ApiResponse(HttpStatus.OK.value(),"transaction  completed successfully",trans.getTransactionid());
		return new ResponseEntity<ApiResponse>(apiresponse,HttpStatus.OK);
//		ApiResponse apiresponse=new ApiResponse(HttpStatus.OK.value()	, "account created successfully", accountNumber);
//		return new ResponseEntity<ApiResponse>(apiresponse,HttpStatus.OK);
//		
	}
	
	@PostMapping(value="/fundTransfer")
	public ResponseEntity<ApiResponse> fundTransfer(@RequestBody @Valid  TransactionDepositRequestDTO tdReq)
	{
		//query
		//log.debug("inside depois1111t amount in acccount - inside transaction controller");//it is not working
		log.info("inside withdraw amount in acccount - inside transaction controller");
		System.out.println(tdReq);
		TransactionModel trans=transactionService.fundTransferFunction(tdReq);
		ApiResponse apiresponse =new ApiResponse(HttpStatus.OK.value(),"transaction  completed successfully",trans.getTransactionid());
		return new ResponseEntity<ApiResponse>(apiresponse,HttpStatus.OK);
//		ApiResponse apiresponse=new ApiResponse(HttpStatus.OK.value()	, "account created successfully", accountNumber);
//		return new ResponseEntity<ApiResponse>(apiresponse,HttpStatus.OK);
//		
	}
}
