package it.sinergis.gsc.model;

// Generated Aug 5, 2016 5:19:16 PM by Hibernate Tools 3.4.0.CR1

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * BuildingsEnergyPerformanceTId generated by hbm2java
 */
@Embeddable
public class BuildingsEnergyPerformanceTId implements java.io.Serializable {
	
	private String uuid;
	private Long certCount;
	private Date energyperformancePerfDate;
	private String energyperformancePerfMethod;
	private String energyperformancePerfUom;
	private Double energyperformancePerfValue;
	private String volumeSource;
	private Double volumeValue;
	private String energyperformancePerfType;
	private String energyperformancePerfClass;
	
	public BuildingsEnergyPerformanceTId() {
	}
	
	public BuildingsEnergyPerformanceTId(String uuid, Long certCount, Date energyperformancePerfDate,
	        String energyperformancePerfMethod, String energyperformancePerfUom, Double energyperformancePerfValue,
	        String volumeSource, Double volumeValue, String energyperformancePerfType, String energyperformancePerfClass) {
		this.uuid = uuid;
		this.certCount = certCount;
		this.energyperformancePerfDate = energyperformancePerfDate;
		this.energyperformancePerfMethod = energyperformancePerfMethod;
		this.energyperformancePerfUom = energyperformancePerfUom;
		this.energyperformancePerfValue = energyperformancePerfValue;
		this.volumeSource = volumeSource;
		this.volumeValue = volumeValue;
		this.energyperformancePerfType = energyperformancePerfType;
		this.energyperformancePerfClass = energyperformancePerfClass;
	}
	
	@Column(name = "UUID", length = 70)
	public String getUuid() {
		return this.uuid;
	}
	
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	@Column(name = "CERT_COUNT", precision = 15, scale = 0)
	public Long getCertCount() {
		return this.certCount;
	}
	
	public void setCertCount(Long certCount) {
		this.certCount = certCount;
	}
	
	@Column(name = "ENERGYPERFORMANCE_PERF_DATE", length = 7)
	public Date getEnergyperformancePerfDate() {
		return this.energyperformancePerfDate;
	}
	
	public void setEnergyperformancePerfDate(Date energyperformancePerfDate) {
		this.energyperformancePerfDate = energyperformancePerfDate;
	}
	
	@Column(name = "ENERGYPERFORMANCE_PERF_METHOD", length = 100)
	public String getEnergyperformancePerfMethod() {
		return this.energyperformancePerfMethod;
	}
	
	public void setEnergyperformancePerfMethod(String energyperformancePerfMethod) {
		this.energyperformancePerfMethod = energyperformancePerfMethod;
	}
	
	@Column(name = "ENERGYPERFORMANCE_PERF_UOM", length = 80)
	public String getEnergyperformancePerfUom() {
		return this.energyperformancePerfUom;
	}
	
	public void setEnergyperformancePerfUom(String energyperformancePerfUom) {
		this.energyperformancePerfUom = energyperformancePerfUom;
	}
	
	@Column(name = "ENERGYPERFORMANCE_PERF_VALUE", precision = 126, scale = 0)
	public Double getEnergyperformancePerfValue() {
		return this.energyperformancePerfValue;
	}
	
	public void setEnergyperformancePerfValue(Double energyperformancePerfValue) {
		this.energyperformancePerfValue = energyperformancePerfValue;
	}
	
	@Column(name = "VOLUME_SOURCE", length = 80)
	public String getVolumeSource() {
		return this.volumeSource;
	}
	
	public void setVolumeSource(String volumeSource) {
		this.volumeSource = volumeSource;
	}
	
	@Column(name = "VOLUME_VALUE", precision = 126, scale = 0)
	public Double getVolumeValue() {
		return this.volumeValue;
	}
	
	public void setVolumeValue(Double volumeValue) {
		this.volumeValue = volumeValue;
	}
	
	@Column(name = "ENERGYPERFORMANCE_PERF_TYPE", length = 80)
	public String getEnergyperformancePerfType() {
		return this.energyperformancePerfType;
	}
	
	public void setEnergyperformancePerfType(String energyperformancePerfType) {
		this.energyperformancePerfType = energyperformancePerfType;
	}
	
	@Column(name = "ENERGYPERFORMANCE_PERF_CLASS", length = 3)
	public String getEnergyperformancePerfClass() {
		return this.energyperformancePerfClass;
	}
	
