package com.prac_icsd2.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.prac_icsd2.aop.TrackMethod;
import com.prac_icsd2.dto.BulkUploadResultDTO;
import com.prac_icsd2.dto.CustomerLoginDTO;
import com.prac_icsd2.dto.CustomerRequestDto;
import com.prac_icsd2.dto.common.ApiResponse;
import com.prac_icsd2.dto.response.CustomerFnmLnmGenderDTO;
import com.prac_icsd2.dto.response.CustomerPageResponseDTO;
import com.prac_icsd2.enums.Gender;
import com.prac_icsd2.exception.ResourceNotFoundException;
import com.prac_icsd2.model.Address;
import com.prac_icsd2.model.Customer;
import com.prac_icsd2.repo.CustomerRepo;
import com.prac_icsd2.serviceI.CustomerService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@CrossOrigin(value = "http://localhost:3000")
@RequiredArgsConstructor
@Slf4j
@RequestMapping(value = "/customer")
public class CustController {

    @Autowired
    CustomerService customerService;

    @Autowired
    CustomerRepo customerRepo;

    @GetMapping(value = "/create")
    public Customer createcustomer() {
        Address addr = Address.builder()
                .addressid(3)
                .addressline1("Ram Nagar Gali NO.3")
                .addressline2("Jatal Road")
                .city("Panipat")
                .state("Haryana")
                .pincode("132105")
                .build();

        Customer c1 = Customer.builder()
                .firstName("Kartik")
                .lastName("Sharma")
                .emailId("sharmakartik090@gmail.com")
                .contactNo("7988209039")
                .address(addr)
                .gender(Gender.MALE)
                .password("hardik")
                .registerationDate(LocalDate.now())
                .build();

        customerService.saveCustomer(c1);
        return c1;
    }

