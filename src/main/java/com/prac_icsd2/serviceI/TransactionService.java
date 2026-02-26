package com.prac_icsd2.serviceI;

import java.util.List;

import com.prac_icsd2.dto.TransactionDepositRequestDTO;
import com.prac_icsd2.model.Account;
import com.prac_icsd2.model.TransactionModel;


public interface TransactionService {
	public TransactionModel saveTransaction(TransactionModel trans);
	public TransactionModel WithDrawAmountfromAccount(TransactionDepositRequestDTO tdd);
	public TransactionModel fundTransferFunction(TransactionDepositRequestDTO tdd);
	public TransactionModel depositAmountInAccount(TransactionDepositRequestDTO tdd);
//	public Transaction depositAmountInAccount(TransactionDepositDTO tdd)
//	public int updateOpeningBalanceByAccountNumber(int accountNumber,double newOpeningBalance);
	public void updateOpeningBalanceByAccountNumber(Account acc,double newOpeningBalance);
	public List<TransactionModel> getTransactionsByAccountNumber(int accountNumber);
	
}
