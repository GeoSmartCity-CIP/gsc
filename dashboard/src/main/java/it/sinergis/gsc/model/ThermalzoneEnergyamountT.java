package it.sinergis.gsc.model;

// Generated Aug 5, 2016 5:19:16 PM by Hibernate Tools 3.4.0.CR1

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * ThermalzoneEnergyamountT generated by hbm2java
 */
@Entity
@Table(name = "THERMALZONE_ENERGYAMOUNT_T")
public class ThermalzoneEnergyamountT implements java.io.Serializable {
	
	private DEnergytype DEnergytype;
	private DEnergyuse DEnergyuse;
	private Double energyamountEstimatedco2;
	private HEnergysource HEnergysource;
	private HUom HUom;
	private ThermalzoneEnergyamountTId id;
	private Thermalzone thermalzone;
	
	public ThermalzoneEnergyamountT() {
	}
	
	public ThermalzoneEnergyamountT(ThermalzoneEnergyamountTId id, HUom HUom, DEnergytype DEnergytype,
	        DEnergyuse DEnergyuse, Thermalzone thermalzone, HEnergysource HEnergysource) {
		this.id = id;
		this.HUom = HUom;
		this.DEnergytype = DEnergytype;
		this.DEnergyuse = DEnergyuse;
		this.thermalzone = thermalzone;
		this.HEnergysource = HEnergysource;
	}
	
	public ThermalzoneEnergyamountT(ThermalzoneEnergyamountTId id, HUom HUom, DEnergytype DEnergytype,
	        DEnergyuse DEnergyuse, Thermalzone thermalzone, HEnergysource HEnergysource, Double energyamountEstimatedco2) {
		this.id = id;
		this.HUom = HUom;
		this.DEnergytype = DEnergytype;
		this.DEnergyuse = DEnergyuse;
		this.thermalzone = thermalzone;
		this.HEnergysource = HEnergysource;
		this.energyamountEstimatedco2 = energyamountEstimatedco2;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ENERGYAMOUNT_E_TYPE", nullable = false, insertable = false, updatable = false)
	public DEnergytype getDEnergytype() {
		return this.DEnergytype;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ENERGYAMOUNT_E_USE", nullable = false, insertable = false, updatable = false)
	public DEnergyuse getDEnergyuse() {
		return this.DEnergyuse;
	}
	
	@Column(name = "ENERGYAMOUNT_ESTIMATEDCO2", precision = 126, scale = 0)
	public Double getEnergyamountEstimatedco2() {
		return this.energyamountEstimatedco2;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ENERGYAMOUNT_E_SOURCE", nullable = false, insertable = false, updatable = false)
	public HEnergysource getHEnergysource() {
		return this.HEnergysource;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ENERGYAMOUNT_E_UOM", nullable = false, insertable = false, updatable = false)
	public HUom getHUom() {
		return this.HUom;
	}
	
	@EmbeddedId
	@AttributeOverrides({
	        @AttributeOverride(name = "uuid", column = @Column(name = "UUID", nullable = false, length = 70)),
	        @AttributeOverride(name = "energyamountEAmount", column = @Column(name = "ENERGYAMOUNT_E_AMOUNT", nullable = false, precision = 126, scale = 0)),
	        @AttributeOverride(name = "energyamountESource", column = @Column(name = "ENERGYAMOUNT_E_SOURCE", nullable = false, length = 80)),
	        @AttributeOverride(name = "energyamountEType", column = @Column(name = "ENERGYAMOUNT_E_TYPE", nullable = false, length = 80)),
	        @AttributeOverride(name = "energyamountEUse", column = @Column(name = "ENERGYAMOUNT_E_USE", nullable = false, length = 80)),
	        @AttributeOverride(name = "energyamountEYear", column = @Column(name = "ENERGYAMOUNT_E_YEAR", nullable = false, length = 7)),
	        @AttributeOverride(name = "energyamountEUom", column = @Column(name = "ENERGYAMOUNT_E_UOM", nullable = false, length = 80)) })
	public ThermalzoneEnergyamountTId getId() {
		return this.id;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "UUID", nullable = false, insertable = false, updatable = false)
	public Thermalzone getThermalzone() {
		return this.thermalzone;
	}
	
	public void setDEnergytype(DEnergytype DEnergytype) {
		this.DEnergytype = DEnergytype;
	}
	
	public void setDEnergyuse(DEnergyuse DEnergyuse) {
		this.DEnergyuse = DEnergyuse;
	}
	
	public void setEnergyamountEstimatedco2(Double energyamountEstimatedco2) {
		this.energyamountEstimatedco2 = energyamountEstimatedco2;
	}
	
	public void setHEnergysource(HEnergysource HEnergysource) {
		this.HEnergysource = HEnergysource;
	}
	
	public void setHUom(HUom HUom) {
		this.HUom = HUom;
	}
	
	public void setId(ThermalzoneEnergyamountTId id) {
		this.id = id;
	}
	
	public void setThermalzone(Thermalzone thermalzone) {
		this.thermalzone = thermalzone;
	}
	
}
