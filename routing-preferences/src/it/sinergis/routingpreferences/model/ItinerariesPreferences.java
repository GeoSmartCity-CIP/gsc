package it.sinergis.routingpreferences.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.hibernatespatial.postgis.StringJsonUserType;

@Entity
@Table(name = "rp_t_itineraries")
@TypeDefs({ @TypeDef(name = "StringJsonObject", typeClass = StringJsonUserType.class) })
public class ItinerariesPreferences implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "id", nullable=false)
	@SequenceGenerator(name = "rp_t_itineraries_id_seq", sequenceName = "rp_t_itineraries_id_seq", allocationSize = 1)
 	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rp_t_itineraries_id_seq")
	private Long id;
	
	@Type(type = "StringJsonObject")
	@Column(name = "data")
    private String data;

	public ItinerariesPreferences() {
		super();
	}
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	
}

