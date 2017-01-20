package it.sinergis.gsc.model;

// Generated Aug 5, 2016 5:19:16 PM by Hibernate Tools 3.4.0.CR1

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * DEnergyPerformanceType generated by hbm2java
 */
@Entity
@Table(name = "D_ENERGY_PERFORMANCE_TYPE")
public class DEnergyPerformanceType implements java.io.Serializable {
	
	private String alphacode;
	private String code;
	private String definition;
	private String name;
	
	public DEnergyPerformanceType() {
	}
	
	public DEnergyPerformanceType(String code) {
		this.code = code;
	}
	
	public DEnergyPerformanceType(String code, String name, String definition, String alphacode) {
		this.code = code;
		this.name = name;
		this.definition = definition;
		this.alphacode = alphacode;
	}
	
	@Column(name = "ALPHACODE", length = 80)
	public String getAlphacode() {
		return this.alphacode;
	}
	
	@Id
	@Column(name = "CODE", unique = true, nullable = false, length = 80)
	public String getCode() {
		return this.code;
	}
	
	@Column(name = "DEFINITION", length = 1200)
	public String getDefinition() {
		return this.definition;
	}
	
	@Column(name = "NAME", length = 160)
	public String getName() {
		return this.name;
	}
	
	public void setAlphacode(String alphacode) {
		this.alphacode = alphacode;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public void setDefinition(String definition) {
		this.definition = definition;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
}
