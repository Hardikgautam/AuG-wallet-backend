package com.prac_icsd2.controller;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prac_icsd2.aop.LogExecutionTime;
import com.prac_icsd2.dto.AccountRequestDTO;
import com.prac_icsd2.dto.common.ApiResponse;
import com.prac_icsd2.enums.AccountType;
import com.prac_icsd2.model.Account;
import com.prac_icsd2.serviceI.AccountService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/account")
@CrossOrigin(value = "http://localhost:3000")
@Slf4j
@RequiredArgsConstructor
public class AccountController {


	private final AccountService accService;
//	public AccountController(AccountService accService)
//	{
//		this.accService=accService;
//	}
//	
	//public Account saveAccount(Account acc)
	
	
	@LogExecutionTime
	@PostMapping(value="/create")
	public ResponseEntity<ApiResponse> createAccount(@Valid @RequestBody  AccountRequestDTO accountRequest)
	{
		log.info("creating a new Account");
		System.out.println(accountRequest);//AccountRequestDTO(accountNumber=1, customerId=1, accountType=CURRENT, openingBalance=10, openingDate=2022-09-29, description=desc)
		
		int accountNumber=accService.saveAccount(accountRequest);
		
		ApiResponse apiresponse=new ApiResponse(HttpStatus.OK.value()	, "account created successfully", accountNumber);
		return new ResponseEntity<ApiResponse>(apiresponse,HttpStatus.OK);
	}
	
	@LogExecutionTime
	@GetMapping(value="/get/{customerId}")
	public ResponseEntity<ApiResponse> getAllAccountsByCustomerId(@PathVariable int customerId) throws Exception
	{
		//query - should we use custome validation...?
		log.info("get list of accounts by customer id "+ customerId);
		List<Account> lstAccounts=accService.getAccountsByCustId(customerId);
		
		if(lstAccounts.isEmpty())
		{
			ApiResponse apirsponse=new ApiResponse(HttpStatus.NO_CONTENT.value(), "No Accounts found for customer id "+ customerId, null);
			return new ResponseEntity<ApiResponse>(apirsponse,HttpStatus.OK);
		}
		ApiResponse apirsponse=new ApiResponse(HttpStatus.FOUND.value(), "list of accounts", lstAccounts);
		
		
		return new ResponseEntity<ApiResponse>(apirsponse,HttpStatus.OK);
	}
	
	
	//query= iski naming convention kya hogi
	
	//query- @valid not working for path variable - how it will work - not blank is not working 
	@LogExecutionTime
	@GetMapping(value="/getAccountsByEmailId/{emailId}")
	//https://www.baeldung.com/spring-validate-requestparam-pathvariable
	public ResponseEntity<ApiResponse> getAllAccountsByCustomerEmailId(@PathVariable @Valid @NotBlank(message = "emailId name should not be blank")
	@NotNull(message="emailId name should not be null")
	@Email(message="not valid email") String emailId) throws Exception
	{
		//query - should we use custome validation...?
		log.info("get list of accounts by customer eid "+ emailId);
		List<Account> lstAccounts=accService.getAccountsByCustEmailId(emailId);
		
		if(lstAccounts.isEmpty())
		{
			ApiResponse apirsponse=new ApiResponse(HttpStatus.NO_CONTENT.value(), "No Accounts found for customer emailid "+ emailId, null);
			return new ResponseEntity<ApiResponse>(apirsponse,HttpStatus.OK);
		}
		ApiResponse apirsponse=new ApiResponse(HttpStatus.FOUND.value(), "list of accounts", lstAccounts);
		
		
		return new ResponseEntity<ApiResponse>(apirsponse,HttpStatus.OK);
	}
	
	
	
	
	
	//******************************************************************
	@LogExecutionTime
	@GetMapping(value="/findByAccountTypeIn")
	public ResponseEntity<ApiResponse> findByAccountTypeIn()
	{
		//	//select * from account where account_type in ('CURRENT','RD','LOAN');
		List<AccountType> accTypes=new LinkedList<>();
		accTypes.add(AccountType.CURRENT);
		accTypes.add(AccountType.RD);
		accTypes.add(AccountType.LOAN);
				List<Account> lst=accService.findByAccountTypeIn(accTypes);
				ApiResponse apirespnose=new ApiResponse(HttpStatus.OK.value(), "list of accounts",lst);
				return new ResponseEntity<ApiResponse>(apirespnose,HttpStatus.OK);
	}
	
