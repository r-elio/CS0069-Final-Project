package atm;

public class InvalidAccountException extends Exception {
	private static final long serialVersionUID = 1L;

	@Override
	public String getMessage() {
		return "InvalidAccountException:\n";
	}
}