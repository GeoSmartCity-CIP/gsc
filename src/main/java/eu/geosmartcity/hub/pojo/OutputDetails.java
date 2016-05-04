package eu.geosmartcity.hub.pojo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "SENSOR_CONTROL")
public class OutputDetails {
	private String wmsUri;
	private String wfsUri;
	private String jsonUri;
	private String shapeUri;
	
	@JsonProperty(value = "wms")
	public String getWmsUri() {
		return wmsUri;
	}
	
	public void setWmsUri(String wmsUri) {
		this.wmsUri = wmsUri;
	}
	
	@JsonProperty(value = "wfs")
	public String getWfsUri() {
		return wfsUri;
	}
	
	public void setWfsUri(String wfsUri) {
		this.wfsUri = wfsUri;
	}
	
	@JsonProperty(value = "shape")
	public String getShapeUri() {
		return shapeUri;
	}
	
	public void setShapeUri(String shapeUri) {
		this.shapeUri = shapeUri;
	}
	
	@JsonProperty(value = "json")
	public String getJsonUri() {
		return jsonUri;
	}

	public void setJsonUri(String jsonUri) {
		this.jsonUri = jsonUri;
	}
	
}
