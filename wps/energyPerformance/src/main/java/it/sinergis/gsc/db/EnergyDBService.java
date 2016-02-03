package it.sinergis.gsc.db;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.Properties;

import org.postgresql.PGStatement;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

import it.sinergis.gsc.wps.EnergyModel;

public class EnergyDBService {
	private String schemaName;
	private String tableName;
	private String projection;
	
	private String jdbc;
	private String user;
	private String password;
	private Boolean debug; 
	
	private Connection con;
	
	public EnergyDBService(String schema, String table, String projection ) throws SQLException{
		this.schemaName = schema;
		this.tableName = table;
		this.projection = projection;
		
		Properties prop = new Properties();
    	InputStream input = null;
		
    	
    	
    	
    	try {
            
    		String filename = "configWPSEnergy.properties";
    		input = getClass().getClassLoader().getResourceAsStream(filename);
    		if(input==null){
    	            System.out.println("Sorry, unable to find " + filename);
    		    return;
    		}

    		//load a properties file from class path, inside static method
    		prop.load(input);
    	        jdbc = prop.getProperty("database");
    	    	user = prop.getProperty("user");
    	    	password = prop.getProperty("password");
    	    	debug = Boolean.parseBoolean(prop.getProperty("debug","false"));
 
    	} catch (IOException ex) {
    		ex.printStackTrace();
        } finally{
        	if(input!=null){
        		try {
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        	}
        }
		
		
    	con =  DriverManager.getConnection(jdbc, user,password);
		
		//this.cp = cp;
	}
	
	public String getSchemaName() {
		return schemaName;
	}
	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public boolean deleteEnergytable() throws SQLException{
		if (debug) return false;
		String query = "DROP TABLE "+schemaName+"."+tableName+";";
		Connection connection =con;
		Statement stmt = connection.createStatement();

		stmt.executeUpdate(query);
		stmt.close();
		return true;
		
	}
	
	
	public boolean createEnergyTable() throws SQLException{
		
		String query="CREATE TABLE "+schemaName+"."+tableName+"(\n"+
			         "   id serial PRIMARY KEY,\n"+
			         "   classid varchar(70),\n"+
			         "   zone varchar(70),\n"+
			         
			         "   geom geometry(MultiPolygon,"+projection+"),\n"+
			         "   ave_floor double precision,\n"+
			         "   bdg_units integer,\n"+
			         "   begin integer,\n"+
			         "   \"end\" integer,\n"+
			         "   elev_value integer,\n"+
			         "   --elev_ref character varying(40),\n"+
			         "   floors double precision,\n"+
			         "   --height_sta character varying(40),\n"+
			         "   height_val double precision,\n"+
			         "   refurbishm character varying(40),\n"+
			         "   area double precision,\n"+
			         "   perimeter double precision,\n"+
			         "   per_comm double precision,\n"+
			         "   name_typology character varying(4),\n"+
			         "   code_typology integer,\n"+
			         "   residential boolean,\n"+
			         "   stairwell integer,\n"+
			         "	 dd double precision,\n"+
			         
			         "   u_roof double precision,\n"+
			         "   u_floor double precision,\n"+
			         "   p_win double precision,\n"+
			         "   u_wall double precision,\n"+
			         "   u_win double precision,\n"+
			         "   delta_u double precision,\n"+
			         "   irrad_hor integer,\n"+
			         "   temp_avg double precision,\n"+
			         "   heating_start integer,\n"+
			         "   heating_end integer,\n"+
			         "   comf_avg double precision,\n"+
			         
					"   cdd double precision,\n"+
					"   comf_avg_s double precision,\n"+
					"   irradiation_s double precision,\n"+
					"   cooling_start integer,\n"+
					"   cooling_end integer,\n"+
			         
			         "   ephw double precision,\n"+
			         "   epe double precision,\n"+
			         "   epi double precision,\n"+
			         "   pilot integer)\n"+
			         "   WITH (\n"+
			         "      OIDS=FALSE\n"+
			         "  );\n";
		//Connection connection = MyPool.getPool().getConnection();
		Connection connection =con;
		Statement stmt = connection.createStatement();

		stmt.executeUpdate(query);
		stmt.close();
		return true;
		
	}
	
	
	public LinkedList<EnergyModel>  getResult() throws SQLException, ParseException{
		String query="select ST_AsText(geom) as mygeomtext, * from "+schemaName+"."+tableName+";";
		Connection connection =con;
		Statement stmt = con.createStatement();
		ResultSet rs;
		LinkedList<EnergyModel> lista = new LinkedList<EnergyModel> ();
		rs = stmt.executeQuery(query);
		WKTReader rdr = new WKTReader();
		while(rs.next()){
			EnergyModel em = new EnergyModel();
			String  t = rs.getString("mygeomtext");
			
			Geometry mygeom = rdr.read(t);
			em.setGeometry(mygeom);
			//em.setGeometry((Geometry)rs.getObject("geom"));			
			em.setId(rs.getString("classid"));
			
			em.setResidential(rs.getBoolean("residential"));
			em.setYearOfCostruction(rs.getInt("end"));
			em.setAve_flor(rs.getDouble("ave_floor"));
			em.setFloor(rs.getDouble("floors"));
			em.setHeigth(rs.getDouble("height_val"));
			em.setRefurbishment(rs.getString("refurbishm"));
			em.setArea(rs.getDouble("area"));
			em.setPerimeter(rs.getDouble("perimeter"));
			em.setCommonPer(rs.getDouble("per_comm"));
			
			
			
			em.setuWin(rs.getDouble("u_win"));
			em.setuFloor(rs.getDouble("u_floor"));
			em.setuRoof(rs.getDouble("u_roof"));
			em.setuWall(rs.getDouble("u_wall"));
			em.setDeltaU(rs.getDouble("delta_u"));
			em.setpWind(rs.getDouble("p_win"));
			
			em.setEpi(rs.getDouble("epi"));
			em.setEpe(rs.getDouble("epe"));
			em.setEphw(rs.getDouble("ephw"));//TODO aggiungere il campo 
			
			
			//em.setTypology(rs.getString(columnLabel));
			
			lista.add(em);
		}
		
		
		rs.close();
		stmt.close();
		return lista;
		
		
		
	}
	
	public boolean insertInto(LinkedList<EnergyModel> listaModelli, String climatiZone) throws SQLException{
		String insert ="insert into "+schemaName+"."+tableName+"("
				+ "classid,zone,ave_floor,\"end\",floors,height_val,refurbishm, residential,geom) values"
				+ "(?,?,?,?,?,?,?,?,ST_GeomFromText(?,+"+projection+"))";
		
		Connection connection =con;

		PreparedStatement pstmt = connection.prepareStatement(insert);
		//PGStatement pgstmt = (org.postgresql.PGStatement)pstmt;
		
		for (EnergyModel em : listaModelli){
			pstmt.setString(1,em.getId());
			if ( em.getId() != null){
				pstmt.setString(1, em.getId() );
			}pstmt.setNull(1, java.sql.Types.VARCHAR);

			pstmt.setString(2, climatiZone);
			//pstmt.setDouble(3, em.getAve_flor() );
			//pstmt.setDouble(3, null);
			pstmt.setNull(3, java.sql.Types.DOUBLE);
			if (em.getAve_flor()!= null)  pstmt.setDouble(3, em.getAve_flor());
			else	pstmt.setNull(3, java.sql.Types.DOUBLE);
			
			if ( em.getYearOfCostruction() != null){
				pstmt.setInt(4, em.getYearOfCostruction() );
			}else pstmt.setNull(4, java.sql.Types.INTEGER);
			
			if (em.getFloor()!= null)  pstmt.setDouble(5, em.getFloor());
			else	pstmt.setNull(5, java.sql.Types.DOUBLE);
		
			
			if (em.getHeigth()!= null)  pstmt.setDouble(6, em.getHeigth());
			else	pstmt.setNull(6, java.sql.Types.DOUBLE);
			
			
			if (em.getRefurbishment()!= null)  pstmt.setString(7, em.getRefurbishment());
			else	pstmt.setNull(7, java.sql.Types.VARCHAR);
			
			if (em.getResidential()!= null)  pstmt.setBoolean(8, em.getResidential());
			else	pstmt.setNull(8, java.sql.Types.BOOLEAN);

			
			pstmt.setString(9, em.getGeometry().toText());
			pstmt.executeUpdate();
		}
	

		
		
		
		
		return true;
	}
	
	public boolean computeAreaPer() throws SQLException{
		Connection connection =con;
		String table = schemaName+"."+tableName;
		String query = "WITH intersections AS  ( "
				+ "SELECT s1.id, s1.geom AS geom1,s2.geom AS  geom2 "
				+ " FROM "+table+ " as s1 ,"+table+ " as s2 where ST_Intersects(s1.geom, s2.geom) "
				+ "), perimeter AS "
				+ "(select s1.id, min(ST_Perimeter(ST_Transform(s1.geom1,3857))) as my_perimeter, sum(ST_Length(ST_Intersection(ST_Transform(s1.geom1,3857),ST_Transform(s1.geom2,3857)))) as my_per_common "
				+ "from intersections as s1 "
				+ " group by s1.id) "
				+ "update "+table+ "  as toup set perimeter =  tmp.my_perimeter , per_comm = tmp.my_per_common "
				+ "from (select * from perimeter) as tmp where toup.id = tmp.id;"
				+ "update  "+table+ " SET area = ST_Area(ST_Transform(geom,3857));";
		
		Statement stmt = con.createStatement();		
		stmt.executeUpdate(query);
		stmt.close();
		return true;
	}
	
	
	public boolean updateFloor() throws SQLException{
		Connection connection =con;
		String table = schemaName+"."+tableName;
		String query = query = ""
				+ "update "+table+ " set ave_floor =3 where ave_floor = 0 or ave_floor is null; "
				+ "update "+table+ " set floors = 0 where floors is null;"
				+ "update "+table+ " set floors = height_val/ave_floor where floors < 1 and height_val >= 1  ;"
		        + "update "+table+ " set height_val = floors * ave_floor where height_val is null or height_val < 1;" ;      
				
		
		Statement stmt = con.createStatement();		
		stmt.executeUpdate(query);
		stmt.close();
		return true;
	}
	public boolean updateALL(String climatic) throws SQLException{
		Connection connection =con;
		String table = schemaName+"."+tableName;
		Statement stmt = con.createStatement();	
		String query = query = "select configurations.computeEP('"+schemaName+"' , '"+tableName+"' , '"+climatic+"');"  ;   
				
		
			
		stmt.executeQuery(query);
		stmt.close();
		return true;
	}
	
	
	
	
	public static void main (String args[]) throws SQLException{
		 String dbUrl = "jdbc:postgresql://gsm-db:5432/geomap4data";
		 String user = "postgres";
		 String psw = "postgres";		 
		 System.out.println("prova");
		EnergyDBService  dbEnergy= new EnergyDBService("wps_energy","tmp1454401115660","3044");
		//dbEnergy.computeAreaPer();
		//dbEnergy.updateFloor();
		//dbEnergy.updateALL("2");
		
		
		/*
		try {
			dbEnergy.createEnergyTable();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		EnergyModel em1 = new  EnergyModel();
		//"classid,zone,ave_floor,\"end\",floors,height_val) values"
		em1.setId("tmp1");
		em1.setAve_flor(2.0);
		em1.setYearOfCostruction(2000);
		em1.setFloor(  15.0);
		em1.setHeigth(150.0);
		
		
		EnergyModel em2 = new  EnergyModel();
		//"classid,zone,ave_floor,\"end\",floors,height_val) values"
		em2.setId("tmp1");
		//em2.setAve_flor(2.0);
		em2.setYearOfCostruction(2000);
		//em2.setFloor(15);
		em2.setHeigth(150.0);
		//Geometry geom = 
		
		
		LinkedList<EnergyModel> lista = new LinkedList<EnergyModel>();
		lista.add(em1);
		lista.add(em2);
		//dbEnergy.insertInto(lista);
		*/
		
	}
	
	
}
