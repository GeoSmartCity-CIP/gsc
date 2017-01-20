package eu.geosmartcity.test.wps;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

/**
 * Classe che viene utilizzata dal viewer che fa da proxy
 * 
 * @author A2SB0132
 */
public class GetPost {
	
	private static final Logger logger = Logger.getLogger(GetPost.class);
	
	/** The error code. */
	private int errorCode;
	
	/**
	 * Instantiates a new gets the post.
	 */
	public GetPost() {
		errorCode = 0;
	}
	
	/**
	 * Sets the error code.
	 * 
	 * @param i
	 *            the new error code
	 */
	public void setErrorCode(int i) {
		errorCode = i;
	}
	
	/**
	 * Gets the error code.
	 * 
	 * @return the error code
	 */
	public int getErrorCode() {
		return errorCode;
	}
	
	/**
	 * Do action.
	 * 
	 * @param s
	 *            the s
	 * @param s1
	 *            the s1
	 * @param hashtable
	 *            the hashtable
	 * @param hashtable1
	 *            the hashtable1
	 * @param i
	 *            the i
	 * @param s2
	 *            the s2
	 * @param s3
	 *            the s3
	 * @param s4
	 *            the s4
	 * @param servletinputstream
	 *            the servletinputstream
	 * @param httpservletresponse
	 *            the httpservletresponse
	 * @return the string
	 */
	public String doAction(String s, String s1, Hashtable hashtable, Hashtable hashtable1, int i, String s2, String s3,
			String s4, ServletInputStream servletinputstream, HttpServletResponse httpservletresponse) {
		if (i > 0) {
			Properties properties = System.getProperties();
			properties.put("sun.net.client.defaultConnectTimeout", (new StringBuilder()).append("").append(i)
					.toString());
			properties.put("sun.net.client.defaultReadTimeout", (new StringBuilder()).append("").append(i).toString());
			System.setProperties(properties);
		}
		if (s2 != null && s3 != null) {
			Properties properties1 = System.getProperties();
			properties1.put("proxySet", "true");
			properties1.put("proxyHost", s2);
			properties1.put("proxyPort", s3);
			System.setProperties(properties1);
		}
		if ("GET".equals(s.toUpperCase()))
			if (s1.toUpperCase().startsWith("HTTPS://"))
				return doGetSSL(s1, hashtable1, s4, httpservletresponse);
			else
				return doGet(s1, hashtable1, s4, httpservletresponse);
		if (s1.toUpperCase().startsWith("HTTPS://"))
			return doPostSSL(s1, hashtable, hashtable1, s4, servletinputstream, httpservletresponse);
		else
			return doPost(s1, hashtable, hashtable1, s4, servletinputstream, httpservletresponse);
	}
	
	/**
	 * Do post.
	 * 
	 * @param s
	 *            the s
	 * @param hashtable
	 *            the hashtable
	 * @param hashtable1
	 *            the hashtable1
	 * @param s1
	 *            the s1
	 * @param servletinputstream
	 *            the servletinputstream
	 * @param httpservletresponse
	 *            the httpservletresponse
	 * @return the string
	 */
	private String doPost(String s, Hashtable hashtable, Hashtable hashtable1, String s1,
			ServletInputStream servletinputstream, HttpServletResponse httpservletresponse) {
		URLConnection urlconnection;
		urlconnection = null;
		String s2 = "";
		URL url = null;
		if (hashtable != null)
			s2 = getParamsFromHash(hashtable);
		try {
			url = new URL(s);
			urlconnection = url.openConnection();
			urlconnection.setDoOutput(true);
			urlconnection.setDoInput(true);
			urlconnection.setUseCaches(false);
			urlconnection.setRequestProperty("content-type", "application/xml");
			if (hashtable1 != null) {
				String s3;
				for (Enumeration enumeration = hashtable1.keys(); enumeration.hasMoreElements(); urlconnection
						.setRequestProperty(s3, (String) hashtable1.get(s3)))
					s3 = (String) enumeration.nextElement();
			}
			
			if (servletinputstream != null) {
				String xmlreq = this.convertStreamToString(servletinputstream);
				if (xmlreq != null && !xmlreq.equals("")) {
					PrintWriter printwriter = new PrintWriter(urlconnection.getOutputStream());
					printwriter.write(xmlreq);
					printwriter.close();
				}
			}
			
			PrintWriter printwriter = new PrintWriter(urlconnection.getOutputStream());
			printwriter.print(s2);
			printwriter.close();
			
			String s5;
			if (urlconnection instanceof HttpURLConnection) {
				HttpURLConnection httpurlconnection = (HttpURLConnection) urlconnection;
				setErrorCode(httpurlconnection.getResponseCode());
			}
			String s4 = urlconnection.getContentType();
			if (s4 != null && s1 != null)
				s4 = rewriteEncoding(s4, s1);
			if (s4 != null)
				httpservletresponse.setContentType(s4);
			s5 = urlconnection.getContentEncoding();
			if (s5 == null)
				s5 = "";
			String s6 = urlconnection.getHeaderField("Content-Disposition");
			if (s6 != null && !s6.equals("")) {
				httpservletresponse.setHeader("Content-Disposition", s6);
			}
			Object obj2;
			BufferedOutputStream bufferedoutputstream;
			try {
				if (s5.indexOf("gzip") >= 0)
					obj2 = new GZIPInputStream(urlconnection.getInputStream());
				else
					obj2 = new BufferedInputStream(urlconnection.getInputStream());
				bufferedoutputstream = new BufferedOutputStream(httpservletresponse.getOutputStream());
				int i;
				while ((i = ((InputStream) (obj2)).read()) >= 0)
					bufferedoutputstream.write(i);
			}
			catch (Exception exception2) {
				return getMessage(s, exception2);
			}
			try {
				if (obj2 != null)
					((InputStream) (obj2)).close();
				if (bufferedoutputstream != null) {
					bufferedoutputstream.flush();
					bufferedoutputstream.close();
				}
			}
			catch (Exception exception1) {
				return getMessage(s, exception1);
			}
		}
		catch (Exception exception) {
			return getMessage(s, exception);
		}
		
		return null;
	}
	
