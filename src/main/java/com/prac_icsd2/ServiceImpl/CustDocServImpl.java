package com.prac_icsd2.ServiceImpl;

import java.io.IOException;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.prac_icsd2.model.Customer;
import com.prac_icsd2.model.CustomerDocuments;
import com.prac_icsd2.repo.CustDocRepo;
import com.prac_icsd2.serviceI.CustDocServ;

@Service
public class CustDocServImpl implements CustDocServ{

	@Autowired
	private CustDocRepo repo;
	@Override
	public int savedoc(Customer customer, MultipartFile file) throws IOException {

	CustomerDocuments doc = CustomerDocuments.builder()
			.customer(customer)
			.filetype(file.getContentType())
			.uploadDate(LocalDate.now())
			.filedata(file.getBytes())
			.documentName(null)
			.build();
	
	CustomerDocuments savedDoc = repo.save(doc);
	
	return savedDoc.getDocumentId();
	}

	@Override
	public CustomerDocuments fromCustDocId(int custdocid) {
		return repo.findById(custdocid)
	               .orElseThrow(() -> new RuntimeException("Document not found with ID: " + custdocid));	}
	
}