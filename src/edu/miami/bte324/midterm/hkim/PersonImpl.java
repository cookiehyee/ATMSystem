package edu.miami.bte324.midterm.hkim;

import java.util.Date;

public class PersonImpl implements Person {
	
	protected String lastName;
	protected String firstName;
	protected String SSN;
	protected Date birthDate;
	
	public PersonImpl() {
		
		firstName = null;
		lastName = null;
		SSN = null;
		birthDate = null;
		
	}
	
	public PersonImpl(String name, String ssn, Date bDate) {
		
		this.firstName = firstName(name);
		this.lastName = lastName(name);
		this.SSN = ssn;
		this.birthDate = bDate;
		
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public String getSSN() {
		return SSN;
	}
	
	public Date getBirthDate() {
		return birthDate;
	}
	
	public void setLastName(String lName) {
		this.lastName = lName;
	}
	
	public void setFirstName(String fName) {
		this.firstName = fName;
	}
	
	public void setSSN(String ssn) {
		this.SSN = ssn;
	}
	
	public void setBirthDate(Date bDate) {
		this.birthDate = bDate;
	}
	
	private static String firstName(String name) {
		String[] names = name.split(" ");
		return names[0];
	}
	
	private static String lastName(String name) {
		String[] names = name.split(" ");
		return names[1];
	}

}