	/**
	 * Do post ssl.
	 * 
	 * @param s
	 *            the s
	 * @param hashtable
	 *            the hashtable
	 * @param hashtable1
	 *            the hashtable1
	 * @param s1
	 *            the s1
	 * @param servletinputstream
	 *            the servletinputstream
	 * @param httpservletresponse
	 *            the httpservletresponse
	 * @return the string
	 */
	private String doPostSSL(String s, Hashtable hashtable, Hashtable hashtable1, String s1,
			ServletInputStream servletinputstream, HttpServletResponse httpservletresponse) {
		HttpsURLConnection httpsurlconnection;
		httpsurlconnection = null;
		String s2 = "";
		if (hashtable != null)
			s2 = getParamsFromHash(hashtable);
		try {
			URL url = new URL(s);
			httpsurlconnection = (HttpsURLConnection) url.openConnection();
			httpsurlconnection.setDoOutput(true);
			httpsurlconnection.setDoInput(true);
			httpsurlconnection.setUseCaches(false);
			httpsurlconnection.setRequestProperty("content-type", "application/xml");
			/*
			 * if (hashtable1 != null) { String s3; for (Enumeration enumeration = hashtable1.keys(); enumeration
			 * .hasMoreElements(); httpsurlconnection .setRequestProperty(s3, (String) hashtable1.get(s3))) s3 =
			 * (String) enumeration.nextElement(); }
			 */
			PrintWriter printwriter = new PrintWriter(httpsurlconnection.getOutputStream());
			printwriter.print(s2);
			int j;
			while ((j = servletinputstream.read()) != -1)
				printwriter.write(j);
			printwriter.close();
			
			String s5;
			if (httpsurlconnection instanceof HttpURLConnection) {
				HttpsURLConnection httpsurlconnection1 = httpsurlconnection;
				setErrorCode(httpsurlconnection1.getResponseCode());
			}
			String s4 = httpsurlconnection.getContentType();
			if (s4 != null && s1 != null)
				s4 = rewriteEncoding(s4, s1);
			if (s4 != null)
				httpservletresponse.setContentType(s4);
			s5 = httpsurlconnection.getContentEncoding();
			if (s5 == null)
				s5 = "";
			Object obj2;
			BufferedOutputStream bufferedoutputstream;
			try {
				if (s5.indexOf("gzip") >= 0)
					obj2 = new GZIPInputStream(httpsurlconnection.getInputStream());
				else
					obj2 = new BufferedInputStream(httpsurlconnection.getInputStream());
				bufferedoutputstream = new BufferedOutputStream(httpservletresponse.getOutputStream());
				int i;
				while ((i = ((InputStream) (obj2)).read()) >= 0)
					bufferedoutputstream.write(i);
			}
			catch (Exception exception2) {
				return getMessage(s, exception2);
			}
			try {
				if (obj2 != null)
					((InputStream) (obj2)).close();
				if (bufferedoutputstream != null) {
					bufferedoutputstream.flush();
					bufferedoutputstream.close();
				}
			}
			catch (Exception exception1) {
				return getMessage(s, exception1);
			}
		}
		catch (Exception exception) {
			return getMessage(s, exception);
		}
		return null;
	}
	
