package atm;

public class InvalidBalanceException extends Exception {
	private static final long serialVersionUID = 1L;

	@Override
	public String getMessage() {
		return "InvalidBalanceException:\n";
	}
}