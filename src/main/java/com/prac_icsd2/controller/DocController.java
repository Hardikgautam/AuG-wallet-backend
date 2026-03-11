package com.prac_icsd2.controller;

import java.io.IOException;

import org.springframework.http.ResponseEntity; 
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.prac_icsd2.dto.common.ApiResponse;
import com.prac_icsd2.model.Customer;
import com.prac_icsd2.model.CustomerDocuments;
import com.prac_icsd2.serviceI.CustDocServ;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/customer-docs")
public class DocController {
    
    @Autowired
    private CustDocServ service;
    
    @PostMapping("/upload")
    public ResponseEntity<ApiResponse> uploadFile(@RequestParam("file") MultipartFile file, 
                             @RequestParam("custId") Customer customer) {
        try {
            int id = service.savedoc(customer, file);
            ApiResponse response = new ApiResponse(HttpStatus.OK.value(),"Document uploaded Successfully",id);
            return ResponseEntity.ok(response);
        } catch(IOException e) {
        	ApiResponse errorResponse = new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),"Upload failed:"+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/view/{id}")
    public ResponseEntity<byte[]> viewDocument(@PathVariable int id) {
        CustomerDocuments doc = service.fromCustDocId(id);
        
        if (doc == null || doc.getFiledata() == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(doc.getFiletype()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + doc.getDocumentName().toString() + "\"")
                .body(doc.getFiledata());
    }
}