    @PostMapping(value = "/create")
    public ResponseEntity<ApiResponse> createCustomer(@RequestBody @Valid CustomerRequestDto customerRequest) {
        log.info("inside create method of customer controller");
        Integer generatedCustomerId = customerService.createCustomer(customerRequest);
        ApiResponse apiResponse = new ApiResponse(HttpStatus.OK.value(), "Customer Created Successfully", generatedCustomerId);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PostMapping(value = "/isValidUser")
    public ResponseEntity<ApiResponse> isValidUser(@RequestBody @Valid CustomerLoginDTO customerLogin) {
        log.info("authenticating user - valid or not");
        boolean res = customerService.isValidCustByEmailidAndPwd(customerLogin);
        ApiResponse apiresponse = new ApiResponse(HttpStatus.OK.value(), "user is validated", res);
        return new ResponseEntity<>(apiresponse, HttpStatus.OK);
    }

  
    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllCustomers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "firstName") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String search) {

        log.info("getAllCustomers page={} size={} sortBy={} sortDir={} search={}",
                 page, size, sortBy, sortDir, search);

        Page<CustomerPageResponseDTO> result =
                customerService.getAllCustomers(page, size, sortBy, sortDir, search);

        if (result.isEmpty()) {
            ApiResponse apiResponse = new ApiResponse(HttpStatus.OK.value(), "No customers found", result);
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        }

        ApiResponse apiResponse = new ApiResponse(HttpStatus.OK.value(), "Customers fetched successfully", result);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @TrackMethod
    @GetMapping("/getById/{id}")
    public ResponseEntity<ApiResponse> getCustomerById(@PathVariable Integer id) {
        log.info("getCustomerById id={}", id);
        Customer customer = customerRepo.findByIdWithAllDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + id));
        ApiResponse apiResponse = new ApiResponse(HttpStatus.OK.value(), "Customer found", customer);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> updateCustomer(
            @PathVariable Integer id,
            @RequestBody CustomerRequestDto dto) {
        log.info("updateCustomer id={}", id);
        Customer existing = customerRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + id));
        existing.setFirstName(dto.getFirstName());
        existing.setLastName(dto.getLastName());
        existing.setEmailId(dto.getEmailId());
        existing.setContactNo(dto.getContactNo());
        customerRepo.save(existing);
        ApiResponse apiResponse = new ApiResponse(HttpStatus.OK.value(), "Customer updated successfully");
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteCustomer(@PathVariable Integer id) {
        log.info("deleteCustomer id={}", id);
        if (!customerRepo.existsById(id)) {
            throw new ResourceNotFoundException("Customer not found: " + id);
        }
        customerRepo.deleteById(id);
        ApiResponse apiResponse = new ApiResponse(HttpStatus.OK.value(), "Customer deleted successfully");
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping(value = "/get/findByLastName/{lnm}")
    public ResponseEntity<ApiResponse> findByLastNameDTOResponse(@PathVariable String lnm) {
        log.info("inside controller findByLastName lnm=" + lnm);
        List<CustomerFnmLnmGenderDTO> lst = customerService.findByLastName(lnm);
        if (lst.isEmpty()) {
            ApiResponse apiresponse = new ApiResponse(HttpStatus.NOT_FOUND.value(), "no customer found");
            return new ResponseEntity<>(apiresponse, HttpStatus.NOT_FOUND);
        }
        ApiResponse apiresponse = new ApiResponse(HttpStatus.FOUND.value(), "customer found", lst);
        return new ResponseEntity<>(apiresponse, HttpStatus.OK);
    }

    @GetMapping(value = "/get/findByFirstNameIgnoreCase/{fn}")
    public ResponseEntity<ApiResponse> findByFirstNameIgnoreCase(@PathVariable String fn) {
        log.info("inside controller findByFirstNameIgnoreCase fn=" + fn);
        List<Customer> lst = customerService.findByFirstNameIgnoreCase(fn);
        if (lst.isEmpty()) {
            ApiResponse apiresponse = new ApiResponse(HttpStatus.NOT_FOUND.value(), "no customer found");
            return new ResponseEntity<>(apiresponse, HttpStatus.NOT_FOUND);
        }
        ApiResponse apiresponse = new ApiResponse(HttpStatus.FOUND.value(), "customer found", lst);
        return new ResponseEntity<>(apiresponse, HttpStatus.OK);
    }

    @GetMapping(value = "/get/findByFirstNameLike/{fn}")
    public ResponseEntity<ApiResponse> findByFirstNameLike(@PathVariable String fn) {
        log.info("inside controller findByFirstNameLike fn=" + fn);
        List<Customer> lst = customerService.findByFirstNameLike(fn);
        if (lst.isEmpty()) {
            ApiResponse apiresponse = new ApiResponse(HttpStatus.NOT_FOUND.value(), "no customer found");
            return new ResponseEntity<>(apiresponse, HttpStatus.NOT_FOUND);
        }
        ApiResponse apiresponse = new ApiResponse(HttpStatus.FOUND.value(), "customer found", lst);
        return new ResponseEntity<>(apiresponse, HttpStatus.OK);
    }

    @GetMapping(value = "/get/findByFirstNameContaining/{fn}")
    public ResponseEntity<ApiResponse> findByFirstNameContaining(@PathVariable String fn) {
        log.info("inside controller findByFirstNameContaining fn=" + fn);
        List<Customer> lst = customerService.findByFirstNameContaining(fn);
        if (lst.isEmpty()) {
            ApiResponse apiresponse = new ApiResponse(HttpStatus.NOT_FOUND.value(), "no customer found");
            return new ResponseEntity<>(apiresponse, HttpStatus.NOT_FOUND);
        }
        ApiResponse apiresponse = new ApiResponse(HttpStatus.FOUND.value(), "customer found", lst);
        return new ResponseEntity<>(apiresponse, HttpStatus.OK);
    }

    @GetMapping(value = "/get/findByfirstNameContains/{fn}")
    public ResponseEntity<ApiResponse> findByfirstNameContains(@PathVariable String fn) {
        log.info("inside controller findByfirstNameContains fn=" + fn);
        List<Customer> lst = customerService.findByfirstNameContains(fn);
        if (lst.isEmpty()) {
            ApiResponse apiresponse = new ApiResponse(HttpStatus.NOT_FOUND.value(), "no customer found");
            return new ResponseEntity<>(apiresponse, HttpStatus.NOT_FOUND);
        }
        ApiResponse apiresponse = new ApiResponse(HttpStatus.FOUND.value(), "customer found", lst);
        return new ResponseEntity<>(apiresponse, HttpStatus.OK);
    }

    @GetMapping(value = "/get/findByfirstNameIsContaining/{fn}")
    public ResponseEntity<ApiResponse> findByfirstNameIsContaining(@PathVariable String fn) {
        log.info("inside controller findByfirstNameIsContaining fn=" + fn);
        List<Customer> lst = customerService.findByfirstNameIsContaining(fn);
        if (lst.isEmpty()) {
            ApiResponse apiresponse = new ApiResponse(HttpStatus.NOT_FOUND.value(), "no customer found");
            return new ResponseEntity<>(apiresponse, HttpStatus.NOT_FOUND);
        }
        ApiResponse apiresponse = new ApiResponse(HttpStatus.FOUND.value(), "customer found", lst);
        return new ResponseEntity<>(apiresponse, HttpStatus.OK);
    }

    @PostMapping(value = "/bulk-upload", consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse> bulkUpload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty() || !file.getOriginalFilename().endsWith(".xlsx")) {
            return new ResponseEntity<>(new ApiResponse(400, "Please upload a valid .xlsx file"), HttpStatus.BAD_REQUEST);
        }
        BulkUploadResultDTO result = customerService.bulkCreateCustomers(file);
        String msg = "Processing complete. Success: " + result.getSuccessCount() + ", Failed: " + result.getFailureCount();
        return new ResponseEntity<>(new ApiResponse(200, msg, result), HttpStatus.OK);
    }

    @GetMapping("/download-template")
    public ResponseEntity<Resource> downloadTemplate() {
        Resource resource = new ClassPathResource("templates/sampletemp.xlsx");
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(resource);
    }
}