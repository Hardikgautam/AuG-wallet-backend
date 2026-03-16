package com.prac_icsd2.serviceI;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.prac_icsd2.dto.BulkUploadResultDTO;
import com.prac_icsd2.dto.CustomerLoginDTO;
import com.prac_icsd2.dto.CustomerRequestDto;
import com.prac_icsd2.dto.response.CustomerFnmLnmGenderDTO;
import com.prac_icsd2.dto.response.CustomerPageResponseDTO;
import com.prac_icsd2.model.Customer;

@Service
public interface CustomerService {

	
	
	public boolean isValidCustByEmailidAndPwd(CustomerLoginDTO customerlogin);
	public Customer saveCustomer(Customer cust);
	public Customer getCustomerByEmailid(String strEmailId);
	public Customer getCustomerByCustId(String strCustId);
	public Integer createCustomer(@Valid CustomerRequestDto customerRequest);
	public List<Customer> findByFirstNameLike(String fn);
	List<Customer> findByFirstNameContaining(String fnm);
	List<Customer> findByfirstNameContains(String fnm);
	List<Customer> findByfirstNameIsContaining(String fnm);
	List<Customer> findByFirstNameIgnoreCase(String fn);
	List<CustomerFnmLnmGenderDTO> findByLastName(String lnm);
	Boolean existsByEmailIdAndPassword(String email, String password);
	
	Customer registerCustomer(Customer customer);

    Customer getCustomerByEmailAndPassword(String email, String password);

//    Customer createCustomer(@Valid CustomerRequestDto customerRequest) throws SchedulerException;

    Boolean isCustomerExistsByID(int customerID);

    Customer getCustomerByEmailId(String strEmailId);

    Customer getCustomerByCustomerId(int strCustomerId);

    List<Customer> getCustomerExpiringTodayOrTomorrow();
    
    BulkUploadResultDTO bulkCreateCustomers(MultipartFile file);


    Page<CustomerPageResponseDTO> getAllCustomers(int page, int size, String sortBy, String sortDir, String search);

}
