package eu.geosmartcity.hub.utils;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import org.apache.log4j.Logger;

public class Email {
	static final Logger LOGGER = Logger.getLogger(Email.class);
	
	public static void sendEmail(String from, String to, String cc, String subject, String message) {
		System.setProperty("java.net.preferIPv4Stack", "true"); // necessario per utilizzare IPV4
		Properties props = System.getProperties();
		
		props.put("mail.smtp.host", ProjectPropertiesSolar.loadByName("smtpServer"));
		props.put("mail.smtp.port", ProjectPropertiesSolar.loadByName("smtpPort"));
		
		Session mailSession = Session.getDefaultInstance(props);
		Message simpleMessage = new MimeMessage(mailSession);
		
		InternetAddress fromAddress = null;
		InternetAddress toAddress = null;
		InternetAddress ccAddress = null;
		try {
			fromAddress = new InternetAddress(from);
			toAddress = new InternetAddress(to);
			if (cc != null) {
				ccAddress = new InternetAddress(cc);
			}
		}
		catch (AddressException e) {
			LOGGER.error("Error during mail sending", e);
		}
		
		try {
			LOGGER.info("FROM: " + fromAddress.toString());
			LOGGER.info("TO: " + toAddress.toString());
			simpleMessage.setFrom(fromAddress);
			simpleMessage.setRecipient(RecipientType.TO, toAddress);
			if (ccAddress != null) {
				simpleMessage.setRecipient(RecipientType.CC, ccAddress);
			}
			
			simpleMessage.setSubject(subject);
			//			simpleMessage.setText(message);
			simpleMessage.setContent(message, "text/html; charset=utf-8");
			
			Transport.send(simpleMessage);
			LOGGER.info("Email sent to " + to);
		}
		catch (MessagingException e) {
			LOGGER.error("Error during mail sending", e);
		}
	}
}