	/**
	 * Do get.
	 * 
	 * @param s
	 *            the s
	 * @param hashtable
	 *            the hashtable
	 * @param s1
	 *            the s1
	 * @param httpservletresponse
	 *            the httpservletresponse
	 * @return the string
	 */
	private String doGet(String s, Hashtable hashtable, String s1, HttpServletResponse httpservletresponse) {
		URLConnection urlconnection;
		urlconnection = null;
		try {
			URL url = new URL(s);
			urlconnection = url.openConnection();
			urlconnection.setDoInput(true);
			urlconnection.setUseCaches(false);
			/*
			 * if (hashtable != null) { String s3; for (Enumeration enumeration = hashtable.keys(); enumeration
			 * .hasMoreElements(); urlconnection.setRequestProperty( s3, (String) hashtable.get(s3))) s3 = (String)
			 * enumeration.nextElement(); }
			 */
			
			String s5;
			if (urlconnection instanceof HttpURLConnection) {
				HttpURLConnection httpurlconnection = (HttpURLConnection) urlconnection;
				setErrorCode(httpurlconnection.getResponseCode());
			}
			String s4 = urlconnection.getContentType();
			if (s4 != null && s1 != null)
				s4 = rewriteEncoding(s4, s1);
			if (s4 != null)
				httpservletresponse.setContentType(s4);
			else
				s4 = "";
			s5 = urlconnection.getContentEncoding();
			if (s5 == null)
				s5 = "";
			String s6 = urlconnection.getHeaderField("Content-Disposition");
			if (s6 != null && !s6.equals("")) {
				httpservletresponse.setHeader("Content-Disposition", s6);
			}
			Object obj2;
			BufferedOutputStream bufferedoutputstream;
			try {
				if (s5.indexOf("gzip") >= 0)
					obj2 = new GZIPInputStream(urlconnection.getInputStream());
				else
					obj2 = new BufferedInputStream(urlconnection.getInputStream());
				bufferedoutputstream = new BufferedOutputStream(httpservletresponse.getOutputStream());
				int i;
				while ((i = ((InputStream) (obj2)).read()) >= 0)
					bufferedoutputstream.write(i);
			}
			catch (Exception exception2) {
				return getMessage(s, exception2);
			}
			try {
				if (obj2 != null)
					((InputStream) (obj2)).close();
				if (bufferedoutputstream != null) {
					bufferedoutputstream.flush();
					bufferedoutputstream.close();
				}
			}
			catch (Exception exception1) {
				return getMessage(s, exception1);
			}
		}
		catch (Exception exception) {
			return getMessage(s, exception);
		}
		return null;
	}
	
	/**
	 * Do get ssl.
	 * 
	 * @param s
	 *            the s
	 * @param hashtable
	 *            the hashtable
	 * @param s1
	 *            the s1
	 * @param httpservletresponse
	 *            the httpservletresponse
	 * @return the string
	 */
	private String doGetSSL(String s, Hashtable hashtable, String s1, HttpServletResponse httpservletresponse) {
		HttpsURLConnection httpsurlconnection;
		httpsurlconnection = null;
		try {
			URL url = new URL(s);
			httpsurlconnection = (HttpsURLConnection) url.openConnection();
			httpsurlconnection.setDoInput(true);
			httpsurlconnection.setUseCaches(false);
			if (hashtable != null) {
				String s3;
				for (Enumeration enumeration = hashtable.keys(); enumeration.hasMoreElements(); httpsurlconnection
						.setRequestProperty(s3, (String) hashtable.get(s3)))
					s3 = (String) enumeration.nextElement();
				
			}
			
			String s5;
			if (httpsurlconnection instanceof HttpURLConnection) {
				HttpsURLConnection httpsurlconnection1 = httpsurlconnection;
				setErrorCode(httpsurlconnection1.getResponseCode());
			}
			String s4 = httpsurlconnection.getContentType();
			if (s4 != null && s1 != null)
				s4 = rewriteEncoding(s4, s1);
			if (s4 != null)
				httpservletresponse.setContentType(s4);
			else
				s4 = "";
			s5 = httpsurlconnection.getContentEncoding();
			if (s5 == null)
				s5 = "";
			Object obj2;
			BufferedOutputStream bufferedoutputstream;
			try {
				if (s5.indexOf("gzip") >= 0)
					obj2 = new GZIPInputStream(httpsurlconnection.getInputStream());
				else
					obj2 = new BufferedInputStream(httpsurlconnection.getInputStream());
				bufferedoutputstream = new BufferedOutputStream(httpservletresponse.getOutputStream());
				int i;
				while ((i = ((InputStream) (obj2)).read()) >= 0)
					bufferedoutputstream.write(i);
			}
			catch (Exception exception2) {
				return getMessage(s, exception2);
			}
			try {
				if (obj2 != null)
					((InputStream) (obj2)).close();
				if (bufferedoutputstream != null) {
					bufferedoutputstream.flush();
					bufferedoutputstream.close();
				}
			}
			catch (Exception exception1) {
				return getMessage(s, exception1);
			}
		}
		catch (Exception exception) {
			return getMessage(s, exception);
		}
		return null;
	}
	
