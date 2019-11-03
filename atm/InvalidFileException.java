package atm;

public class InvalidFileException extends Exception {
	private static final long serialVersionUID = 1L;

	@Override
	public String getMessage() {
		return "InvalidFileException:\n";
	}
}