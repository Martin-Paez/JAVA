package exceptions;

public class FileFormatException extends Exception{

	private static final long serialVersionUID = -1076073809104531122L;

	public FileFormatException(String msg) {
		super(msg);
	}
}
