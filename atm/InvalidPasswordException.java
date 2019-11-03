package atm;

public class InvalidPasswordException extends Exception {
	private static final long serialVersionUID = 1L;

	@Override
	public String getMessage() {
		return "InvalidPasswordException:\n";
	}
}