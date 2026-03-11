package com.prac_icsd2.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.prac_icsd2.dto.common.ApiResponse;
import com.prac_icsd2.enums.DocType;
import com.prac_icsd2.model.CustomerDocuments_Path;
import com.prac_icsd2.serviceI.ImageService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/image")
@CrossOrigin(origins = "http://localhost:3000") 
@RequiredArgsConstructor
@Slf4j
public class ImageController {
	
	@Value("${file.upload-dir}")
	private String uploadDir;
	
	@Autowired
	private ImageService imgservice;
	
	@PostMapping(value="/upload/{customerId}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ApiResponse> uploadDocument(
			@PathVariable int customerId,
			@RequestParam("file") MultipartFile file,
			@RequestParam("docType") String docType
			){
		try {
			int docId = imgservice.saveDoc(file,customerId,DocType.valueOf(docType.toUpperCase()));
			ApiResponse response = ApiResponse.builder()
					   .code(HttpStatus.OK.value())
	                    .message("Document Uploaded Successfully")
	                    .data(docId)
	                    .build();
			return new ResponseEntity<>(response,HttpStatus.OK);
			
		}catch(Exception e) {
			log.error("Upload failed",e);
			
			ApiResponse response = ApiResponse.builder()
					.code(HttpStatus.INTERNAL_SERVER_ERROR.value())
					.message("Failed to upload:"+e.getMessage())
					.build();
			
			return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@GetMapping("/get/{docId}")
	public ResponseEntity<ApiResponse> getDocumentByDocumentID(@PathVariable int docId){
		try {
			CustomerDocuments_Path doc = imgservice.getDocByDocumentId(docId);
			if(doc==null ) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(ApiResponse.builder()
								.code(HttpStatus.NOT_FOUND.value())
								.message("Document not found in database")
					.build());
			}
			String cleanPath = doc.getFilePath();
			if(cleanPath.startsWith("/")|| cleanPath.startsWith("\\")) {
				cleanPath = cleanPath.substring(1);
			}
			
			Path fullPath = Paths.get(uploadDir).resolve(cleanPath);
			
			
			if(!Files.exists(fullPath)) {
				log.warn("File missing on disk:"+fullPath.toAbsolutePath());
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(ApiResponse.builder()
								.code(HttpStatus.NOT_FOUND.value())
								.message("Document record exists, but file is missing on server")
									.build());
						
			}
			
			byte[] imageBytes = Files.readAllBytes(fullPath);
			String base64Image = Base64.getEncoder().encodeToString(imageBytes);
			
			 Map<String, Object> data = new HashMap<>();
		        data.put("documentid", doc.getDocumentid());
		        data.put("fileName", doc.getFileName());
		        data.put("filetype", doc.getFiletype());
		        data.put("docname", doc.getDocname());
		        data.put("filePath", doc.getFilePath());
		        data.put("uploadDate", doc.getUploadDate());
		        data.put("status", doc.getStatus());
		        data.put("imageData", base64Image);
		        
		        return ResponseEntity.ok(
		        		ApiResponse.builder()
		        		.code(HttpStatus.OK.value())
		        		.message("Document Fetched Successfully")
		        		.data(data)
		        		.build()
		        		);
		}catch(Exception e) {
			 log.error("Unexpected error for docId: " + docId, e);
		        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
		                .body(ApiResponse.builder()
		                        .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
		                        .message("An unexpected error occurred")
		                        .build()
		                        );

		}
	}		
		
	
	 @GetMapping("/customer/{customerId}")
	    public ResponseEntity<ApiResponse> getDocsByCustomer(@PathVariable int customerId) {
	        List<CustomerDocuments_Path> docs = imgservice.getDocsByCustomerId(customerId);
	        
	        if(docs==null || docs.isEmpty()) {
	        	return ResponseEntity.status(HttpStatus.NOT_FOUND)
	        			.body(ApiResponse.builder()
	        					.code(HttpStatus.NOT_FOUND.value())
	        					.message("No Document found")
	        					.build());
	        }
	        
	        List<Map<String,Object>> responselist = new ArrayList<>();
	        for(CustomerDocuments_Path doc : docs) {
	        	
	        	try {
	        	Path fullPath = Paths.get(uploadDir,doc.getFilePath());
	        	
	        	if(!Files.exists(fullPath)) { 
	        		log.warn("File not found on disk:"+fullPath.toAbsolutePath());
	        		continue;
	        	}
	        	
	        	byte[] imageBytes = Files.readAllBytes(fullPath);
	        	
	        	String base64image = Base64.getEncoder().encodeToString(imageBytes);
	        	
	        	Map<String,Object> map = new HashMap<>();
	        	map.put("documentid", doc.getDocumentid());
	        	map.put("fileName",doc.getFileName());
	        	map.put("filetype",doc.getFiletype());
	        	map.put("docname",doc.getDocname());
	        	map.put("filePath",doc.getFilePath());
	        	map.put("uploadDate",doc.getUploadDate());
	        	map.put("status",doc.getStatus());
	        	map.put("imageData",base64image);
	        	
	        	responselist.add(map);
	        	
	        	
	        }catch(IOException e) {
	        log.error("Error reading file:"+doc.getFilePath(),e);	
	        }
	        }
	        
	        return ResponseEntity.ok(
	        		ApiResponse.builder()
	        		.code(HttpStatus.OK.value())
	        		.message("Document Fetched Successfully")
	        		.data(responselist)
	        		.build()
	        		
	        		);
	        
	 }
	 @GetMapping("/doc/{customerId}/{docType}")
	    public ResponseEntity<ApiResponse> getDocByDocName(
	            @PathVariable int customerId,
	            @PathVariable DocType docType) {
	        Optional<CustomerDocuments_Path> doc = imgservice.getDocByDocName(customerId, docType);
	        ApiResponse response = ApiResponse.builder()
	                .code(HttpStatus.OK.value())
	                .message("Document fetched successfully")
	                .data(doc)
	                .build();
	        return new ResponseEntity<>(response, HttpStatus.OK);
	    }
	 @DeleteMapping("/delete/{docId}")
	    public ResponseEntity<ApiResponse> deleteDoc(@PathVariable int docId) {
	        try {
	            imgservice.deleteDoc(docId);
	            ApiResponse response = ApiResponse.builder()
	                    .code(HttpStatus.OK.value())
	                    .message("Document deleted successfully")
	                    .build();
	            return new ResponseEntity<>(response, HttpStatus.OK);
	        } catch (Exception e) {
	            ApiResponse response = ApiResponse.builder()
	                    .code(HttpStatus.NOT_FOUND.value())
	                    .message("Document not found")
	                    .build();
	            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	        }
	    }
	 
	 @GetMapping("/view/{docId}")
	 public void viewImage (@PathVariable int docId,HttpServletResponse response) throws IOException{
		 
		 CustomerDocuments_Path doc = imgservice.getDocByDocumentId(docId);
		 
		 
		 Path filePath = Paths.get(uploadDir).resolve(doc.getFilePath()).normalize();
		 
		 File file = filePath.toFile();
		 
		 if(file.exists()) {
			 
			 String contentType = Files.probeContentType(filePath);
			 response.setContentType(contentType != null? contentType : MediaType.IMAGE_JPEG_VALUE);

			 try(InputStream in = new FileInputStream(file)){
				 StreamUtils.copy(in, response.getOutputStream());

			 }
		 }else {
			 log.error("file missing at:"+filePath.toAbsolutePath());
			 response.sendError(404,"File not found on disk");
		 }
	 }
	}
	
    
