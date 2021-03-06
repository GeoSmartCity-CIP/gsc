package it.sinergis.gsc.model;

// Generated Aug 5, 2016 5:19:16 PM by Hibernate Tools 3.4.0.CR1

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * BuildingsStructureMatId generated by hbm2java
 */
@Embeddable
public class BuildingsStructureMatId implements java.io.Serializable {
	
	private String uuid;
	private String structureMat;
	
	public BuildingsStructureMatId() {
	}
	
	public BuildingsStructureMatId(String uuid, String structureMat) {
		this.uuid = uuid;
		this.structureMat = structureMat;
	}
	
	@Column(name = "UUID", nullable = false, length = 70)
	public String getUuid() {
		return this.uuid;
	}
	
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	@Column(name = "STRUCTURE_MAT", nullable = false, length = 80)
	public String getStructureMat() {
		return this.structureMat;
	}
	
	public void setStructureMat(String structureMat) {
		this.structureMat = structureMat;
	}
	
	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof BuildingsStructureMatId))
			return false;
		BuildingsStructureMatId castOther = (BuildingsStructureMatId) other;
		
		return ((this.getUuid() == castOther.getUuid()) || (this.getUuid() != null && castOther.getUuid() != null && this
		        .getUuid().equals(castOther.getUuid())))
		        && ((this.getStructureMat() == castOther.getStructureMat()) || (this.getStructureMat() != null
		                && castOther.getStructureMat() != null && this.getStructureMat().equals(
		                castOther.getStructureMat())));
	}
	
	public int hashCode() {
		int result = 17;
		
		result = 37 * result + (getUuid() == null ? 0 : this.getUuid().hashCode());
		result = 37 * result + (getStructureMat() == null ? 0 : this.getStructureMat().hashCode());
		return result;
	}
	
}
