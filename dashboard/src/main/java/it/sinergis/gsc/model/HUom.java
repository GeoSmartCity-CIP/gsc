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
 * HUom generated by hbm2java
 */
@Entity
@Table(name = "H_UOM")
public class HUom implements java.io.Serializable {
	
	private String alphacode;
	private Set<BuildingsEnergyamountT> buildingsEnergyamountTs = new HashSet<BuildingsEnergyamountT>(0);
	private String code;
	private Set<Conversion> conversionsForInput = new HashSet<Conversion>(0);
	private Set<Conversion> conversionsForOutput = new HashSet<Conversion>(0);
	private String definition;
	private String name;
	private Set<ThermalzoneEnergyamountT> thermalzoneEnergyamountTs = new HashSet<ThermalzoneEnergyamountT>(0);
	private Set<Thermalzone> thermalzones = new HashSet<Thermalzone>(0);
	
	public HUom() {
	}
	
	public HUom(String code) {
		this.code = code;
	}
	
	public HUom(String code, String name, String definition, String alphacode, Set<Conversion> conversionsForInput,
	        Set<ThermalzoneEnergyamountT> thermalzoneEnergyamountTs, Set<Thermalzone> thermalzones,
	        Set<Conversion> conversionsForOutput, Set<BuildingsEnergyamountT> buildingsEnergyamountTs) {
		this.code = code;
		this.name = name;
		this.definition = definition;
		this.alphacode = alphacode;
		this.conversionsForInput = conversionsForInput;
		this.thermalzoneEnergyamountTs = thermalzoneEnergyamountTs;
		this.thermalzones = thermalzones;
		this.conversionsForOutput = conversionsForOutput;
		this.buildingsEnergyamountTs = buildingsEnergyamountTs;
	}
	
	@Column(name = "ALPHACODE", length = 80)
	public String getAlphacode() {
		return this.alphacode;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "HUom")
	public Set<BuildingsEnergyamountT> getBuildingsEnergyamountTs() {
		return this.buildingsEnergyamountTs;
	}
	
	@Id
	@Column(name = "CODE", unique = true, nullable = false, length = 80)
	public String getCode() {
		return this.code;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "HUomByInput")
	public Set<Conversion> getConversionsForInput() {
		return this.conversionsForInput;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "HUomByOutput")
	public Set<Conversion> getConversionsForOutput() {
		return this.conversionsForOutput;
	}
	
	@Column(name = "DEFINITION", length = 1200)
	public String getDefinition() {
		return this.definition;
	}
	
	@Column(name = "NAME", length = 850)
	public String getName() {
		return this.name;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "HUom")
	public Set<ThermalzoneEnergyamountT> getThermalzoneEnergyamountTs() {
		return this.thermalzoneEnergyamountTs;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "HUom")
	public Set<Thermalzone> getThermalzones() {
		return this.thermalzones;
	}
	
	public void setAlphacode(String alphacode) {
		this.alphacode = alphacode;
	}
	
	public void setBuildingsEnergyamountTs(Set<BuildingsEnergyamountT> buildingsEnergyamountTs) {
		this.buildingsEnergyamountTs = buildingsEnergyamountTs;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public void setConversionsForInput(Set<Conversion> conversionsForInput) {
		this.conversionsForInput = conversionsForInput;
	}
	
	public void setConversionsForOutput(Set<Conversion> conversionsForOutput) {
		this.conversionsForOutput = conversionsForOutput;
	}
	
	public void setDefinition(String definition) {
		this.definition = definition;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setThermalzoneEnergyamountTs(Set<ThermalzoneEnergyamountT> thermalzoneEnergyamountTs) {
		this.thermalzoneEnergyamountTs = thermalzoneEnergyamountTs;
	}
	
	public void setThermalzones(Set<Thermalzone> thermalzones) {
		this.thermalzones = thermalzones;
	}
	
}