	@LogExecutionTime
	@GetMapping(value="/findByOpeningBalanceNot/{opBal}")
	public ResponseEntity<ApiResponse> findByOpeningBalanceNot(@PathVariable double opBal)
	{
		//where ob<>ipob;
		List<Account> lst=accService.findByOpeningBalanceNot(opBal);
		ApiResponse apirespnose=new ApiResponse(HttpStatus.OK.value(), "list of accounts",lst);
		return new ResponseEntity<ApiResponse>(apirespnose,HttpStatus.OK);
	}
	
	@LogExecutionTime
	@GetMapping(value="/findByOrderByOpeningBalanceAsc")
	public ResponseEntity<ApiResponse> findByOrderByOpeningBalanceAsc()
	{
		List<Account> lst=accService.findByOrderByOpeningBalanceAsc();
		ApiResponse apirespnose=new ApiResponse(HttpStatus.OK.value(), "list of accounts",lst);
		return new ResponseEntity<ApiResponse>(apirespnose,HttpStatus.OK);
	}
	
	@LogExecutionTime
	@GetMapping(value="/findByOpeningDateAfter")
	public ResponseEntity<ApiResponse> findByOpeningDateAfter()
	{
		LocalDate  startDate=LocalDate.of(2022, 10, 7);//yy mm dd
		
		List<Account> lst=accService.findByOpeningDateAfter(startDate);
		ApiResponse apirespnose=new ApiResponse(HttpStatus.OK.value(), "list of accounts",lst);
		return new ResponseEntity<ApiResponse>(apirespnose,HttpStatus.OK);
	}
	
	
	@LogExecutionTime
	@GetMapping(value="/findByOpeningDateBetween")
	public ResponseEntity<ApiResponse> findByOpeningDateBetween()
	{
		LocalDate  startDate=LocalDate.of(2022, 10, 2);//yy mm dd
		LocalDate  endDate=LocalDate.of(2022, 10, 7);
		List<Account> lst=accService.findByOpeningDateBetween(startDate	, endDate);
		ApiResponse apirespnose=new ApiResponse(HttpStatus.OK.value(), "list of accounts",lst);
		return new ResponseEntity<ApiResponse>(apirespnose,HttpStatus.OK);
	}
	
	
	@LogExecutionTime
	@GetMapping(value="/getAccLesThanOpBal/{opBal}")
	public ResponseEntity<ApiResponse> getAllAccountsLessThanOpeningBalance(@PathVariable double opBal) throws Exception
	{

		log.info("get list of accounts less than openingbalance "+ opBal);
		List<Account> lstAccounts=accService.getAccountsLessThanOpBal(opBal);
		
		if(lstAccounts.isEmpty())
		{
			ApiResponse apirsponse=new ApiResponse(HttpStatus.NO_CONTENT.value(), "No Accounts , having opbal less than  "+ opBal, null);
			return new ResponseEntity<ApiResponse>(apirsponse,HttpStatus.OK);
		}
		ApiResponse apirsponse=new ApiResponse(HttpStatus.FOUND.value(), "list of accounts", lstAccounts);
		
		
		return new ResponseEntity<ApiResponse>(apirsponse,HttpStatus.OK);
	}
	
