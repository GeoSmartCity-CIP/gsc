package eu.geosmartcity.hub.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;

public class Httprequest {
	private static final Logger LOGGER = Logger.getLogger(Httprequest.class);
	
	/**
	 * @param url
	 * @param user
	 * @param pwd
	 * @return
	 */
	public String getAuth(String url, String user, String pwd) {
		try {
			
			String result = sendGet(url, user, pwd);
			return result;
		}
		catch (IOException e) {
			LOGGER.error("Errore in connessione verso " + url, e);
		}
		return null;
	}
	
	/**
	 * @param url
	 * @return
	 */
	public String get(String url) {
		try {
			
			String result = sendGet(url);
			return result;
		}
		catch (IOException e) {
			LOGGER.error("Errore in connessione verso " + url, e);
		}
		return null;
	}
	
	/**
	 * @param url
	 * @param payload
	 * @param contentType
	 * @return
	 */
	public String postPublishPostgisTable(String url, String workspaceGeo, String dataStoreGeo, String payload,
			String contentType) {
		//curl -v -u admin:geoserver -X POST -H "Content-type: text/xml" -d "<featureType><name>lakes</name></featureType>" http://localhost:8080/geoserver/rest/workspaces/opengeo/datastores/pgstore/featuretypes
		try {
			String completeUrl = url + "rest/workspaces/" + workspaceGeo + "/datastores/" + dataStoreGeo
					+ "/featuretypes";
			String result = sendPostAuth(completeUrl, payload, contentType, ReadFromConfig.loadByName("userGeoserver"),
					ReadFromConfig.loadByName("pwdGeoserver"));
			return result;
		}
		catch (IOException e) {
			LOGGER.error("Error in connection " + url, e);
		}
		return null;
	}
	
	/**
	 * @param url
	 * @param payload
	 * @param contentType
	 * @return
	 */
	public String putReloadBbox(String url, String workspaceGeo, String dataStoreGeo, String featureType, String payload,
			String contentType) {
		//curl -v -u admin:geoserver -X POST -H "Content-type: text/xml" -d "<featureType><name>lakes</name></featureType>" http://localhost:8080/geoserver/rest/workspaces/opengeo/datastores/pgstore/featuretypes?recalculate=nativebbox,latlonbbox
		try {
			String completeUrl = url + "rest/workspaces/" + workspaceGeo + "/datastores/" + dataStoreGeo
					+ "/featuretypes/"+featureType+"?recalculate=nativebbox,latlonbbox";
			String result = sendPutAuth(completeUrl, payload, contentType, ReadFromConfig.loadByName("userGeoserver"),
					ReadFromConfig.loadByName("pwdGeoserver"));
			return result;
		}
		catch (IOException e) {
			LOGGER.error("Error in connection " + url, e);
		}
		return null;
	}
	
	/**
	 * @param url
	 * @param user
	 * @param pwd
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private String sendGet(String url, String user, String pwd) throws UnsupportedEncodingException {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		String authStr = user + ":" + pwd;
		String authEncoded = Base64.encodeBase64String(authStr.getBytes());
		String result = null;
		HttpGet hget = new HttpGet(url);
		hget.setHeader("Authorization", "Basic " + authEncoded);
		
		HttpResponse response = null;
		try {
			response = httpclient.execute(hget);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream instream = entity.getContent();
				result = Functions.convertStreamToString(instream);
				return result;
			}
		}
		catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			LOGGER.error("Errore in HttpRequest get per l'url " + url, e);
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			LOGGER.error(e);
		}
		finally {
			//httpclient.getConnectionManager().shutdown();
		}
		
		return result;
	}
	
	private String sendGet(String url) throws UnsupportedEncodingException {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		String result = null;
		HttpGet hget = new HttpGet(url);
		
		HttpResponse response = null;
		try {
			response = httpclient.execute(hget);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream instream = entity.getContent();
				result = Functions.convertStreamToString(instream);
				return result;
			}
		}
		catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			LOGGER.error("Errore in HttpRequest get per l'url " + url, e);
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			LOGGER.error(e);
		}
		finally {
			//httpclient.getConnectionManager().shutdown();
		}
		
		return result;
	}
	
	private String sendPostAuth(String url, String payload, String contentType, String user, String pwd)
			throws UnsupportedEncodingException {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		String authStr = user + ":" + pwd;
		String authEncoded = Base64.encodeBase64String(authStr.getBytes());
		String result = null;
		HttpPost hpost = new HttpPost(url);
		hpost.setHeader("Authorization", "Basic " + authEncoded);
		hpost.setHeader("Content-Type", contentType);
		
		StringEntity entity = new StringEntity(payload);
		LOGGER.info(payload);
		hpost.setEntity(entity);
		
		HttpResponse response = null;
		try {
			response = httpclient.execute(hpost);
			HttpEntity entityResponse = response.getEntity();
			if (entityResponse != null) {
				InputStream instream = entityResponse.getContent();
				result = Functions.convertStreamToString(instream);
				return result;
			}
		}
		catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			LOGGER.error("Errore in HttpRequest get per l'url " + url, e);
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			LOGGER.error(e);
		}
		
		return result;
	}
	
	private String sendPutAuth(String url, String payload, String contentType, String user, String pwd)
			throws UnsupportedEncodingException {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		String authStr = user + ":" + pwd;
		String authEncoded = Base64.encodeBase64String(authStr.getBytes());
		String result = null;
		HttpPut hput = new HttpPut(url);
		hput.setHeader("Authorization", "Basic " + authEncoded);
		hput.setHeader("Content-Type", contentType);
		
		StringEntity entity = new StringEntity(payload);
		LOGGER.info(payload);
		hput.setEntity(entity);
		
		HttpResponse response = null;
		try {
			response = httpclient.execute(hput);
			LOGGER.info("response reload bbox "+response);
			HttpEntity entityResponse = response.getEntity();
			if (entityResponse != null) {
				InputStream instream = entityResponse.getContent();
				result = Functions.convertStreamToString(instream);
				return result;
			}
		}
		catch (ClientProtocolException e) {
			LOGGER.error("Errore in HttpRequest put per l'url " + url, e);
		}
		catch (IOException e) {
			LOGGER.error(e);
		}
		
		return result;
	}
	
	public String getCapa(String url) {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpGet hget = new HttpGet(url);
		HttpResponse response = null;
		String result = null;
		try {
			response = httpclient.execute(hget);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream instream = entity.getContent();
				result = Functions.convertStreamToString(instream);
			}
		}
		catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			LOGGER.error("Errore in HttpRequest get per l'url " + url, e);
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			LOGGER.error(e);
		}
		return result;
	}
	
}
