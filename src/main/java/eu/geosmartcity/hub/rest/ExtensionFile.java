package eu.geosmartcity.hub.rest;

enum ExtensionFile {
	GML("gml"), ZIP("package"), TXT("single"), KML("kml"), CITYGML("citygml");
	
	private String extensiondName;
	
	private ExtensionFile(String fieldName) {
		this.extensiondName = fieldName;
	}
	
	public String getExtdName() {
		return extensiondName;
	}
	
	public static ExtensionFile getField(String field) {
		for (ExtensionFile extensionField : values()) {
			if (extensionField.toString().equals(field)) {
				return extensionField;
			}
		}
		return null;
	}
	
}
