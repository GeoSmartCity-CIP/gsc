package it.sinergis.routingpreferences.common;

public class PreferenceObject {
	
	private String userId;
	private String maxWalkingDistance;
	private String walkingSpeed;
	private String maxBikingDistance;
	private String bikingSpeed;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getMaxWalkingDistance() {
		return maxWalkingDistance;
	}
	public void setMaxWalkingDistance(String maxWalkingDistance) {
		this.maxWalkingDistance = maxWalkingDistance;
	}
	public String getWalkingSpeed() {
		return walkingSpeed;
	}
	public void setWalkingSpeed(String walkingSpeed) {
		this.walkingSpeed = walkingSpeed;
	}
	public String getMaxBikingDistance() {
		return maxBikingDistance;
	}
	public void setMaxBikingDistance(String maxBikingDistance) {
		this.maxBikingDistance = maxBikingDistance;
	}
	public String getBikingSpeed() {
		return bikingSpeed;
	}
	public void setBikingSpeed(String bikingSpeed) {
		this.bikingSpeed = bikingSpeed;
	}
	
	
}