	public void setEnergyperformancePerfClass(String energyperformancePerfClass) {
		this.energyperformancePerfClass = energyperformancePerfClass;
	}
	
	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof BuildingsEnergyPerformanceTId))
			return false;
		BuildingsEnergyPerformanceTId castOther = (BuildingsEnergyPerformanceTId) other;
		
		return ((this.getUuid() == castOther.getUuid()) || (this.getUuid() != null && castOther.getUuid() != null && this
		        .getUuid().equals(castOther.getUuid())))
		        && ((this.getCertCount() == castOther.getCertCount()) || (this.getCertCount() != null
		                && castOther.getCertCount() != null && this.getCertCount().equals(castOther.getCertCount())))
		        && ((this.getEnergyperformancePerfDate() == castOther.getEnergyperformancePerfDate()) || (this
		                .getEnergyperformancePerfDate() != null && castOther.getEnergyperformancePerfDate() != null && this
		                .getEnergyperformancePerfDate().equals(castOther.getEnergyperformancePerfDate())))
		        && ((this.getEnergyperformancePerfMethod() == castOther.getEnergyperformancePerfMethod()) || (this
		                .getEnergyperformancePerfMethod() != null && castOther.getEnergyperformancePerfMethod() != null && this
		                .getEnergyperformancePerfMethod().equals(castOther.getEnergyperformancePerfMethod())))
		        && ((this.getEnergyperformancePerfUom() == castOther.getEnergyperformancePerfUom()) || (this
		                .getEnergyperformancePerfUom() != null && castOther.getEnergyperformancePerfUom() != null && this
		                .getEnergyperformancePerfUom().equals(castOther.getEnergyperformancePerfUom())))
		        && ((this.getEnergyperformancePerfValue() == castOther.getEnergyperformancePerfValue()) || (this
		                .getEnergyperformancePerfValue() != null && castOther.getEnergyperformancePerfValue() != null && this
		                .getEnergyperformancePerfValue().equals(castOther.getEnergyperformancePerfValue())))
		        && ((this.getVolumeSource() == castOther.getVolumeSource()) || (this.getVolumeSource() != null
		                && castOther.getVolumeSource() != null && this.getVolumeSource().equals(
		                castOther.getVolumeSource())))
		        && ((this.getVolumeValue() == castOther.getVolumeValue()) || (this.getVolumeValue() != null
		                && castOther.getVolumeValue() != null && this.getVolumeValue().equals(
		                castOther.getVolumeValue())))
		        && ((this.getEnergyperformancePerfType() == castOther.getEnergyperformancePerfType()) || (this
		                .getEnergyperformancePerfType() != null && castOther.getEnergyperformancePerfType() != null && this
		                .getEnergyperformancePerfType().equals(castOther.getEnergyperformancePerfType())))
		        && ((this.getEnergyperformancePerfClass() == castOther.getEnergyperformancePerfClass()) || (this
		                .getEnergyperformancePerfClass() != null && castOther.getEnergyperformancePerfClass() != null && this
		                .getEnergyperformancePerfClass().equals(castOther.getEnergyperformancePerfClass())));
	}
	
	public int hashCode() {
		int result = 17;
		
		result = 37 * result + (getUuid() == null ? 0 : this.getUuid().hashCode());
		result = 37 * result + (getCertCount() == null ? 0 : this.getCertCount().hashCode());
		result = 37 * result
		        + (getEnergyperformancePerfDate() == null ? 0 : this.getEnergyperformancePerfDate().hashCode());
		result = 37 * result
		        + (getEnergyperformancePerfMethod() == null ? 0 : this.getEnergyperformancePerfMethod().hashCode());
		result = 37 * result
		        + (getEnergyperformancePerfUom() == null ? 0 : this.getEnergyperformancePerfUom().hashCode());
		result = 37 * result
		        + (getEnergyperformancePerfValue() == null ? 0 : this.getEnergyperformancePerfValue().hashCode());
		result = 37 * result + (getVolumeSource() == null ? 0 : this.getVolumeSource().hashCode());
		result = 37 * result + (getVolumeValue() == null ? 0 : this.getVolumeValue().hashCode());
		result = 37 * result
		        + (getEnergyperformancePerfType() == null ? 0 : this.getEnergyperformancePerfType().hashCode());
		result = 37 * result
		        + (getEnergyperformancePerfClass() == null ? 0 : this.getEnergyperformancePerfClass().hashCode());
		return result;
	}
	
}
