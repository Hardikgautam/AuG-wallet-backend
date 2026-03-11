package com.prac_icsd2.ServiceImpl;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.prac_icsd2.enums.DocType;
import com.prac_icsd2.exception.ResourceNotFoundException;
import com.prac_icsd2.model.Customer;
import com.prac_icsd2.model.CustomerDocuments_Path;
import com.prac_icsd2.repo.CustomerRepo;
import com.prac_icsd2.repo.DocumentRepository;
import com.prac_icsd2.serviceI.ImageService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ImageServiceImpl implements ImageService {

	@Autowired
	private DocumentRepository docRepo;
	
	@Autowired
	private CustomerRepo customerRepo;
	
	@Value("${file.upload-dir}")
	private String BASE_DIR;
	
	
	@Override
	public int saveDoc(MultipartFile file, int customerId, DocType docType) throws IOException {
		// TODO Auto-generated method stub
		
		//check if customer exists or not 
		Optional<Customer> optCust = customerRepo.findById(customerId);
		 
		if(optCust.isEmpty()) {
			throw new ResourceNotFoundException("Kindly register as a customer first");
		}
		Customer customer = optCust.get();
		
		//check for same document typre softdelete
		
		Optional<CustomerDocuments_Path> existing = docRepo.findByCustomer_CustomerIdAndDocnameAndStatusTrue(customerId,docType);
		
		if(existing.isPresent()) {
			throw new IllegalStateException("A"+ docType.name()+"document already exists, Delete the esisting one to upload a new one");
		}
		
		//create folder per customer
		Path folder = Paths.get(BASE_DIR, "customer_"+customerId);
		Files.createDirectories(folder);
		
		//Build unique filenname and save to disk 
		
		String fileName = docType.name()+"_"+System.currentTimeMillis()+"_"+file.getOriginalFilename();
		Files.write(folder.resolve(fileName),file.getBytes());
		
		//Store relative path in DB
		String filePath = "/customer_"+customerId+"/"+fileName;
		
		//Build and save entity
		CustomerDocuments_Path doc = CustomerDocuments_Path.builder()
				.customer(customer)
				.docname(docType)
				.filetype(filePath)
				.filePath(filePath)
				.fileName(fileName)
				.uploadDate(LocalDate.now())
				.status(true)
				.build();
		
		CustomerDocuments_Path saved = docRepo.save(doc);
		return saved.getDocumentid();
				
	}

	@Override
	public CustomerDocuments_Path getDocByDocumentId(int documentId) {
		// TODO Auto-generated method stub
		return docRepo.findById(documentId)
				.orElseThrow(()-> new ResourceNotFoundException("Document not found with id:"+documentId));
	}

	@Override
	public List<CustomerDocuments_Path> getDocsByCustomerId(int customerId) {
		// TODO Auto-generated method stub
		return docRepo.findByCustomer_CustomerIdAndStatusTrue(customerId);
	}

	@Override
	public void deleteDoc(int documentId) {
		// TODO Auto-generated method stub
		CustomerDocuments_Path doc = docRepo.findById(documentId)
				.orElseThrow(()-> new ResourceNotFoundException("Document not found  with id :"+documentId));
		doc.setStatus(false);
		docRepo.save(doc);
	}

	@Override
	public Optional<CustomerDocuments_Path> getDocByDocName(int customerId, DocType docType) {
		// TODO Auto-generated method stub
		return docRepo.findByCustomer_CustomerIdAndDocnameAndStatusTrue(customerId,docType);
	}


	}
	

