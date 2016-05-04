package eu.geosmartcity.hub.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.MultiPolygon;

public final class Functions {
	final static Logger LOGGER = Logger.getLogger(Functions.class);
	
	//TODO da mettere statica in util
	public static String convertStreamToString(InputStream is) {
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		}
		catch (IOException e) {
			LOGGER.error(e);
		}
		finally {
			try {
				is.close();
			}
			catch (IOException e) {
				LOGGER.error(e);
			}
		}
		return sb.toString();
	}
	
	public static String addSeparator(String path) {
		if (path.charAt(path.length() - 1) != File.separatorChar) {
			path += File.separator;
		}
		return path;
	}
	
	public static String loadStream(InputStream s) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(s));
		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = br.readLine()) != null)
			sb.append(line).append("\n");
		return sb.toString();
	}
	
	public static String addUrlSection(String url, String section) {
		if (url.substring(url.length() - 1) == "/") {
			if (section.substring(0, 1) == "/") {
				return url.concat(section.substring(1, section.length() - 1));
			}
			return url.concat(section);
		}
		else {
			if (section.substring(0, 1) == "/") {
				return url.concat(section);
			}
			return url + "/" + section;
		}
		
	}
	
	public static String getExtension(String path) {
		return FilenameUtils.getExtension(path);
	}
	
	public static String writeZipShapeOnDisk(InputStream in) throws FileNotFoundException, IOException {
		ZipInputStream zin = new ZipInputStream(in);
		ZipEntry ze = null;
		String nameShape = null;
		String directory = ReadFromConfig.loadByName("hostdirectory");
		while ((ze = zin.getNextEntry()) != null) {
			System.out.println("Unzipping " + ze.getName());
			FileOutputStream fout = new FileOutputStream(directory + ze.getName());
			for (int c = zin.read(); c != -1; c = zin.read()) {
				fout.write(c);
			}
			zin.closeEntry();
			fout.close();
			if (Functions.getExtension(directory + ze.getName()).equals("shp")) {
				nameShape = directory + ze.getName();
			}
			
		}
		zin.close();
		return nameShape;
		
	}
	
	@SuppressWarnings("resource")
	public static String writeFileOnDisk(InputStream in, String shortNameFile) {
		String directory = ReadFromConfig.loadByName("hostdirectory");
		String nameFile = directory + shortNameFile;
		OutputStream outputStream = null;
		try {
			LOGGER.info("inizio scrittura file " + ReadFromConfig.loadByName("hostdirectory") + shortNameFile);
			outputStream = new FileOutputStream(new File(ReadFromConfig.loadByName("hostdirectory") + shortNameFile));
			int read = 0;
			byte[] bytes = new byte[1024];
			
			while ((read = in.read(bytes)) != -1) {
				outputStream.write(bytes, 0, read);
			}
			LOGGER.info("fine scrittura file " + shortNameFile);
		}
		catch (Exception e) {
			LOGGER.error("Problems on writing on disk");
			LOGGER.error(e);
			nameFile = null;
		}
		finally {
			try {
				outputStream.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return nameFile;
	}
	
	public static String addZ(MultiPolygon mp, Coordinate[] coordinates) {
		
		String multiP = mp.toText();
		String[] arrayMp = multiP.split(",");
		int i = 0;
		String multPZ = "MULTIPOLYGONZ(((";
		for (Coordinate ss : coordinates) {
			LOGGER.info(ss.toString());
			if (arrayMp[i].indexOf(")") >= 0 && i < arrayMp.length - 1) {
				multPZ = multPZ + arrayMp[i].replace("MULTIPOLYGON", "").replace("(", "").replace(")", "") + " " + ss.z
						+ ")) , ((";
			}
			else {
				if (arrayMp[i].indexOf(")") >= 0 && i == arrayMp.length - 1) {
					multPZ = multPZ + arrayMp[i].replace("MULTIPOLYGON", "").replace("(", "").replace(")", "") + " "
							+ ss.z + ")))";
				}
				else {
					multPZ = multPZ + arrayMp[i].replace("MULTIPOLYGON", "").replace("(", "").replace(")", "") + " "
							+ ss.z + " , ";
				}
				
			}
			
			i = i + 1;
		}
		LOGGER.info(multPZ);
		return multPZ;
		
	}
	
	public static String execCmd(String[] cmd) throws java.io.IOException {
		java.util.Scanner s = new java.util.Scanner(Runtime.getRuntime().exec(cmd).getInputStream())
				.useDelimiter("\\A");
		return s.hasNext() ? s.next() : "";
	}
	
	public static String createPolygonZFromCoordinates(List<String> coordinates) {
		//POLYGONZ((0 0 0,4 0 0,4 4 0,0 4 0,0 0 0))
		String polygonZ = "POLYGONZ ((";
		for (String elem : coordinates) {
			if (polygonZ.equals("POLYGONZ ((")) {
				polygonZ = polygonZ + elem;
			}
			else {
				polygonZ = polygonZ + "," + elem;
			}
		}
		return polygonZ + "))";
	}
	
	/*
	 * cancella i file temporanei
	 */
	public static boolean deleteTmpFile(String filePath) {
		File tmpFile = new File(filePath);
		if (tmpFile != null && tmpFile.exists()) {
			LOGGER.debug("cancello il file temporaneo " + tmpFile.getName());
			return tmpFile.delete();
		}
		return false;
	}
}
