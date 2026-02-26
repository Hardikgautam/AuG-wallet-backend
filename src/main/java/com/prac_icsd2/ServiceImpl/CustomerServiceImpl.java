package com.prac_icsd2.ServiceImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prac_icsd2.dto.CustomerLoginDTO;
import com.prac_icsd2.dto.CustomerRequestDto;
import com.prac_icsd2.dto.response.CustomerFnmLnmGenderDTO;
import com.prac_icsd2.exception.EntityAlreadyExistException;
import com.prac_icsd2.exception.ResourceNotFoundException;
import com.prac_icsd2.model.Address;
import com.prac_icsd2.model.Customer;
import com.prac_icsd2.repo.AddressRepo;
import com.prac_icsd2.repo.CustomerRepo;
import com.prac_icsd2.serviceI.CustomerService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	CustomerRepo customerRepo;
	
	@Autowired
	AddressRepo addressRepo;
	
	@Override
	public boolean isValidCustByEmailidAndPwd(CustomerLoginDTO customerLogin) {
		
		System.out.println("1111111111");
		boolean res=true;
		Optional<Customer> optCust=customerRepo.findByEmailIdAndPassword(customerLogin.getEmailId(), customerLogin.getPassword());
		System.out.println("2222222");
		if(optCust.isEmpty())
		{
			res=false;
			throw new ResourceNotFoundException("customer is not existing for email id "+ customerLogin.getEmailId() + " and pwd= "+ customerLogin.getPassword());
		}
		
		return res;
	}

	@Override
	public Customer saveCustomer(Customer cust) {
		return null;
	}

	@Override
	public Customer getCustomerByEmailid(String strEmailId) {
		log.info("finding customer for email id "+ strEmailId);
		Optional<Customer> optCust=customerRepo.findByEmailId(strEmailId);
		if(optCust.isEmpty())
		{
			//email id is already present
			log.info("customer is not present for email id "+ strEmailId);
			throw new  ResourceNotFoundException("customer do not exist for email id "+ strEmailId);
		}
		Customer cust=optCust.get();
		log.info("custome is present for eid "+ strEmailId);
		return cust;
	}

	@Override
	public Customer getCustomerByCustId(String strCustId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer createCustomer(@Valid CustomerRequestDto crDto) {
		log.info("Inside save customer of service with given request"+crDto);
		System.out.println("inside save customer of service with given request "+ crDto);
		//step 1 : validate 
		
		Optional<Customer> optCust=customerRepo.findByEmailId(crDto.getEmailId());
		if(optCust.isPresent())
		{
			//email id is already present
			throw new EntityAlreadyExistException("Customer email id is already existing ");
		}
		
		//step 2 : convert dto to entity
		Address add=Address.builder()
				.addressline1(crDto.getAddressLine1())
				.addressline2(crDto.getAddressLine2())
				.city(crDto.getCity())
				.state(crDto.getState())
				.pincode(crDto.getPincode())
				.build();
	
//		Address addCreated=addressRepo.save(add);
		
		
		Customer cust=Customer.builder()
				.firstName(crDto.getFirstName())
				.lastName(crDto.getLastName())
				.emailId(crDto.getEmailId())
				.contactNo(crDto.getContactNo())
				.address(add)
				.gender(crDto.getGender())
				.password(crDto.getPassword())
				.registerationDate(LocalDate.now())
				.build();
		
		System.out.println("cust entity is now from builder : "+ cust);
		log.info("Cust enttity saved",cust);;
		Customer custCreated=customerRepo.save(cust);
		
		//step 3 : save entity and send response
		
		
		return custCreated.getCustomerId();
		//return 100;
	}

	@Override
	public List<Customer> findByFirstNameLike(String fn) {
		
		log.info("inside List<Customer> findByFirstNameLike(String fn)");
		//List<Customer> lst=customerRepo.findByFirstNameLike("%"+fn+"%");- 
		/*
		 * //	List<Customer> findByFirstNameContaining(String fnm);
//	List<Customer> findByfirstNameContains(String fnm);
//	List<Customer> findByfirstNameIsContaining(String fnm);
 * same as like findByFirstNameLike("%"+fn+"%")*/
		
	//	List<Customer> lst=customerRepo.findByFirstNameLike("%"+fn);//t in last ex scott-ex %t
		List<Customer> lst=customerRepo.findByFirstNameLike(fn+"%");//t in last ex smith,scott-ex s%
		
		return lst;
	}

	@Override
	public List<Customer> findByFirstNameContaining(String fnm) {
		
		List <Customer> lst=customerRepo.findByfirstNameIsContaining(fnm);
		return lst;
	}

	@Override
	public List<Customer> findByfirstNameContains(String fnm) {
		List <Customer> lst=customerRepo.findByfirstNameContains(fnm);
		return lst;
	}

	@Override
	public List<Customer> findByfirstNameIsContaining(String fnm) {
		List <Customer> lst=customerRepo.findByfirstNameIsContaining(fnm);
		return lst;
	}
	public List<Customer> findByFirstNameIgnoreCase(String fn)
	{
		List <Customer> lst=customerRepo.findByFirstNameIgnoreCase(fn);
		return lst;
	}
	public List<CustomerFnmLnmGenderDTO> findByLastName(String lnm)
	{
		
		List <CustomerFnmLnmGenderDTO> lst=customerRepo.findByLastName(lnm);
		return lst;
	}
	public Boolean existsByEmailIdAndPassword(String email, String password)
	{
		return customerRepo.existsByEmailIdAndPassword(email, password);
		
	}

	@Override
	public Customer registerCustomer(Customer customer) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Customer getCustomerByEmailAndPassword(String email, String password) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean isCustomerExistsByID(int customerID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Customer getCustomerByEmailId(String strEmailId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Customer getCustomerByCustomerId(int strCustomerId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Customer> getCustomerExpiringTodayOrTomorrow() {
		// TODO Auto-generated method stub
		return null;
	}
	


}
