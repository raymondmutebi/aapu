package org.pahappa.systems.security;

public class BusinessResponse {
	private String businessName;
	private String businessAcronym;
	private String businessEmail;
	private String businessCountry;
	private String businessApiUsername;
	private String businessApiPassword;
	private float businessBalance;
	private String status;
	/**
	 * @return the businessName
	 */
	public String getBusinessName() {
		return businessName;
	}
	/**
	 * @param businessName the businessName to set
	 */
	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}
	/**
	 * @return the businessAcronym
	 */
	public String getBusinessAcronym() {
		return businessAcronym;
	}
	/**
	 * @param businessAcronym the businessAcronym to set
	 */
	public void setBusinessAcronym(String businessAcronym) {
		this.businessAcronym = businessAcronym;
	}
	/**
	 * @return the businessEmail
	 */
	public String getBusinessEmail() {
		return businessEmail;
	}
	/**
	 * @param businessEmail the businessEmail to set
	 */
	public void setBusinessEmail(String businessEmail) {
		this.businessEmail = businessEmail;
	}
	/**
	 * @return the businessCountry
	 */
	public String getBusinessCountry() {
		return businessCountry;
	}
	/**
	 * @param businessCountry the businessCountry to set
	 */
	public void setBusinessCountry(String businessCountry) {
		this.businessCountry = businessCountry;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return the businessApiUsername
	 */
	public String getBusinessApiUsername() {
		return businessApiUsername;
	}
	/**
	 * @param businessApiUsername the businessApiUsername to set
	 */
	public void setBusinessApiUsername(String businessApiUsername) {
		this.businessApiUsername = businessApiUsername;
	}
	/**
	 * @return the businessApiPassword
	 */
	public String getBusinessApiPassword() {
		return businessApiPassword;
	}
	/**
	 * @param businessApiPassword the businessApiPassword to set
	 */
	public void setBusinessApiPassword(String businessApiPassword) {
		this.businessApiPassword = businessApiPassword;
	}
	/**
	 * @return the businessBalance
	 */
	public float getBusinessBalance() {
		return businessBalance;
	}
	/**
	 * @param businessBalance the businessBalance to set
	 */
	public void setBusinessBalance(float businessBalance) {
		this.businessBalance = businessBalance;
	}
	
}
