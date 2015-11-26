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

@Entity
@Table(name = "rp_t_itineraries")
@TypeDefs({ @TypeDef(name = "StringJsonObject", typeClass = StringJsonUserType.class) })
public class ItinerariesPreferences implements Serializable{
	
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

