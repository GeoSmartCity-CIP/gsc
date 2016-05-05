package eu.geosmartcity.test.wps;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

/**
 * @author A2SB0132
 */
public class HttpProxyServlet extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8532212401146879671L;
	
	private static final Logger logger = Logger.getLogger(HttpProxyServlet.class);
	
	/** The host. */
	private String host;
	
	/** The proxy host. */
	private String proxyHost;
	
	/** The proxy port. */
	private String proxyPort;
	
	/** The rewrite host. */
	private String rewriteHost;
	
	/** The encoding. */
	private String encoding;
	
	/** The uri. */
	private String uri;
	
	/**
	 * Instantiates a new http proxy servlet.
	 */
	public HttpProxyServlet() {
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.servlet.GenericServlet#init()
	 */
	public void init() throws ServletException {
		host = getInitParameter("host");
		if (host == null) {
			logger.error("HttpProxyServlet needs a host in the parameter.");
			throw new ServletException("HttpProxyServlet needs a host in the parameter.");
		}
		else {
			proxyHost = getInitParameter("proxyHost");
			proxyPort = getInitParameter("proxyPort");
			rewriteHost = getInitParameter("rewriteHost");
			encoding = getInitParameter("encoding");
			uri = getInitParameter("uri");
			return;
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse)
	 */
	public void doGet(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse)
			throws ServletException, IOException {
		String s = httpservletrequest.getQueryString();
		String encodingParams = null;
		//Se è passato in richiesto un encoding
		if (s != null && s.indexOf("encodingCTW") > 0) {
			String sapp = s.trim();
			sapp = sapp.substring(sapp.indexOf("encodingCTW") + 12, sapp.length());
			if (sapp.indexOf("&") > 0) {
				sapp = sapp.substring(0, sapp.indexOf("&"));
			}
			encodingParams = sapp;
		}
		if (encodingParams == null) {
			encodingParams = encoding;
		}
		String s1 = host;
		Object obj = null;
		if (uri != null) {
			uri = normalizeUri(uri);
			String s2 = httpservletrequest.getRequestURI();
			if (s2 != null) {
				if (!s2.startsWith(uri))
					s2 = null;
				else if (uri.length() >= s2.length())
					s2 = null;
				else
					s2 = s2.substring(uri.length());
				if (s2 != null)
					s1 = addUri(s1, s2);
			}
		}
		s1 = addQuery(s1, s);
		Hashtable hashtable = new Hashtable();
		getHeaders(httpservletrequest, hashtable);
		if ("true".equals(rewriteHost))
			hashtable.put("Host", getHostInfo(host));
		GetPost getpost = new GetPost();
		String s3 = getpost.doAction(httpservletrequest.getMethod().toUpperCase(), s1, null, hashtable, -1, proxyHost,
				proxyPort, encodingParams, httpservletrequest.getInputStream(), httpservletresponse);
		if (s3 != null) {
			int i = getpost.getErrorCode();
			if (i > 0)
				httpservletresponse.sendError(i, s3);
			else
				httpservletresponse.sendError(503, s3);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse)
	 */
	public void doPost(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse)
			throws ServletException, IOException {
		String s = httpservletrequest.getQueryString();
		String s1 = host;
		Object obj = null;
		if (uri != null) {
			uri = normalizeUri(uri);
			String s2 = httpservletrequest.getRequestURI();
			if (s2 != null) {
				if (!s2.startsWith(uri))
					s2 = null;
				else if (uri.length() >= s2.length())
					s2 = null;
				else
					s2 = s2.substring(uri.length());
				if (s2 != null)
					s1 = addUri(s1, s2);
			}
		}
		s1 = addQuery(s1, s);
		Hashtable hashtable = new Hashtable();
		Hashtable hashtable1 = new Hashtable();
		Enumeration enumeration = httpservletrequest.getParameterNames();
		do {
			if (!enumeration.hasMoreElements())
				break;
			String s3 = (String) enumeration.nextElement();
			String as[] = httpservletrequest.getParameterValues(s3);
			if (as != null && as.length > 0) {
				int i = 0;
				while (i < as.length) {
					hashtable1
							.put((new StringBuilder()).append(s3).append("<").append(i).append(">").toString(), as[i]);
					i++;
				}
			}
		}
		while (true);
		getHeaders(httpservletrequest, hashtable);
		if ("true".equals(rewriteHost))
			hashtable.put("Host", getHostInfo(host));
		GetPost getpost = new GetPost();
		String s4 = getpost.doAction(httpservletrequest.getMethod().toUpperCase(), s1, hashtable1, hashtable, -1,
				proxyHost, proxyPort, encoding, httpservletrequest.getInputStream(), httpservletresponse);
		if (s4 != null) {
			int j = getpost.getErrorCode();
			if (j > 0)
				httpservletresponse.sendError(j, s4);
			else
				httpservletresponse.sendError(503, s4);
		}
	}
	
	/**
	 * Gets the headers.
	 *
	 * @param httpservletrequest
	 *            the httpservletrequest
	 * @param hashtable
	 *            the hashtable
	 * @return the headers
	 */
	private void getHeaders(HttpServletRequest httpservletrequest, Hashtable hashtable) {
		String s;
		for (Enumeration enumeration = httpservletrequest.getHeaderNames(); enumeration.hasMoreElements(); hashtable
				.put(s, httpservletrequest.getHeader(s)))
			s = (String) enumeration.nextElement();
		
	}
	
	/**
	 * Gets the host info.
	 *
	 * @param s
	 *            the s
	 * @return the host info
	 */
	private String getHostInfo(String s) {
		String s1 = s;
		int i = s1.indexOf("://");
		if (i > 0)
			s1 = s1.substring(i + 3);
		i = s1.indexOf("/");
		if (i > 0)
			s1 = s1.substring(0, i);
		i = s1.indexOf("?");
		if (i > 0)
			s1 = s1.substring(0, i);
		i = s1.indexOf("#");
		if (i > 0)
			s1 = s1.substring(0, i);
		i = s1.indexOf(";");
		if (i > 0)
			s1 = s1.substring(0, i);
		return s1;
	}
	
	/**
	 * Adds the query.
	 *
	 * @param s
	 *            the s
	 * @param s1
	 *            the s1
	 * @return the string
	 */
	private String addQuery(String s, String s1) {
		if (s1 != null) {
			if (s.indexOf("?") < 0)
				return (new StringBuilder()).append(s).append("?").append(s1).toString();
			else
				return (new StringBuilder()).append(s).append("&").append(s1).toString();
		}
		else {
			return s;
		}
	}
	
	/**
	 * Adds the uri.
	 *
	 * @param s
	 *            the s
	 * @param s1
	 *            the s1
	 * @return the string
	 */
	private String addUri(String s, String s1) {
		if (s1 == null)
			return s;
		if (s1.length() == 0)
			return s;
		if (s1.equals("/"))
			return s;
		String s2 = s1;
		if (s2.charAt(0) == '/')
			s2 = s2.substring(1);
		if (s2.length() == 0)
			return s;
		String s3 = "";
		String s4 = s;
		int i = s4.indexOf("?");
		if (i > 0) {
			if (i < s4.length() - 1)
				s3 = s4.substring(i + 1);
			s4 = s4.substring(0, i);
		}
		if (!s4.endsWith("/"))
			s4 = (new StringBuilder()).append(s4).append("/").toString();
		s4 = (new StringBuilder()).append(s4).append(s2).toString();
		if (s3.length() > 0)
			s4 = (new StringBuilder()).append(s4).append("?").append(s3).toString();
		return s4;
	}
	
	/**
	 * Normalize uri.
	 *
	 * @param s
	 *            the s
	 * @return the string
	 */
	private String normalizeUri(String s) {
		int i = s.indexOf("*");
		if (i < 0)
			return s;
		if (i == 0)
			return "/";
		else
			return s.substring(0, i);
	}
}