	@LogExecutionTime
	@GetMapping(value="/get/AccLesThanEqualOpBal/{opBal}")
	public ResponseEntity<ApiResponse> getAllAccountsLessThanEqualOpeningBalance(@PathVariable double opBal) throws Exception
	{

		log.info("get list of accounts less than EQUAL openingbalance "+ opBal);
		List<Account> lstAccounts=accService.getAccountsLessThanEqualOpBal(opBal);
		
		if(lstAccounts.isEmpty())
		{
			ApiResponse apirsponse=new ApiResponse(HttpStatus.NO_CONTENT.value(), "No Accounts , having opbal less than  eQUAL"+ opBal, null);
			return new ResponseEntity<ApiResponse>(apirsponse,HttpStatus.OK);
		}
		ApiResponse apirsponse=new ApiResponse(HttpStatus.FOUND.value(), "list of accounts", lstAccounts);
		
		
		return new ResponseEntity<ApiResponse>(apirsponse,HttpStatus.OK);
	
	}
	
	
	@LogExecutionTime
	@GetMapping(value="/get/findByOpeningBalanceGreaterThan/{opBal}")
	public ResponseEntity<ApiResponse> findByOpeningBalanceGreaterThan(@PathVariable double opBal) throws Exception
	{

		log.info("get list of accounts greater  than  openingbalance "+ opBal);
		List<Account> lstAccounts=accService.findByOpeningBalanceGreaterThan(opBal);
		
		if(lstAccounts.isEmpty())
		{
			ApiResponse apirsponse=new ApiResponse(HttpStatus.NO_CONTENT.value(), "No Accounts , having opbal greater than  "+ opBal, null);
			return new ResponseEntity<ApiResponse>(apirsponse,HttpStatus.OK);
		}
		ApiResponse apirsponse=new ApiResponse(HttpStatus.FOUND.value(), "list of accounts", lstAccounts);
		
		
		return new ResponseEntity<ApiResponse>(apirsponse,HttpStatus.OK);
	
	}
	
	
	@LogExecutionTime
	@GetMapping(value="/get/findDistinctByAccountTypeAndOpeningBalance/{accType}/{opBal}")
	public ResponseEntity<ApiResponse> findDistinctByAccountTypeAndOpeningBalance(@PathVariable double opBal,@PathVariable String accType) throws Exception
	{
		log.info("loan and opbalance - distinct entry we want ");
		log.info("inside findDistinctByAccountTypeAndOpeningBalance accType="+ accType+ " opbal ="+ opBal);
		AccountType at=null;
		if(accType.equals("LOAN"))
		{
			at=AccountType.LOAN;
		}
		else if(accType.equals("SALARY"))
		{
			at=AccountType.SALARY;
		}
		
		List<Account> lst=accService.findDistinctByAccountTypeAndOpeningBalance(at, opBal);
		if(lst.isEmpty())
		{
			ApiResponse apirsponse=new ApiResponse(HttpStatus.NO_CONTENT.value(), "No accournts founds- no record found "+ opBal, null);
			return new ResponseEntity<ApiResponse>(apirsponse,HttpStatus.OK);
		}
		ApiResponse apiresponse =new ApiResponse(HttpStatus.FOUND.value(), "list of accounts", lst);
		
		return new ResponseEntity<ApiResponse>(apiresponse,HttpStatus.OK);
	}

	
	@LogExecutionTime
	@GetMapping(value="/get/getDistinctAccType")
	public ResponseEntity<ApiResponse> getDistinctAccType() throws Exception
	{
		log.info("accType - distinct entry we want ");
		
		
		List<String> lst=accService.getDistinctAccType();
		if(lst.isEmpty())
		{
			ApiResponse apirsponse=new ApiResponse(HttpStatus.NO_CONTENT.value(), "No accourntType founds- no record found ", null);
			return new ResponseEntity<ApiResponse>(apirsponse,HttpStatus.OK);
		}
		ApiResponse apiresponse =new ApiResponse(HttpStatus.FOUND.value(), "list of accountsType", lst);
		
		return new ResponseEntity<ApiResponse>(apiresponse,HttpStatus.OK);
	}

	@LogExecutionTime
	@GetMapping(value="/get/findDistinctByOpeningBalance/{opBal}")
	public ResponseEntity<ApiResponse> findDistinctByOpeningBalance(@PathVariable double opBal) throws Exception
	{
		log.info("opbalance - distinct entry we want ");
		log.info("inside findDistinctByOpeningBalance  opbal ="+ opBal);
		
		List<Account> lst=accService.findDistinctByOpeningBalance(opBal);
		if(lst.isEmpty())
		{
			ApiResponse apirsponse=new ApiResponse(HttpStatus.NO_CONTENT.value(), "No accournts founds- no record found "+ opBal, null);
			return new ResponseEntity<ApiResponse>(apirsponse,HttpStatus.OK);
		}
		ApiResponse apiresponse =new ApiResponse(HttpStatus.FOUND.value(), "list of accounts", lst);
		
		return new ResponseEntity<ApiResponse>(apiresponse,HttpStatus.OK);
	}

	
	


}
