/**
 * 
 */
package it.sinergis.gsc.exceptions;

/**
 * Classe che modella una generica eccezione lanciata da un servizio.
 * 
 * @author A2MD0149
 *
 */
public class GSCServiceException extends Exception {

	/** Seriale di classe. */
	private static final long serialVersionUID = 1L;

	private String errorDescription;

	public GSCServiceException(String message, String description) {
		super(message);
		this.errorDescription = description;
	}

	public String getErrorDescription() {
		return errorDescription;
	}

}
