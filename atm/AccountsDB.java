package atm;

/*
 *   Implementing the package Serializable allows an object to be serialized and written into a file 
 * 	 and then it can later be deserialized to be read.
 */

import java.io.Serializable;
import java.util.HashMap; // imports the hashmap from the interface map
import java.util.Set; // a collection that contains no duplicate elements

public class AccountsDB implements Serializable {
	private static final long serialVersionUID = 1L;
	// All the class attributes are declared
	private static final int ADMINCODE = 123;
	private HashMap<Integer,Account> accounts;
	
	// CONSTRUCTOR
	public AccountsDB() {
		accounts = new HashMap<Integer,Account>();
	}
	
	// CUSTOM METHODS
	/* 
	 * The addAccount(Account account) method adds an account to the database, if the accountNo has a duplicate, it will throw an
	 * invalid account exception. If the balance of account is 0, it will throw an InvalidBalanceException. Otherwise,
	 * the account data entered by the admin will be put into the accounts HashMap.
	 */
	
	public void addAccount(Account account) throws InvalidAccountException, InvalidBalanceException {
		if (containsAccount(account.getAccountNo())) {
			throw new InvalidAccountException();
		}
		else if (account.getBalance() < 0) {
			throw new InvalidBalanceException();
		}
		else {
			accounts.put(account.getAccountNo(), account);
		}
	}
	
	/* 
	 * The remove account method removes an account and will throw an InvalidAccountException if the account does not exist.
	 */
	
	public void removeAccount(Account account) throws InvalidAccountException {
		if (!(containsAccount(account.getAccountNo()))) {
			throw new InvalidAccountException();
		}
		else {
			accounts.remove(account.getAccountNo());
		}
	}
	
	/*
	 * This method updates the current values of the different attributes of the accounts.
	 * It will throw an error if the target account does not exist.
	 */
	
	public void updateAccount(int targetNo, Account account) throws InvalidAccountException {
		if (!(containsAccount(targetNo))) {
			throw new InvalidAccountException();
		}
		else if (targetNo == account.getAccountNo()) {
			Account updatedAccount = accounts.get(targetNo);
			updatedAccount.setData(account.getPassword(), account.getAccountName(), account.getBalance());
		}
		else {
			accounts.remove(targetNo);
			accounts.put(account.getAccountNo(), account);
		}
	}
	
	// Checks if the account exist. Returns true if an account exist otherwise, return false.
	public boolean containsAccount(int accountNo) {
		return accounts.containsKey(accountNo) ? true : false;
	}
	
	// Get the accountNo of the account.
	public Account getAccount(int accountNo) throws InvalidAccountException {
		if (!(containsAccount(accountNo))) {
			throw new InvalidAccountException();
		}
		else {
			return accounts.get(accountNo);
		}
	}
	
	// Creates the Admin and its passcode.
	public void createAdmin() {
		Account adminAccount = new Account(ADMINCODE, String.valueOf(ADMINCODE), "admin");
		accounts.put(adminAccount.getAccountNo(), adminAccount);
	}
	
	// Returns the admincode if the currennt account is the admin
	public boolean isAdmin(Account account) {
		return account.getAccountNo() == ADMINCODE;
	}
	
	// Returns the admincode
	public static int getAdminCode() {
		return ADMINCODE;
	}
	
	// Retrieves the data of the accounts
	public HashMap<Integer, Account> getAccountsDB(){
		return accounts;
	}
	
	/* 
	 * Retrieves the data. The content of the keySet of the accounts are assigned into a new set named Keys.
	 * The keySet along with the account attribute no. is assigned to a 2D object named data.
	 */
	
	public Object[][] getAccountsTable(){
		Set<Integer> keys = accounts.keySet();
		Object[][] data = new Object[keys.size()-1][Account.getAttributeNo()];
		int i = 0;
		for (Integer key : keys) {
			if (key == ADMINCODE) {
				continue;
			}
			Account account = accounts.get(key);
			data[i] = account.getData(); // retrieves the corresponding data that matches with the given key.
			i++;
		}
		return data;
	}
	
	/* 
	 *  The withdraw method performs the Withdraw in the interface. It will throw an InvalidAccountException whenever
	 * 	the accountNo does not match. It will also throw an InvalidBalanceExceptiion if the balance of the user
	 *  is less than the amount to be withdrawn. The user cannot also withdraw a negative amount.
	 */
	
	public void withdraw(int accountNo, double amount) throws InvalidAccountException, InvalidBalanceException {
		if (!(accounts.containsKey(accountNo))) {
			throw new InvalidAccountException();
		}
		else if (amount < 0 || accounts.get(accountNo).getBalance() < amount) {
			throw new InvalidBalanceException();
		}
		else {
			accounts.get(accountNo).withdraw(amount);
		}
	}
	
	/* 
	 * The deposit method performs the deposit in the interface. It will throw an InvalidAccountException whenever
	 * 	the accountNo does not match. An InvalidBalancexception will be thrown if the amount to be deposited is negative.
	 */
	
	public void deposit(int accountNo, double amount) throws InvalidAccountException, InvalidBalanceException {
		if (!(accounts.containsKey(accountNo))) {
			throw new InvalidAccountException();
		}
		else if (amount < 0) {
			throw new InvalidBalanceException();
		}
		else {
			accounts.get(accountNo).deposit(amount);
		}
	}
	
	/* 
	 * The transfer method will throw an InvalidAccountException if the sourceNo or the target account does not exist.
	 * It will also throw an InvalidBalanceException if the amount to be transferred is greater than the current balance
	 * of the source account or if the amount to be sent is negative.
	 */
	
	public void transfer(int sourceNo, int destNo, double amount) throws InvalidAccountException, InvalidBalanceException {
		if (!(accounts.containsKey(sourceNo) && accounts.containsKey(destNo)) || sourceNo == destNo) {
			throw new InvalidAccountException();
		}
		else if (amount < 0 || accounts.get(sourceNo).getBalance() < amount) {
			throw new InvalidBalanceException();
		}
		else {
			accounts.get(sourceNo).withdraw(amount);
			accounts.get(destNo).deposit(amount);
		}
	}
	
	/*
	 * The changePassword method throws an InvalidAccountException if the account no does not correspond with the current password.
	 */
	
	public void changePassword(int accountNo, char[] password) throws InvalidAccountException {
		if (!(accounts.containsKey(accountNo))) {
			throw new InvalidAccountException();
		}
		else {
			accounts.get(accountNo).setPassword(String.valueOf(password));
		}
	}
	
	/*
	 * This method validates if the password of the account and the password inputted in the passField match. If it matches, it will
	 * return true.
	 */
	
	public static boolean passwordChecker(char[] password, char[] passField) {
		if (password.length != passField.length) {
			return false;
		}
		
		for (int i = 0; i < password.length; ++i) {
			if (password[i] != passField[i]) {
				return false;
			}
		}
		
		return true;
	}
}
