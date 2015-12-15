package it.sinergis.routingpreferences.exception;

import it.sinergis.routingpreferences.common.PropertyReader;

public class RPException extends Exception {

	/** Error messages reader. */
	private PropertyReader pr;	
	
	/** Error code. */
	private String errorCode;
	
	/** Error message. */
	private String errorMessage;
	
	public RPException(String errorCode) {
		super(errorCode);
		pr = new PropertyReader("error_messages.properties");
		this.setErrorCode(errorCode);
		this.setErrorMessage(pr.getValue(errorCode));	
	}
	
	public String returnErrorString() {
		return "An exception has occurred: "+this.getErrorMessage() +"("+this.getErrorCode()+")";
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	
}
