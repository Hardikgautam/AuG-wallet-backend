package com.prac_icsd2.ServiceImpl;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.prac_icsd2.dto.BulkUploadResultDTO;
import com.prac_icsd2.dto.CustomerLoginDTO;
import com.prac_icsd2.dto.CustomerRequestDto;
import com.prac_icsd2.dto.response.CustomerFnmLnmGenderDTO;
import com.prac_icsd2.dto.response.CustomerPageResponseDTO;
import com.prac_icsd2.enums.Gender;
import com.prac_icsd2.enums.SubscriptionType;
import com.prac_icsd2.exception.EntityAlreadyExistException;
import com.prac_icsd2.exception.ResourceNotFoundException;
import com.prac_icsd2.model.Address;
import com.prac_icsd2.model.Customer;
import com.prac_icsd2.model.Subscription;
import com.prac_icsd2.repo.AddressRepo;
import com.prac_icsd2.repo.CustomerRepo;
import com.prac_icsd2.repo.SubscriptionRepo;
import com.prac_icsd2.scheduler.WelcomeEmailSchedulerService;
import com.prac_icsd2.serviceI.CustomerService;
import com.prac_icsd2.serviceI.EmailService;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private jakarta.validation.Validator validator;

    @Autowired
    private WelcomeEmailSchedulerService welcomeEmailSchedulerService;

    @Autowired
    CustomerRepo customerRepo;

    @Autowired
    AddressRepo addressRepo;

    @Autowired
    private EmailService emailService;

    @Autowired
    SubscriptionRepo srepo;

    @Autowired
    @Value("${app.plan.basic.expiry-days:15}")
    private int expdate;

    @Override
    public boolean isValidCustByEmailidAndPwd(CustomerLoginDTO customerLogin) {
        Optional<Customer> optCust = customerRepo.findByEmailIdAndPassword(
                customerLogin.getEmailId(), customerLogin.getPassword());
        if (optCust.isEmpty()) {
            throw new ResourceNotFoundException(
                    "Customer not found for email: " + customerLogin.getEmailId());
        }
        return true;
    }

    @Override
    public Customer saveCustomer(Customer cust) {
        return customerRepo.save(cust);
    }

    @Override
    public Customer getCustomerByEmailid(String strEmailId) {
        log.info("finding customer for email id {}", strEmailId);
        return customerRepo.findByEmailId(strEmailId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Customer not found for email: " + strEmailId));
    }

    @Override
    public Customer getCustomerByCustId(String strCustId) {
        int id = Integer.parseInt(strCustId);
        return customerRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Customer not found for id: " + strCustId));
    }

    @Override
    public Integer createCustomer(@Valid CustomerRequestDto crDto) {
        log.info("Inside createCustomer of service: {}", crDto);

        Optional<Customer> optCust = customerRepo.findByEmailId(crDto.getEmailId());
        if (optCust.isPresent()) {
            throw new EntityAlreadyExistException("Customer email already exists.");
        }

        Address add = Address.builder()
                .addressline1(crDto.getAddressLine1())
                .addressline2(crDto.getAddressLine2())
                .city(crDto.getCity())
                .state(crDto.getState())
                .pincode(crDto.getPincode())
                .build();

        LocalDate startDate  = LocalDate.now();
        LocalDate expiryDate = startDate.plusDays(expdate);

        Subscription subdetails = Subscription.builder()
                .planExpiryDate(expiryDate)
                .planStartDate(startDate)
                .subscriptionType(SubscriptionType.BASIC)
                .build();

        Customer cust = Customer.builder()
                .firstName(crDto.getFirstName())
                .lastName(crDto.getLastName())
                .emailId(crDto.getEmailId())
                .contactNo(crDto.getContactNo())
                .address(add)
                .subscription(subdetails)
                .gender(crDto.getGender())
                .password(crDto.getPassword())
                .registerationDate(LocalDate.now())
                .build();

        Customer custCreated = customerRepo.save(cust);

        welcomeEmailSchedulerService.scheduleWelcomeEmail(
                custCreated.getEmailId(),
                custCreated.getFirstName(),
                subdetails.getSubscriptionType(),
                startDate,
                expiryDate);

        return custCreated.getCustomerId();
    }

    @Override
    public Page<CustomerPageResponseDTO> getAllCustomers(
            int page, int size, String sortBy, String sortDir, String search) {

        log.info("getAllCustomers page={} size={} sortBy={} sortDir={} search={}",
                 page, size, sortBy, sortDir, search);

        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        String searchParam = (search == null || search.trim().isEmpty())
                ? null
                : search.trim().toLowerCase();

        Page<Customer> customerPage = customerRepo.findAllWithSearch(searchParam, pageable);

        return customerPage.map(c -> CustomerPageResponseDTO.builder()
                .customerId(c.getCustomerId())
                .firstName(c.getFirstName())
                .lastName(c.getLastName())
                .emailId(c.getEmailId())
                .contactNo(c.getContactNo())
                .gender(c.getGender())
                .registerationDate(c.getRegisterationDate())
                .city(c.getAddress()  != null ? c.getAddress().getCity()  : "")
                .state(c.getAddress() != null ? c.getAddress().getState() : "")
                .build());
    }

    @Override
    public Customer registerCustomer(Customer customer) {
        return customerRepo.save(customer);
    }

    @Override
    public Customer getCustomerByEmailAndPassword(String email, String password) {
        return customerRepo.findByEmailIdAndPassword(email, password)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Customer not found for email: " + email));
    }

    @Override
    public Boolean isCustomerExistsByID(int customerID) {
        return customerRepo.existsById(customerID);
    }

    @Override
    public Customer getCustomerByEmailId(String strEmailId) {
        return customerRepo.findByEmailId(strEmailId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Customer not found for email: " + strEmailId));
    }

    @Override
    public Customer getCustomerByCustomerId(int strCustomerId) {
        return customerRepo.findById(strCustomerId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Customer not found for id: " + strCustomerId));
    }

    public Customer getCustomerById(Integer customerId) {
        return customerRepo.findByIdWithAllDetails(customerId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Customer not found for id: " + customerId));
    }

    @Override
    public List<Customer> getCustomerExpiringTodayOrTomorrow() {
        LocalDate today    = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);
        List<Customer> result = new ArrayList<>();
        result.addAll(customerRepo.findBySubscription_PlanExpiryDateAndSubscription_SubscriptionTypeNot(
                today, SubscriptionType.PREMIUM));
        result.addAll(customerRepo.findBySubscription_PlanExpiryDateAndSubscription_SubscriptionTypeNot(
                tomorrow, SubscriptionType.PREMIUM));
        return result;
    }

    @Override
    public List<Customer> findByFirstNameLike(String fn) {
        log.info("findByFirstNameLike fn={}", fn);
        return customerRepo.findByFirstNameLike(fn + "%");
    }

    @Override
    public List<Customer> findByFirstNameContaining(String fnm) {
        return customerRepo.findByfirstNameIsContaining(fnm);
    }

    @Override
    public List<Customer> findByfirstNameContains(String fnm) {
        return customerRepo.findByfirstNameContains(fnm);
    }

    @Override
    public List<Customer> findByfirstNameIsContaining(String fnm) {
        return customerRepo.findByfirstNameIsContaining(fnm);
    }

    @Override
    public List<Customer> findByFirstNameIgnoreCase(String fn) {
        return customerRepo.findByFirstNameIgnoreCase(fn);
    }

    @Override
    public List<CustomerFnmLnmGenderDTO> findByLastName(String lnm) {
        return customerRepo.findByLastName(lnm);
    }

    @Override
    public Boolean existsByEmailIdAndPassword(String email, String password) {
        return customerRepo.existsByEmailIdAndPassword(email, password);
    }

    @Override
    @Transactional
    public BulkUploadResultDTO bulkCreateCustomers(MultipartFile file) {
        List<Object[]> dbIdentifiers = customerRepo.findAllUniqueIdentifiers();
        Set<String> dbEmails   = new HashSet<>();
        Set<String> dbContacts = new HashSet<>();

        for (Object[] row : dbIdentifiers) {
            if (row[0] != null) dbEmails.add(row[0].toString().toLowerCase().trim());
            if (row[1] != null) dbContacts.add(row[1].toString().trim());
        }

        List<Customer> customersToSave = new ArrayList<>();
        List<BulkUploadResultDTO.RowErrorDTO> errorList = new ArrayList<>();

        LocalDate startDate  = LocalDate.now();
        LocalDate expiryDate = startDate.plusDays(expdate);

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            validateHeaders(sheet.getRow(0));
            int totalRowsHandled = sheet.getLastRowNum();

            for (int i = 1; i <= totalRowsHandled; i++) {
                Row row = sheet.getRow(i);
                if (row == null || isRowEmpty(row)) continue;

                int rowNum = i + 1;
                CustomerRequestDto dto = mapRowToDto(row);

                if (dto == null || hasAnyNullField(dto)) {
                    errorList.add(createError(rowNum, getCellValue(row, 2),
                            "Missing required fields or invalid Gender"));
                    continue;
                }

                String email   = dto.getEmailId().toLowerCase().trim();
                String contact = dto.getContactNo().trim();

                if (dbEmails.contains(email) || dbContacts.contains(contact)) {
                    errorList.add(createError(rowNum, email, "Email or Contact already exists"));
                    continue;
                }

                Address add = Address.builder()
                        .addressline1(dto.getAddressLine1())
                        .addressline2(dto.getAddressLine2())
                        .city(dto.getCity())
                        .state(dto.getState())
                        .pincode(dto.getPincode())
                        .build();

                Subscription sub = Subscription.builder()
                        .planStartDate(startDate)
                        .planExpiryDate(expiryDate)
                        .subscriptionType(SubscriptionType.BASIC)
                        .build();

                Customer cust = Customer.builder()
                        .firstName(dto.getFirstName())
                        .lastName(dto.getLastName())
                        .emailId(email)
                        .contactNo(contact)
                        .gender(dto.getGender())
                        .password(dto.getPassword())
                        .registerationDate(startDate)
                        .address(add)
                        .subscription(sub)
                        .build();

                customersToSave.add(cust);
                dbEmails.add(email);
                dbContacts.add(contact);
            }

            if (!customersToSave.isEmpty()) {
                customerRepo.saveAll(customersToSave);
                customersToSave.forEach(c -> welcomeEmailSchedulerService.scheduleWelcomeEmail(
                        c.getEmailId(), c.getFirstName(),
                        SubscriptionType.BASIC, startDate, expiryDate));
            }

            return BulkUploadResultDTO.builder()
                    .totalRows(totalRowsHandled)
                    .successCount(customersToSave.size())
                    .failureCount(errorList.size())
                    .errors(errorList)
                    .build();

        } catch (IOException e) {
            log.error("Error reading excel file", e);
            throw new RuntimeException("Excel parsing failed: " + e.getMessage());
        }
    }

    private CustomerRequestDto mapRowToDto(Row row) {
        try {
            String firstName    = getCellValue(row, 0);
            String lastName     = getCellValue(row, 1);
            String email        = getCellValue(row, 2);
            String contactNo    = getCellValue(row, 3);
            String genderRaw    = getCellValue(row, 4).trim().toUpperCase();
            String password     = getCellValue(row, 5);
            String addressLine1 = getCellValue(row, 6);
            String addressLine2 = getCellValue(row, 7);
            String city         = getCellValue(row, 8);
            String state        = getCellValue(row, 9);
            String pincode      = getCellValue(row, 10);

            if (email.isEmpty() || firstName.isEmpty()) return null;

            Gender gender = null;
            try {
                gender = Gender.valueOf(genderRaw);
            } catch (IllegalArgumentException e) {
                log.error("Invalid gender at row {}: {}", row.getRowNum() + 1, genderRaw);
            }

            return CustomerRequestDto.builder()
                    .firstName(firstName).lastName(lastName).emailId(email)
                    .contactNo(contactNo).gender(gender).password(password)
                    .addressLine1(addressLine1).addressLine2(addressLine2)
                    .city(city).state(state).pincode(pincode)
                    .build();

        } catch (Exception e) {
            log.error("Row mapping failed at row {}: {}", row.getRowNum() + 1, e.getMessage());
            return null;
        }
    }

    private String getCellValue(Row row, int colIndex) {
        Cell cell = row.getCell(colIndex, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
        if (cell == null) return "";
        return switch (cell.getCellType()) {
            case STRING  -> cell.getStringCellValue().trim();
            case NUMERIC -> String.valueOf((long) cell.getNumericCellValue());
            default      -> "";
        };
    }

    private boolean isRowEmpty(Row row) {
        for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
            Cell cell = row.getCell(c);
            if (cell != null && cell.getCellType() != CellType.BLANK) return false;
        }
        return true;
    }

    private BulkUploadResultDTO.RowErrorDTO createError(int row, String email, String msg) {
        return BulkUploadResultDTO.RowErrorDTO.builder()
                .rowNumber(row).emailid(email).errors(List.of(msg)).build();
    }

    private boolean hasAnyNullField(CustomerRequestDto dto) {
        if (dto == null) return true;
        return isBlank(dto.getEmailId())       || isBlank(dto.getContactNo())  ||
               isBlank(dto.getFirstName())     || isBlank(dto.getLastName())   ||
               dto.getGender() == null         || isBlank(dto.getPassword())   ||
               isBlank(dto.getAddressLine1())  || isBlank(dto.getAddressLine2()) ||
               isBlank(dto.getCity())          || isBlank(dto.getState())      ||
               isBlank(dto.getPincode());
    }

    private boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }

    private void validateHeaders(Row headerRow) {
        List<String> expectedHeaders = List.of(
                "firstName", "lastName", "emailId", "contactNo", "gender",
                "password", "addressLine1", "addressLine2", "city", "state", "pincode");

        if (headerRow == null) throw new RuntimeException("Uploaded file has no headers");

        for (int i = 0; i < expectedHeaders.size(); i++) {
            String actualHeader = getCellValue(headerRow, i);
            if (!expectedHeaders.get(i).equalsIgnoreCase(actualHeader)) {
                throw new RuntimeException("Template Error: Column mismatch at index " + i +
                        ". Expected '" + expectedHeaders.get(i) + "' but found '" + actualHeader + "'.");
            }
        }
    }
}