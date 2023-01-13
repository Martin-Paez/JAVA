package exceptions;

public class WrongDataException extends Exception {

	private static final long serialVersionUID = 5349748107566004697L;

	public WrongDataException(String msg) {
		super(msg);
	}
}
