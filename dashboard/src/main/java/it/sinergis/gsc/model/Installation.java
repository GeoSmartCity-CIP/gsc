package it.sinergis.gsc.model;

// Generated Aug 5, 2016 5:19:16 PM by Hibernate Tools 3.4.0.CR1

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Installation generated by hbm2java
 */
@Entity
@Table(name = "INSTALLATION")
public class Installation implements java.io.Serializable {
	
	private Buildings buildings;
	private Date dateActivation;
	private Long dateCBeginning;
	private Long dateCEnd;
	private Long dateRBeginning;
	private Long dateREnd;
	private DConditionofconstruction DConditionofconstruction;
	private DElevationreference DElevationreferenceByElevRef;
	private DElevationreference DElevationreferenceByHeightHeightLow;
	private DElevationreference DElevationreferenceByHeightHeightRef;
	private DHeightstatus DHeightstatus;
	private DInstallationnature DInstallationnature;
	private DTypeValue DTypeValue;
	private Double energyEstimated;
	private Double energyProduced;
	//private Serializable geometry2d;
	private Double heightHeightVal;
	private HOwnershiptype HOwnershiptype;
	private String identifierIdLoc;
	private String identifierIdName;
	private String identifierIdVers;
	private Set<InstallationElevationT> installationElevationTs = new HashSet<InstallationElevationT>(0);
	private Set<InstallationExtRefT> installationExtRefTs = new HashSet<InstallationExtRefT>(0);
	private Long lifespanBeginning;
	private Long lifespanEnd;
	private String name;
	private Double power;
	private String purpose;
	private Double surface;
	private Thermalzone thermalzone;
	private String uuid;
	
	public Installation() {
	}
	
	public Installation(String uuid, DHeightstatus DHeightstatus, DTypeValue DTypeValue, Thermalzone thermalzone,
	        HOwnershiptype HOwnershiptype, DElevationreference DElevationreferenceByHeightHeightRef,
	        DElevationreference DElevationreferenceByHeightHeightLow, DInstallationnature DInstallationnature,
	        Buildings buildings, DElevationreference DElevationreferenceByElevRef,
	        DConditionofconstruction DConditionofconstruction, Date dateActivation, Long dateCBeginning, Long dateCEnd,
	        Long dateRBeginning, Long dateREnd, Double energyEstimated, Double energyProduced, Double heightHeightVal,
	        String identifierIdLoc, String identifierIdName, String identifierIdVers, Long lifespanBeginning,
	        Long lifespanEnd, String name, Double power, String purpose, Double surface, /* Serializable geometry2d, */
	        Set<InstallationExtRefT> installationExtRefTs, Set<InstallationElevationT> installationElevationTs) {
		this.uuid = uuid;
		this.DHeightstatus = DHeightstatus;
		this.DTypeValue = DTypeValue;
		this.thermalzone = thermalzone;
		this.HOwnershiptype = HOwnershiptype;
		this.DElevationreferenceByHeightHeightRef = DElevationreferenceByHeightHeightRef;
		this.DElevationreferenceByHeightHeightLow = DElevationreferenceByHeightHeightLow;
		this.DInstallationnature = DInstallationnature;
		this.buildings = buildings;
		this.DElevationreferenceByElevRef = DElevationreferenceByElevRef;
		this.DConditionofconstruction = DConditionofconstruction;
		this.dateActivation = dateActivation;
		this.dateCBeginning = dateCBeginning;
		this.dateCEnd = dateCEnd;
		this.dateRBeginning = dateRBeginning;
		this.dateREnd = dateREnd;
		this.energyEstimated = energyEstimated;
		this.energyProduced = energyProduced;
		this.heightHeightVal = heightHeightVal;
		this.identifierIdLoc = identifierIdLoc;
		this.identifierIdName = identifierIdName;
		this.identifierIdVers = identifierIdVers;
		this.lifespanBeginning = lifespanBeginning;
		this.lifespanEnd = lifespanEnd;
		this.name = name;
		this.power = power;
		this.purpose = purpose;
		this.surface = surface;
		//this.geometry2d = geometry2d;
		this.installationExtRefTs = installationExtRefTs;
		this.installationElevationTs = installationElevationTs;
	}
	
