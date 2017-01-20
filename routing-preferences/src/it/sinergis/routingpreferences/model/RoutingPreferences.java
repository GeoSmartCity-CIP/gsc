package it.sinergis.routingpreferences.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.hibernatespatial.postgis.StringJsonUserType;

/**
 * Entity implementation class for Entity: RoutingPreferences
 *
 * @author Andrea Di Nora
 */
@Entity
@Table(name = "rp_t_preferences")
@TypeDefs({ @TypeDef(name = "StringJsonObject", typeClass = StringJsonUserType.class) })
public class RoutingPreferences implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Type(type = "StringJsonObject")
	@Column(name = "data")
    private String data;

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
}
//@Entity
//@Table(name = "rp_t_preferences")
//@NamedQueries({ @NamedQuery(name = "RoutingPreferences.findByUserId", query = "SELECT rp FROM RoutingPreferences rp WHERE rp.userId = :userId") })
//public class RoutingPreferences implements Serializable {
//
//	private static final long serialVersionUID = 1L;
//
//	@Id
//	@SequenceGenerator(name = "rp_t_preferences_id_seq", sequenceName = "rp_t_preferences_id_seq", allocationSize = 1)
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rp_t_preferences_id_seq")
//	@Column(name = "Id", nullable = false)
//	private Long id;
//
//	@Column(name = "UserId", nullable = false)
//	private String userId;
//
//	@Column(name = "MaxWalkDistance", nullable = false)
//	private int maxWalkDistance;
//
//	@Column(name = "WalkingSpeed", nullable = false)
//	private int walkingSpeed;
//
//	@Column(name = "MaxBikeDistance", nullable = false)
//	private int maxBikeDistance;
//
//	@Column(name = "BikingSpeed", nullable = false)
//	private int bikingSpeed;
//
//	public RoutingPreferences() {
//		super();
//	}
//
//	public Long getId() {
//		return id;
//	}
//
//	public void setId(Long id) {
//		this.id = id;
//	}
//
//	public String getUserId() {
//		return userId;
//	}
//
//	public void setUserId(String userId) {
//		this.userId = userId;
//	}
//
//	public int getMaxWalkDistance() {
//		return maxWalkDistance;
//	}
//
//	public void setMaxWalkDistance(int maxWalkDistance) {
//		this.maxWalkDistance = maxWalkDistance;
//	}
//
//	public int getWalkingSpeed() {
//		return walkingSpeed;
//	}
//
//	public void setWalkingSpeed(int walkingSpeed) {
//		this.walkingSpeed = walkingSpeed;
//	}
//
//	public int getMaxBikeDistance() {
//		return maxBikeDistance;
//	}
//
//	public void setMaxBikeDistance(int maxBikeDistance) {
//		this.maxBikeDistance = maxBikeDistance;
//	}
//
//	public int getBikingSpeed() {
//		return bikingSpeed;
//	}
//
//	public void setBikingSpeed(int bikingSpeed) {
//		this.bikingSpeed = bikingSpeed;
//	}
//
//	@Override
//	public int hashCode() {
//		final int prime = 31;
//		int result = 1;
//		result = prime * result
//				+ ((userId == null) ? 0 : userId.hashCode());
//		result = prime * result + walkingSpeed;
//		result = prime * result + maxWalkDistance;
//		result = prime * result + maxBikeDistance;
//		result = prime * result + ((id == null) ? 0 : id.hashCode());
//		result = prime * result + bikingSpeed;
//		return result;
//	}
//
//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj)
//			return true;
//		if (obj == null)
//			return false;
//		if (getClass() != obj.getClass())
//			return false;
//		RoutingPreferences other = (RoutingPreferences) obj;
//		if (userId == null) {
//			if (other.userId != null)
//				return false;
//		} else if (!userId.equals(other.userId))
//			return false;
//		if (id == null) {
//			if (other.id != null)
//				return false;
//		} else if (!id.equals(other.id))
//			return false;
//		if (walkingSpeed != other.walkingSpeed)
//			return false;
//		if (maxWalkDistance != other.maxWalkDistance)
//			return false;
//		if (maxBikeDistance != other.maxBikeDistance)
//			return false;
//		if (bikingSpeed != other.bikingSpeed)
//			return false;
//		return true;
//	}
//
//	@Override
//	public String toString() {
//		return "RoutingPreference [id=" + id + ", userId=" + userId
//				+ ", walkingSpeed=" + walkingSpeed + ", maxWalkDistance=" + maxWalkDistance + ", maxBikeDistance=" + maxBikeDistance + ", bikingSpeed=" + bikingSpeed + "]";
//	}
//}
