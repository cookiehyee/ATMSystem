package edu.miami.bte324.midterm.hkim;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ClientImpl extends PersonImpl implements Client {
	
	private Integer checkingBalance;
	private Integer savingsBalance;
	private Integer accountNumber;
	private Integer clientID;
	
	private static Integer clientIDGenerator = 100;
	
	public ClientImpl() {
		
		super();
		
		checkingBalance = null;
		savingsBalance = null;
		accountNumber = null;
		
	}
	
	public ClientImpl(String name, String ssn, Date bDate, Integer cBalance, Integer sBalance, Integer aNumber) {
		
		super(name, ssn, bDate);
		
		this.checkingBalance = cBalance;
		this.savingsBalance = sBalance;
		this.accountNumber = aNumber;
		this.clientID = clientIDGenerator;
		
		++clientIDGenerator;
		
	}
	
	public Integer getCheckingBalance() {
		return checkingBalance;
	}
	
	public Integer getSavingsBalance() {
		return savingsBalance;
	}
	
	public Integer getAccountNumber() {
		return accountNumber;
	}
	
	public Integer getClientID() {
		return clientID;
	}
	
	public void setCheckingBalance(Integer cBalance) {
		this.checkingBalance = cBalance;
	}
	
	public void setSavingsBalance(Integer sBalance) {
		this.savingsBalance = sBalance;
	}
	
	public void setAccountNumber(Integer aNumber) {
		this.accountNumber = aNumber;
	}
	
	public void setClientID(Integer cID) {
		this.clientID = cID;
	}
	
	@Override
	public String toString() {
		Format date = new SimpleDateFormat ("yyyy/MM/dd");
		String formattedDate = date.format(birthDate);
		
		return formattedDate + " " + accountNumber + " " + lastName + ", " + firstName + " " + SSN + " $" + savingsBalance + " $" + checkingBalance;
	}
	
}
