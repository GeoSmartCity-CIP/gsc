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
	
	/**
	 * Returns an error as json in the following format: {"error":{"errorCode":"???","errorDescription":"???"}}
	 * 
	 * @return
	 */
	public String returnErrorString() {
		return "{\"error\":{\"errorCode\":\""+this.getErrorCode()+"\",\"errorDescription\":\""+this.getErrorMessage()+"\"}}";
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
