package com.prac_icsd2.serviceI;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.prac_icsd2.enums.DocType;
import com.prac_icsd2.model.CustomerDocuments_Path;

public interface ImageService {


	int saveDoc(MultipartFile file,int customerId, DocType docType) throws IOException;
	

	CustomerDocuments_Path getDocByDocumentId(int documentId);
	
	
	List<CustomerDocuments_Path> getDocsByCustomerId(int customerId);
	
	
	void deleteDoc(int documentId);
	
	
	Optional<CustomerDocuments_Path> getDocByDocName(int customerId,DocType docType);
}
