package com.prac_icsd2.ServiceImpl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prac_icsd2.dto.TransactionDepositRequestDTO;
import com.prac_icsd2.enums.TransactionType;
import com.prac_icsd2.model.Account;
import com.prac_icsd2.model.TransactionModel;
import com.prac_icsd2.repo.AccountRepo;
import com.prac_icsd2.repo.TransactionRepo;
import com.prac_icsd2.serviceI.TransactionService;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService{

	@Autowired
	TransactionRepo tr;
	@Autowired
	AccountRepo ar;
	// query - is it ok - we are creating reference of impl class not reference of
	// interface class
	@Autowired
	 AccountServiceImpl as;

	@Override
	public TransactionModel saveTransaction(TransactionModel trans) {
		tr.save(trans);
		return trans;
	}

	@Override
	public TransactionModel fundTransferFunction(TransactionDepositRequestDTO trans) {
	//	int fromAccountNumber = tdd.getFromAccount();
		Account fromAcc = as.getAccountByAccNumber(trans.getFromAccount().getAccountnumber());
		
		Account toAcc = as.getAccountByAccNumber(trans.getToAccount().getAccountnumber());
		
		double oldOBFromAcc = fromAcc.getOpeningBalance();
		double newOBFromAcc = oldOBFromAcc - trans.getAmount();
		
		updateOpeningBalanceByAccountNumber(fromAcc, newOBFromAcc);
		
		TransactionModel tra = TransactionModel.builder()
				.transactionType(TransactionType.DEBIT)
	            .transactiondate(LocalDate.now())
	            .amount(trans.getAmount())
	            .description(trans.getTransactionType() +"from" +trans.getFromAccount() + "to"+trans.getToAccount())
	            .fromAccount(trans.getFromAccount())
	            .toAccount(trans.getToAccount())
	            .build();

		tr.save(tra);

		double oldOBToAcc = toAcc.getOpeningBalance();
		double newOBToAcc = oldOBToAcc + trans.getAmount();
		updateOpeningBalanceByAccountNumber(toAcc, newOBToAcc);

		TransactionModel trato = TransactionModel.builder()
				.transactionType(TransactionType.CREDIT)
	            .transactiondate(LocalDate.now())
	            .amount(trans.getAmount())
	            .description(trans.getTransactionType() +"from" +trans.getFromAccount() + "to"+trans.getToAccount())
	            .fromAccount(trans.getFromAccount())
	            .toAccount(trans.getToAccount())
	            .build();
		tr.save(trato);
		return tra;
		
	}
	
	public TransactionModel WithDrawAmountfromAccount(TransactionDepositRequestDTO trans) {
	//int accountNumber = trans.getAccountnumber();
		
		Account fromacc=ar.findById(trans.getFromAccount().getAccountnumber()).orElseThrow(()->new  RuntimeException("From Account not found"));
		//Account toacc=ar.findById(trans.getToAccount().getAccountnumber()).orElseThrow(()->new  RuntimeException("To Account not found"));
		//Account acc = as.getAccountByAccNumber(accountNumber);
		
		TransactionModel tra = TransactionModel.builder()
							.transactionType(TransactionType.DEBIT)
				            .transactiondate(LocalDate.now())
				            .amount(trans.getAmount())
				            .description(trans.getTransactionType() +"from" +trans.getFromAccount().getAccountnumber())
				            .fromAccount(fromacc)
//				            .toAccount(toacc)
				            .build();
		
		double newOpeningBalance;
	
				 newOpeningBalance = fromacc.getOpeningBalance() - trans.getAmount();
				 updateOpeningBalanceByAccountNumber(fromacc, newOpeningBalance);
		
		TransactionModel t = tr.save(tra);
		log.info("transaction saved with details " + tra);

		return t;
	}
	
	@Override
	public TransactionModel depositAmountInAccount(TransactionDepositRequestDTO trans) {
	//int accountNumber = trans.getAccountnumber();
		
		//Account fromacc=ar.findById(trans.getFromAccount().getAccountnumber()).orElseThrow(()->new  RuntimeException("From Account not found"));
		Account toacc=ar.findById(trans.getToAccount().getAccountnumber()).orElseThrow(()->new  RuntimeException("To Account not found"));
		//Account acc = as.getAccountByAccNumber(accountNumber);
		
		TransactionModel tra = TransactionModel.builder()
							.transactionType(TransactionType.CREDIT)
				            .transactiondate(LocalDate.now())
				            .amount(trans.getAmount())
				            .description(trans.getTransactionType() + "to"+trans.getToAccount().getAccountnumber())
//				            .fromAccount(fromacc)
				            .toAccount(toacc)
				            .build();
		
				double newOpeningBalance = toacc.getOpeningBalance() + trans.getAmount();
				updateOpeningBalanceByAccountNumber(toacc, newOpeningBalance);
				
				
		
		TransactionModel t = tr.save(tra);
		log.info("transaction saved with details " + tra);

		return t;
	}

	public void updateOpeningBalanceByAccountNumber(Account acc, double newOpeningBalance) {		
		acc.setOpeningBalance(newOpeningBalance);
		ar.save(acc);
		log.info("account is updated with new updatedbalance " + newOpeningBalance + " accno" + acc.getAccountnumber());	
	}
	@Override
	public List<TransactionModel> getTransactionsByAccountNumber(int accountNumber) {
		// TODO Auto-generated method stub
		return tr.findByAccountNumber(accountNumber);
	}









    
}