	/**
	 * Gets the message.
	 * 
	 * @param s
	 *            the s
	 * @param exception
	 *            the exception
	 * @return the message
	 */
	protected String getMessage(String s, Exception exception) {
		String s1 = exception.getClass().getName();
		int i = s1.lastIndexOf('.');
		s1 = s1.substring(i + 1);
		StringWriter stringwriter = new StringWriter();
		PrintWriter printwriter = new PrintWriter(stringwriter);
		exception.printStackTrace(printwriter);
		return (new StringBuilder()).append("Request: ").append(s).append("\nException: ").append(s1).append(": ")
				.append(exception.getMessage()).append("\n").append(stringwriter.getBuffer().toString()).toString();
	}
	
	/**
	 * Gets the input stream reader.
	 * 
	 * @param inputstream
	 *            the inputstream
	 * @param s
	 *            the s
	 * @return the input stream reader
	 * @throws UnsupportedEncodingException
	 *             the unsupported encoding exception
	 */
	private InputStreamReader getInputStreamReader(InputStream inputstream, String s)
			throws UnsupportedEncodingException {
		if (s == null)
			return new InputStreamReader(inputstream);
		else
			return new InputStreamReader(inputstream, s);
	}
	
	/**
	 * Rewrite encoding.
	 * 
	 * @param s
	 *            the s
	 * @param s1
	 *            the s1
	 * @return the string
	 */
	private String rewriteEncoding(String s, String s1) {
		if (s.indexOf("charset") < 0)
			return (new StringBuilder()).append(s).append(";charset=").append(s1).toString();
		int i = s.indexOf(";");
		if (i < 0)
			i = s.indexOf("charset");
		return (new StringBuilder()).append(s.substring(0, i)).append(";charset=").append(s1).toString();
	}
	
	/**
	 * Gets the params from hash.
	 * 
	 * @param hashtable
	 *            the hashtable
	 * @return the params from hash
	 */
	private String getParamsFromHash(Hashtable hashtable) {
		String s = "";
		String s1 = "";
		for (Enumeration enumeration = hashtable.keys(); enumeration.hasMoreElements();) {
			if (s.length() > 0)
				s = (new StringBuilder()).append(s).append("&").toString();
			String s2 = (String) enumeration.nextElement();
			String s3 = s2;
			int i = s3.indexOf("<");
			if (i > 0)
				s3 = s3.substring(0, i);
			s = (new StringBuilder()).append(s).append(s3).append("=").toString();
			s = (new StringBuilder()).append(s).append(URLEncoder.encode((String) hashtable.get(s2))).toString();
		}
		
		return s;
	}
	
	/**
	 * Convert stream to string.
	 * 
	 * @param is
	 *            the is
	 * @return the string
	 */
	public String convertStreamToString(InputStream is) {
		/*
		 * To convert the InputStream to String we use the BufferedReader.readLine() method. We iterate until the
		 * BufferedReader return null which means there's no more data to read. Each line will appended to a
		 * StringBuilder and returned as String.
		 */
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		}
		catch (IOException e) {
			logger.error("errore nella lettura dello stream", e);
		}
		finally {
			try {
				is.close();
			}
			catch (IOException e) {
				logger.error("errore nella chiusura dello stream", e);
			}
		}
		
		return sb.toString();
	}
	
}