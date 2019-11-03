package atm;

import java.io.Serializable;

/* 
 * Implementing the package Serializable allows an object to be serialized and written into a file 
 * and then it can later be deserialized to be read. 
 */

public class Account implements Serializable {
	
	/*
	 * All the variables are declared. Notice that some are all written in capital letters, its because it is a FINAL variable.
	 */

	private static final long serialVersionUID = 1L;
	private final static String ATTRIBUTES[] = { "Account Number", "Password", "Name", "Balance" };
	private final static int ATTRIBUTENO = ATTRIBUTES.length;
	private int accountNo;
	private String password;
	private String accountName;
	private double balance;
	
	// Constructors
	public Account() {
		accountNo = 0;
		password = null;
		accountName = null;
		balance = 0.0;
	}
	
	public Account(int accountNo, String password) {
		this.accountNo = accountNo;
		this.password = password;
		accountName = null;
		balance = 0.0;
	}
	
	public Account(int accountNo, String password, String accountName) {
		this.accountNo = accountNo;
		this.password = password;
		this.accountName = accountName;
		balance = 0.0;
	}
	
	public Account(int accountNo, String password, String accountName, double balance) {
		this.accountNo = accountNo;
		this.password = password;
		this.accountName = accountName;
		this.balance = balance;
	}
	
	// Mutators
	public void setAccountNo(int accountNo) {
		this.accountNo = accountNo;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	
	public void setBalance(double balance) {
		this.balance = balance;
	}
	
	// Accessors
	public int getAccountNo() {
		return accountNo;
	}
	
	public String getPassword() {
		return password;
	}
	
	public String getAccountName() {
		return accountName;
	}
	
	public double getBalance() {
		return balance;
	}
	
	// Custom Methods
	
	// Sets the data of the account
	public void setData(String password, String accountName, double balance) {
		this.password = password;
		this.accountName = accountName;
		this.balance = balance;
	}
	
	// This method retrieves the data and is assigned to an Object[] array.
	public Object[] getData() {
		Object[] obj = {accountNo,password,accountName,balance};
		return obj;
	}
	
	// Returns all the attributes
	public static String[] getAttributes() {
		return ATTRIBUTES;
	}
	
	// Returns all the attribute no.
	public static int getAttributeNo() {
		return ATTRIBUTENO;
	}
	
	// Withdraws a certain amount from the account
	public void withdraw(double amount) {
		balance -= amount;
	}
	
	// Deposits an amount to the account
	public void deposit(double amount) {
		balance += amount;
	}
}