	public Installation(String uuid, DInstallationnature DInstallationnature,
	        DConditionofconstruction DConditionofconstruction, Serializable geometry2d) {
		this.uuid = uuid;
		this.DInstallationnature = DInstallationnature;
		this.DConditionofconstruction = DConditionofconstruction;
		//this.geometry2d = geometry2d;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "UUID_INST2BDG")
	public Buildings getBuildings() {
		return this.buildings;
	}
	
	@Temporal(TemporalType.DATE)
	@Column(name = "DATE_ACTIVATION", length = 7)
	public Date getDateActivation() {
		return this.dateActivation;
	}
	
	@Column(name = "DATE_C_BEGINNING", precision = 15, scale = 0)
	public Long getDateCBeginning() {
		return this.dateCBeginning;
	}
	
	@Column(name = "DATE_C_END", precision = 15, scale = 0)
	public Long getDateCEnd() {
		return this.dateCEnd;
	}
	
	@Column(name = "DATE_R_BEGINNING", precision = 15, scale = 0)
	public Long getDateRBeginning() {
		return this.dateRBeginning;
	}
	
	@Column(name = "DATE_R_END", precision = 15, scale = 0)
	public Long getDateREnd() {
		return this.dateREnd;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CONDITION", nullable = false)
	public DConditionofconstruction getDConditionofconstruction() {
		return this.DConditionofconstruction;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ELEV_REF")
	public DElevationreference getDElevationreferenceByElevRef() {
		return this.DElevationreferenceByElevRef;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "HEIGHT_HEIGHT_LOW")
	public DElevationreference getDElevationreferenceByHeightHeightLow() {
		return this.DElevationreferenceByHeightHeightLow;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "HEIGHT_HEIGHT_REF")
	public DElevationreference getDElevationreferenceByHeightHeightRef() {
		return this.DElevationreferenceByHeightHeightRef;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "HEIGHT_HEIGHT_STAT")
	public DHeightstatus getDHeightstatus() {
		return this.DHeightstatus;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "INSTALLATION_NATURE", nullable = false)
	public DInstallationnature getDInstallationnature() {
		return this.DInstallationnature;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TYPE")
	public DTypeValue getDTypeValue() {
		return this.DTypeValue;
	}
	
	@Column(name = "ENERGY_ESTIMATED", precision = 126, scale = 0)
	public Double getEnergyEstimated() {
		return this.energyEstimated;
	}
	
	@Column(name = "ENERGY_PRODUCED", precision = 126, scale = 0)
	public Double getEnergyProduced() {
		return this.energyProduced;
	}
	
	//	@Column(name = "GEOMETRY2D", nullable = false)
	//	public Serializable getGeometry2d() {
	//		return this.geometry2d;
	//	}
	
	@Column(name = "HEIGHT_HEIGHT_VAL", precision = 126, scale = 0)
	public Double getHeightHeightVal() {
		return this.heightHeightVal;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "OWNERSHIP")
	public HOwnershiptype getHOwnershiptype() {
		return this.HOwnershiptype;
	}
	
	@Column(name = "IDENTIFIER_ID_LOC", length = 40)
	public String getIdentifierIdLoc() {
		return this.identifierIdLoc;
	}
	
	@Column(name = "IDENTIFIER_ID_NAME", length = 40)
	public String getIdentifierIdName() {
		return this.identifierIdName;
	}
	
	@Column(name = "IDENTIFIER_ID_VERS", length = 40)
	public String getIdentifierIdVers() {
		return this.identifierIdVers;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "installation")
	public Set<InstallationElevationT> getInstallationElevationTs() {
		return this.installationElevationTs;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "installation")
	public Set<InstallationExtRefT> getInstallationExtRefTs() {
		return this.installationExtRefTs;
	}
	
	@Column(name = "LIFESPAN_BEGINNING", precision = 15, scale = 0)
	public Long getLifespanBeginning() {
		return this.lifespanBeginning;
	}
	
	@Column(name = "LIFESPAN_END", precision = 15, scale = 0)
	public Long getLifespanEnd() {
		return this.lifespanEnd;
	}
	
	@Column(name = "NAME", length = 100)
	public String getName() {
		return this.name;
	}
	
	@Column(name = "POWER", precision = 126, scale = 0)
	public Double getPower() {
		return this.power;
	}
	
	@Column(name = "PURPOSE", length = 40)
	public String getPurpose() {
		return this.purpose;
	}
	
	@Column(name = "SURFACE", precision = 126, scale = 0)
	public Double getSurface() {
		return this.surface;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "UUID_INST2TZ")
	public Thermalzone getThermalzone() {
		return this.thermalzone;
	}
	
	@Id
	@Column(name = "UUID", unique = true, nullable = false, length = 70)
	public String getUuid() {
		return this.uuid;
	}
	
	public void setBuildings(Buildings buildings) {
		this.buildings = buildings;
	}
	
	public void setDateActivation(Date dateActivation) {
		this.dateActivation = dateActivation;
	}
	
	public void setDateCBeginning(Long dateCBeginning) {
		this.dateCBeginning = dateCBeginning;
	}
	
	public void setDateCEnd(Long dateCEnd) {
		this.dateCEnd = dateCEnd;
	}
	
	public void setDateRBeginning(Long dateRBeginning) {
		this.dateRBeginning = dateRBeginning;
	}
	
	public void setDateREnd(Long dateREnd) {
		this.dateREnd = dateREnd;
	}
	
	public void setDConditionofconstruction(DConditionofconstruction DConditionofconstruction) {
		this.DConditionofconstruction = DConditionofconstruction;
	}
	
	public void setDElevationreferenceByElevRef(DElevationreference DElevationreferenceByElevRef) {
		this.DElevationreferenceByElevRef = DElevationreferenceByElevRef;
	}
	
	public void setDElevationreferenceByHeightHeightLow(DElevationreference DElevationreferenceByHeightHeightLow) {
		this.DElevationreferenceByHeightHeightLow = DElevationreferenceByHeightHeightLow;
	}
	
	public void setDElevationreferenceByHeightHeightRef(DElevationreference DElevationreferenceByHeightHeightRef) {
		this.DElevationreferenceByHeightHeightRef = DElevationreferenceByHeightHeightRef;
	}
	
	public void setDHeightstatus(DHeightstatus DHeightstatus) {
		this.DHeightstatus = DHeightstatus;
	}
	
	public void setDInstallationnature(DInstallationnature DInstallationnature) {
		this.DInstallationnature = DInstallationnature;
	}
	
	public void setDTypeValue(DTypeValue DTypeValue) {
		this.DTypeValue = DTypeValue;
	}
	
	public void setEnergyEstimated(Double energyEstimated) {
		this.energyEstimated = energyEstimated;
	}
	
	public void setEnergyProduced(Double energyProduced) {
		this.energyProduced = energyProduced;
	}
	
	//	public void setGeometry2d(Serializable geometry2d) {
	//		this.geometry2d = geometry2d;
	//	}
	
	public void setHeightHeightVal(Double heightHeightVal) {
		this.heightHeightVal = heightHeightVal;
	}
	
	public void setHOwnershiptype(HOwnershiptype HOwnershiptype) {
		this.HOwnershiptype = HOwnershiptype;
	}
	
	public void setIdentifierIdLoc(String identifierIdLoc) {
		this.identifierIdLoc = identifierIdLoc;
	}
	
	public void setIdentifierIdName(String identifierIdName) {
		this.identifierIdName = identifierIdName;
	}
	
	public void setIdentifierIdVers(String identifierIdVers) {
		this.identifierIdVers = identifierIdVers;
	}
	
	public void setInstallationElevationTs(Set<InstallationElevationT> installationElevationTs) {
		this.installationElevationTs = installationElevationTs;
	}
	
	public void setInstallationExtRefTs(Set<InstallationExtRefT> installationExtRefTs) {
		this.installationExtRefTs = installationExtRefTs;
	}
	
	public void setLifespanBeginning(Long lifespanBeginning) {
		this.lifespanBeginning = lifespanBeginning;
	}
	
	public void setLifespanEnd(Long lifespanEnd) {
		this.lifespanEnd = lifespanEnd;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setPower(Double power) {
		this.power = power;
	}
	
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	
	public void setSurface(Double surface) {
		this.surface = surface;
	}
	
	public void setThermalzone(Thermalzone thermalzone) {
		this.thermalzone = thermalzone;
	}
	
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
}
