package com.prac_icsd2.serviceI;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.prac_icsd2.model.Customer;
import com.prac_icsd2.model.CustomerDocuments;

public interface CustDocServ{
	
	int savedoc(Customer customer, MultipartFile file) throws IOException;
	CustomerDocuments fromCustDocId(int custdocid);
}