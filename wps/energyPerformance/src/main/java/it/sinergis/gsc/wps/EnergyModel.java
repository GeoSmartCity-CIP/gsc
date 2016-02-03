package it.sinergis.gsc.wps;

import com.vividsolutions.jts.geom.Geometry;

public class EnergyModel {
	private String id;
	private String geomText;
	private String refurbishment;
	private Boolean residential;
	private Integer yearOfCostruction;
	private Double ave_flor;
	private Double floor;
	private Double heigth;
	private String typology;
	
	
	private Double area;
	private Double perimeter;
	private Double commonPer;
	
	

	
	private Double pWind;
	private Double uRoof;
	private Double uFloor;
	private Double uWall;
	private Double uWin;
	private Double deltaU;
	
	
	private Double epi;
	private Double epe;
	private Double ephw;
	
	private Geometry geometry;
	
	public Double getArea() {
		return area;
	}
	public void setArea(Double area) {
		this.area = area;
	}
	public Double getPerimeter() {
		return perimeter;
	}
	public void setPerimeter(Double perimeter) {
		this.perimeter = perimeter;
	}
	public Double getCommonPer() {
		return commonPer;
	}
	public void setCommonPer(Double commonPer) {
		this.commonPer = commonPer;
	}
	
	
	public Double getEpe() {
		return epe;
	}
	public void setEpe(Double epe) {
		this.epe = epe;
	}
	public Double getEphw() {
		return ephw;
	}
	public void setEphw(Double ephw) {
		this.ephw = ephw;
	}
	public Double getuRoof() {
		return uRoof;
	}
	public void setuRoof(Double uRoof) {
		this.uRoof = uRoof;
	}
	public Double getuFloor() {
		return uFloor;
	}
	public void setuFloor(Double uFloor) {
		this.uFloor = uFloor;
	}
	public Double getuWall() {
		return uWall;
	}
	public void setuWall(Double uWall) {
		this.uWall = uWall;
	}
	public Double getuWin() {
		return uWin;
	}
	public void setuWin(Double uWin) {
		this.uWin = uWin;
	}
	public Double getDeltaU() {
		return deltaU;
	}
	public void setDeltaU(Double deltaU) {
		this.deltaU = deltaU;
	}
	public String getTypology() {
		return typology;
	}
	public void setTypology(String typology) {
		this.typology = typology;
	}
	public Boolean getResidential() {
		return residential;
	}

	
	
	
	
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getGeomText() {
		return geomText;
	}
	public void setGeomText(String geomText) {
		this.geomText = geomText;
	}
	public String getRefurbishment() {
		return refurbishment;
	}
	public void setRefurbishment(String refurbishment) {
		this.refurbishment = refurbishment;
	}
	public Boolean isResidential() {
		return residential;
	}
	public void setResidential(Boolean residential) {
		this.residential = residential;
	}
	public Integer getYearOfCostruction() {
		return yearOfCostruction;
	}
	public void setYearOfCostruction(Integer yearOfCostruction) {
		this.yearOfCostruction = yearOfCostruction;
	}
	public Double getAve_flor() {
		return ave_flor;
	}
	public void setAve_flor(Double ave_flor) {
		this.ave_flor = ave_flor;
	}
	public Double getFloor() {
		return floor;
	}
	public void setFloor(Double floor) {
		this.floor = floor;
	}
	public Double getHeigth() {
		return heigth;
	}
	public void setHeigth(Double heigth) {
		this.heigth = heigth;
	}
	public Geometry getGeometry() {
		return geometry;
	}
	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}
	public Double getEpi() {
		return epi;
	}
	public void setEpi(Double epi) {
		this.epi = epi;
	}
	public Double getpWind() {
		return pWind;
	}
	public void setpWind(Double pWind) {
		this.pWind = pWind;
	}
	
	
}
