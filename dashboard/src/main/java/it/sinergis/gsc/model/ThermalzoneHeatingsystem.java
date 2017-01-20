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
 * ThermalzoneHeatingsystem generated by hbm2java
 */
@Entity
@Table(name = "THERMALZONE_HEATINGSYSTEM")
public class ThermalzoneHeatingsystem implements java.io.Serializable {
	
	private DHeatingsystem DHeatingsystem;
	private ThermalzoneHeatingsystemId id;
	private Thermalzone thermalzone;
	
	public ThermalzoneHeatingsystem() {
	}
	
	public ThermalzoneHeatingsystem(ThermalzoneHeatingsystemId id, DHeatingsystem DHeatingsystem,
	        Thermalzone thermalzone) {
		this.id = id;
		this.DHeatingsystem = DHeatingsystem;
		this.thermalzone = thermalzone;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "HEATINGSYSTEM", nullable = false, insertable = false, updatable = false)
	public DHeatingsystem getDHeatingsystem() {
		return this.DHeatingsystem;
	}
	
	@EmbeddedId
	@AttributeOverrides({
	        @AttributeOverride(name = "uuid", column = @Column(name = "UUID", nullable = false, length = 70)),
	        @AttributeOverride(name = "heatingsystem", column = @Column(name = "HEATINGSYSTEM", nullable = false, length = 80)) })
	public ThermalzoneHeatingsystemId getId() {
		return this.id;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "UUID", nullable = false, insertable = false, updatable = false)
	public Thermalzone getThermalzone() {
		return this.thermalzone;
	}
	
	public void setDHeatingsystem(DHeatingsystem DHeatingsystem) {
		this.DHeatingsystem = DHeatingsystem;
	}
	
	public void setId(ThermalzoneHeatingsystemId id) {
		this.id = id;
	}
	
	public void setThermalzone(Thermalzone thermalzone) {
		this.thermalzone = thermalzone;
	}
	
}
