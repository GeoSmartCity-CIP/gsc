package it.sinergis.gsc.model;

// Generated Aug 5, 2016 5:19:16 PM by Hibernate Tools 3.4.0.CR1

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * BuildingsEnergyamountTId generated by hbm2java
 */
@Embeddable
public class BuildingsEnergyamountTId implements java.io.Serializable {
	
	private String uuid;
	private double energyamountEAmount;
	private String energyamountESource;
	private String energyamountEType;
	private String energyamountEUse;
	private Date energyamountEYear;
	private String energyamountEUom;
	
	public BuildingsEnergyamountTId() {
	}
	
	public BuildingsEnergyamountTId(String uuid, double energyamountEAmount, String energyamountESource,
	        String energyamountEType, String energyamountEUse, Date energyamountEYear, String energyamountEUom) {
		this.uuid = uuid;
		this.energyamountEAmount = energyamountEAmount;
		this.energyamountESource = energyamountESource;
		this.energyamountEType = energyamountEType;
		this.energyamountEUse = energyamountEUse;
		this.energyamountEYear = energyamountEYear;
		this.energyamountEUom = energyamountEUom;
	}
	
	@Column(name = "UUID", nullable = false, length = 70)
	public String getUuid() {
		return this.uuid;
	}
	
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	@Column(name = "ENERGYAMOUNT_E_AMOUNT", nullable = false, precision = 126, scale = 0)
	public double getEnergyamountEAmount() {
		return this.energyamountEAmount;
	}
	
	public void setEnergyamountEAmount(double energyamountEAmount) {
		this.energyamountEAmount = energyamountEAmount;
	}
	
	@Column(name = "ENERGYAMOUNT_E_SOURCE", nullable = false, length = 80)
	public String getEnergyamountESource() {
		return this.energyamountESource;
	}
	
	public void setEnergyamountESource(String energyamountESource) {
		this.energyamountESource = energyamountESource;
	}
	
	@Column(name = "ENERGYAMOUNT_E_TYPE", nullable = false, length = 80)
	public String getEnergyamountEType() {
		return this.energyamountEType;
	}
	
	public void setEnergyamountEType(String energyamountEType) {
		this.energyamountEType = energyamountEType;
	}
	
	@Column(name = "ENERGYAMOUNT_E_USE", nullable = false, length = 80)
	public String getEnergyamountEUse() {
		return this.energyamountEUse;
	}
	
	public void setEnergyamountEUse(String energyamountEUse) {
		this.energyamountEUse = energyamountEUse;
	}
	
	@Column(name = "ENERGYAMOUNT_E_YEAR", nullable = false, length = 7)
	public Date getEnergyamountEYear() {
		return this.energyamountEYear;
	}
	
	public void setEnergyamountEYear(Date energyamountEYear) {
		this.energyamountEYear = energyamountEYear;
	}
	
	@Column(name = "ENERGYAMOUNT_E_UOM", nullable = false, length = 80)
	public String getEnergyamountEUom() {
		return this.energyamountEUom;
	}
	
	public void setEnergyamountEUom(String energyamountEUom) {
		this.energyamountEUom = energyamountEUom;
	}
	
	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof BuildingsEnergyamountTId))
			return false;
		BuildingsEnergyamountTId castOther = (BuildingsEnergyamountTId) other;
		
		return ((this.getUuid() == castOther.getUuid()) || (this.getUuid() != null && castOther.getUuid() != null && this
		        .getUuid().equals(castOther.getUuid())))
		        && (this.getEnergyamountEAmount() == castOther.getEnergyamountEAmount())
		        && ((this.getEnergyamountESource() == castOther.getEnergyamountESource()) || (this
		                .getEnergyamountESource() != null && castOther.getEnergyamountESource() != null && this
		                .getEnergyamountESource().equals(castOther.getEnergyamountESource())))
		        && ((this.getEnergyamountEType() == castOther.getEnergyamountEType()) || (this.getEnergyamountEType() != null
		                && castOther.getEnergyamountEType() != null && this.getEnergyamountEType().equals(
		                castOther.getEnergyamountEType())))
		        && ((this.getEnergyamountEUse() == castOther.getEnergyamountEUse()) || (this.getEnergyamountEUse() != null
		                && castOther.getEnergyamountEUse() != null && this.getEnergyamountEUse().equals(
		                castOther.getEnergyamountEUse())))
		        && ((this.getEnergyamountEYear() == castOther.getEnergyamountEYear()) || (this.getEnergyamountEYear() != null
		                && castOther.getEnergyamountEYear() != null && this.getEnergyamountEYear().equals(
		                castOther.getEnergyamountEYear())))
		        && ((this.getEnergyamountEUom() == castOther.getEnergyamountEUom()) || (this.getEnergyamountEUom() != null
		                && castOther.getEnergyamountEUom() != null && this.getEnergyamountEUom().equals(
		                castOther.getEnergyamountEUom())));
	}
	
	public int hashCode() {
		int result = 17;
		
		result = 37 * result + (getUuid() == null ? 0 : this.getUuid().hashCode());
		result = 37 * result + (int) this.getEnergyamountEAmount();
		result = 37 * result + (getEnergyamountESource() == null ? 0 : this.getEnergyamountESource().hashCode());
		result = 37 * result + (getEnergyamountEType() == null ? 0 : this.getEnergyamountEType().hashCode());
		result = 37 * result + (getEnergyamountEUse() == null ? 0 : this.getEnergyamountEUse().hashCode());
		result = 37 * result + (getEnergyamountEYear() == null ? 0 : this.getEnergyamountEYear().hashCode());
		result = 37 * result + (getEnergyamountEUom() == null ? 0 : this.getEnergyamountEUom().hashCode());
		return result;
	}
	
}