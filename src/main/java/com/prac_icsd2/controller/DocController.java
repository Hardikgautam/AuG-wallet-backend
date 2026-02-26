//package com.prac_icsd2.controller;
//
//import java.io.File;
//import java.io.IOException;
//import java.net.MalformedURLException;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.io.UrlResource;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;
//
//import com.prac_icsd2.dto.common.ApiResponse;
//import com.prac_icsd2.exception.ResourceNotFoundException;
//import com.prac_icsd2.model.Customer;
//import com.prac_icsd2.model.CustomerDocuments;
//import com.prac_icsd2.serviceI.CustomerService;
//
//import jakarta.annotation.Resource;
//
//@RestController
//@RequestMapping("/customerDoc")
//public class DocController {
//
//	@Autowired
//	CustomerService cs;
//	@Autowired
//	CustDocServ csds;
//
//	/**
//	 * This method helps in uploading the document of a customer
//	 * Accepted file types are jpg/jpeg/svg/png
//	 * @param id
//	 * @param fileType
//	 * @param file
//	 * @return
//	 */
//	@PostMapping("/uploadDocument/{id}")
//	public ResponseEntity<ApiResponse> uploadDocument(@PathVariable int id, @RequestParam("fileType") String fileType,
//			@RequestParam("file") MultipartFile file) throws IOException {
//		Customer customer = cs.getCustomerByCustomerId(id)
//;
//		if (customer == null) {
//			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//		} else {
//			int documentId=csds.savedoc(customer, fileType, file);
//			ApiResponse apiResponse = new ApiResponse(HttpStatus.OK.value(), "File Uploaded Successfully", documentId);
//			return new ResponseEntity<>(apiResponse, HttpStatus.OK);
//		}
//	}
//	
//	@GetMapping("/getDocument/{id}")
//	public ResponseEntity<Resource> getfilebydocid(@PathVariable int id) throws MalformedURLException {
//		CustomerDocuments custdoc=csds.fromCustDocId(id)
//;
//		File f=new File(custdoc.getUploadedDocumentspath());
//		
//        UrlResource resource = new UrlResource(f.toURI());
//        Resource res;
//        if(resource.exists()) {
//             res=  resource;
//        } else {
//            throw new ResourceNotFoundException("File not found " + custdoc.getUploadedDocumentspath());
//        }
//		 return ResponseEntity.ok()
//				 .contentType(MediaType.IMAGE_JPEG)
//	                .body(res);
//	}
//	
//	
//
//}
