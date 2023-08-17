package edu.miami.bte324.midterm.hkim;

public interface Client extends Person {
	
	public Integer getCheckingBalance();
	public Integer getSavingsBalance();
	public Integer getAccountNumber();
	public Integer getClientID();
	
	public void setCheckingBalance(Integer cBalance);
	public void setSavingsBalance(Integer sBalance);

}
