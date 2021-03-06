package it.sinergis.gsc.model;

// Generated Aug 5, 2016 5:19:16 PM by Hibernate Tools 3.4.0.CR1

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * HCurrentuse generated by hbm2java
 */
@Entity
@Table(name = "H_CURRENTUSE")
public class HCurrentuse implements java.io.Serializable {
	
	private String alphacode;
	private Set<Buildings> buildingses = new HashSet<Buildings>(0);
	private Set<BuildingsUseMT> buildingsUseMTs = new HashSet<BuildingsUseMT>(0);
	private String code;
	private String definition;
	private String name;
	
	public HCurrentuse() {
	}
	
	public HCurrentuse(String code) {
		this.code = code;
	}
	
	public HCurrentuse(String code, String name, String definition, String alphacode, Set<Buildings> buildingses,
	        Set<BuildingsUseMT> buildingsUseMTs) {
		this.code = code;
		this.name = name;
		this.definition = definition;
		this.alphacode = alphacode;
		this.buildingses = buildingses;
		this.buildingsUseMTs = buildingsUseMTs;
	}
	
	@Column(name = "ALPHACODE", length = 80)
	public String getAlphacode() {
		return this.alphacode;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "HCurrentuse")
	public Set<Buildings> getBuildingses() {
		return this.buildingses;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "HCurrentuse")
	public Set<BuildingsUseMT> getBuildingsUseMTs() {
		return this.buildingsUseMTs;
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
	
	@Column(name = "NAME", length = 850)
	public String getName() {
		return this.name;
	}
	
	public void setAlphacode(String alphacode) {
		this.alphacode = alphacode;
	}
	
	public void setBuildingses(Set<Buildings> buildingses) {
		this.buildingses = buildingses;
	}
	
	public void setBuildingsUseMTs(Set<BuildingsUseMT> buildingsUseMTs) {
		this.buildingsUseMTs = buildingsUseMTs;
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
