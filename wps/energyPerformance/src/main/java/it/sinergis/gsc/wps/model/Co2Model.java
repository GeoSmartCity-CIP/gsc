package it.sinergis.gsc.wps.model;

import com.vividsolutions.jts.geom.Geometry;

public class Co2Model {
	
	private String id;
	private Double area;
	private Double ep;
	private Double height;
	private Double co2Emmision;
	
	public Geometry getGeometry() {
		return geometry;
	}
	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Double getArea() {
		return area;
	}
	public void setArea(Double area) {
		this.area = area;
	}
	public Double getEp() {
		return ep;
	}
	public void setEp(Double ep) {
		this.ep = ep;
	}
	public Double getCo2Emmision() {
		return co2Emmision;
	}
	public void setCo2Emmision(Double co2Emmision) {
		this.co2Emmision = co2Emmision;
	}
	public Double getHeight() {
		return height;
	}
	public void setHeight(Double height) {
		this.height = height;
	}
	private Geometry geometry;

}
