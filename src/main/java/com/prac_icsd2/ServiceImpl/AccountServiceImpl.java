package com.prac_icsd2.ServiceImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prac_icsd2.dto.AccountRequestDTO;
import com.prac_icsd2.enums.AccountType;
import com.prac_icsd2.enums.TransactionType; // Ensure you create this Enum
import com.prac_icsd2.exception.ResourceNotFoundException;
import com.prac_icsd2.model.Account;
import com.prac_icsd2.model.Customer;
import com.prac_icsd2.model.TransactionModel;
//import com.prac_icsd2.model.TransactionModel; // Ensure you have this Model
import com.prac_icsd2.repo.AccountRepo;
import com.prac_icsd2.repo.CustomerRepo;
import com.prac_icsd2.serviceI.AccountService;
//import com.prac_icsd2.serviceI.TransactionService; // Ensure you have this Service
import com.prac_icsd2.serviceI.TransactionService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepo accountRepo;

    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private CustomerServiceImpl customerService;

    @Lazy
    @Autowired
    private TransactionService transactionService;

    @Override
    @Transactional 
    
    public int saveAccount(AccountRequestDTO accReq) {
        log.info("Creating a New Account and Initial Transaction");

        System.out.println("Inside saveaccount in accountservice implementation ................................................................................................");
        Optional<Customer> optCust = customerRepo.findById(accReq.getCustomer().getCustomerId());
        if (optCust.isEmpty()) {
            throw new ResourceNotFoundException("Customer is not present in database with ID: " 
                + accReq.getCustomer().getCustomerId());
        }
        Customer cust = optCust.get();

        Account account = Account.builder()
                .accountType(accReq.getAccountType())
                .customer(cust)
                .openingBalance(accReq.getOpeningBalance())
                .description(accReq.getDescription())
                .openingDate(LocalDate.now())
                .build();

        int accNumberCreated = accountRepo.save(account).getAccountnumber();

        TransactionModel tra = TransactionModel.builder()
                .transactionType(TransactionType.CREDIT)
                .transactiondate(LocalDate.now())
                .amount(accReq.getOpeningBalance())
                .description(TransactionType.CREDIT + " for initial opening of " + accNumberCreated)
                .build();

        transactionService.saveTransaction(tra);

        return accNumberCreated;
    }

    @Override
    public List<Account> getAccountsByCustId(int intCustid) {
        log.info("Getting accounts for customer id: " + intCustid);
        List<Account> lstAccounts = accountRepo.findByCustomerCustomerId(intCustid);
        if (lstAccounts.isEmpty()) {
            throw new ResourceNotFoundException("No Account found for customer id " + intCustid);
        }
        return lstAccounts;
    }

    @Override
    public Account getAccountByAccNumber(int accountNumber) {
        return accountRepo.findById(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Account is not existing for account number " + accountNumber));
    }

    @Override
    public List<Account> getAccountsByCustEmailId(String custEmailId) {
        // Using the style of calling the existing internal method
        Customer cust = customerService.getCustomerByEmailid(custEmailId);
        return getAccountsByCustId(cust.getCustomerId());
    }

    @Override
    public List<Account> getAccountsLessThanOpBal(double openingBalance) {
        List<Account> lst = accountRepo.findByOpeningBalanceLessThan(openingBalance);
        if (lst.isEmpty()) {
            throw new ResourceNotFoundException("No accounts found with balance less than: " + openingBalance);
        }
        return lst;
    }

    @Override
    public List<Account> getAccountsLessThanEqualOpBal(double openingBalance) {
        return accountRepo.findByOpeningBalanceLessThanEqual(openingBalance);
    }

    @Override
    public List<Account> findDistinctByAccountTypeAndOpeningBalance(AccountType accType, double openingBalance) {
        return accountRepo.findDistinctByAccountTypeAndOpeningBalance(accType, openingBalance);
    }

    @Override
    public List<Account> findDistinctByOpeningBalance(double openingBalance) {
        return accountRepo.findDistinctByOpeningBalance(openingBalance);
    }

    @Override
    public List<String> getDistinctAccType() {
        return accountRepo.getDistinctAccTypes();
    }

    @Override
    public List<Account> findByOpeningBalanceGreaterThan(double openingBalance) {
        return accountRepo.findByOpeningBalanceGreaterThan(openingBalance);
    }

    @Override
    public List<Account> findByOpeningDateBetween(LocalDate startDate, LocalDate endDate) {
        return accountRepo.findByOpeningDateBetween(startDate, endDate);
    }

    @Override
    public List<Account> findByOpeningDateAfter(LocalDate dt) {
        return accountRepo.findByOpeningDateAfter(dt);
    }

    @Override
    public List<Account> findByOrderByOpeningBalanceAsc() {
        // Updated to Ascending as per the target style
        return accountRepo.findByOrderByOpeningBalanceAsc();
    }

    @Override
    public List<Account> findByOpeningBalanceNot(double ob) {
        return accountRepo.findByOpeningBalanceNot(ob);
    }

    @Override
    public List<Account> findByAccountTypeIn(List<AccountType> accTypes) {
        return accountRepo.findByAccountTypeIn(accTypes);
    }
}