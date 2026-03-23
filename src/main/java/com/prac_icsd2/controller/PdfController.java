package com.prac_icsd2.controller;

import java.io.ByteArrayInputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prac_icsd2.ServiceImpl.PdfService;
import com.prac_icsd2.model.Customer;
import com.prac_icsd2.repo.CustomerRepo;

@RestController
@CrossOrigin(value = "http://localhost:3000")
@RequestMapping("/pdf")
public class PdfController {

	@Autowired
	private PdfService pdfService;

	@Autowired
	private CustomerRepo customerRepository;

	@GetMapping("/createPdf")
	public ResponseEntity<InputStreamResource> createPdf() {
		ByteArrayInputStream pdf = pdfService.createPdf();
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("Content-Disposition", "inline; filename=customer.pdf");
		return ResponseEntity.ok()
				.headers(httpHeaders)
				.contentType(MediaType.APPLICATION_PDF)
				.body(new InputStreamResource(pdf));
	}

	@GetMapping("/customer/{customerId}")
	public ResponseEntity<InputStreamResource> getCustomerPdf(@PathVariable Integer customerId) {

		Customer customer = customerRepository.findById(customerId).orElse(null);

		if (customer == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}

		String rawName = customer.getFirstName() + "_" + customer.getLastName();
		String safeName = rawName.replaceAll("[^a-zA-Z0-9_\\-]", "_");
		String encodedName;

		try {
			encodedName = URLEncoder.encode(safeName + ".pdf", StandardCharsets.UTF_8.name());
		} catch (Exception e) {
			encodedName = "customer.pdf";
		}

		ByteArrayInputStream pdf = pdfService.generateCustomerPdf(customerId);

		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_DISPOSITION,
				"inline; filename=\"" + safeName + ".pdf\"; filename*=UTF-8''" + encodedName);

		return ResponseEntity.ok()
				.headers(headers)
				.contentType(MediaType.APPLICATION_PDF)
				.body(new InputStreamResource(pdf));
		
		
		//customerby customerid is working only so only use this claude
	}
}