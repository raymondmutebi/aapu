package org.pahappa.systems.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.sers.webutils.model.BaseEntity;

@Entity
@Table(name = "app_setting")
@Inheritance(strategy = InheritanceType.JOINED)
public class SystemSetting extends BaseEntity {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private float referrerPercentageCharge;
    private String egosmsUrl;
    private String egoSmsApiUsername;
    private String egoSmsApiPassword;
    private float registartionFee;
    private float subscriptionFee;
    private String flutterPublicKey;
    private String flutterSecretKey;
    private String smtpAddress = "apikey";
    private String smtpPassword = "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";
    private String smtpHost = "smtp.sendgrid.net";
    private String smtpPort = "587";
    private LookUpField professional;

    private boolean sendGrid;
    private String sendGridUrl;
    private String sendGridSenderAddress;
    private String sendGridBearerToken;
     private String postEndpoint="http://aapug.org/wp-json/wp/v2/posts";

    private String balanceCode = "UGX-MTNMM";
    private String flutterwavePublicKey = "FLWPUBK_TEST-f43bfbb4d1e42b1452641aa8585f49ab-X";
    private String flutterwaveEncryptionKey = "FLWSECK_TESTe172564b6676";
    private String flutterwaveSecretKey = "FLWSECK-433eeec81d5f1e5bdb811111db9484dd-X";
    private String flutterwaveUrl = "https://api.flutterwave.com/v3/";
     private String flutterwaveReditectUrl = "http://212.111.42.51:8080/aapu/PaymentComplete";

    /**
     * @return the referrerPercentageCharge
     */
    @Column(name = "referrer_percentage_charge", nullable = true, length = 10)
    public float getReferrerPercentageCharge() {
        return referrerPercentageCharge;
    }

    /**
     * @param referrerPercentageCharge the referrerPercentageCharge to set
     */
    public void setReferrerPercentageCharge(float referrerPercentageCharge) {
        this.referrerPercentageCharge = referrerPercentageCharge;
    }

    /**
     * @return the egosmsUrl
     */
    @Column(name = "egosms_url", nullable = true, length = 100)
    public String getEgosmsUrl() {
        return egosmsUrl;
    }

    /**
     * @param egosmsUrl the egosmsUrl to set
     */
    public void setEgosmsUrl(String egosmsUrl) {
        this.egosmsUrl = egosmsUrl;
    }

    /**
     * @return the egoSmsApiUsername
     */
    @Column(name = "ego_sms_api_username", nullable = true, length = 100)
    public String getEgoSmsApiUsername() {
        return egoSmsApiUsername;
    }

    /**
     * @param egoSmsApiUsername the egoSmsApiUsername to set
     */
    public void setEgoSmsApiUsername(String egoSmsApiUsername) {
        this.egoSmsApiUsername = egoSmsApiUsername;
    }

    /**
     * @return the egoSmsApiPassword
     */
    @Column(name = "ego_sms_api_password", nullable = true, length = 100)
    public String getEgoSmsApiPassword() {
        return egoSmsApiPassword;
    }

    /**
     * @param egoSmsApiPassword the egoSmsApiPassword to set
     */
    public void setEgoSmsApiPassword(String egoSmsApiPassword) {
        this.egoSmsApiPassword = egoSmsApiPassword;
    }

    @Column(name = "balanca_code", length = 10)
    public String getBalanceCode() {
        return balanceCode;
    }

    public void setBalanceCode(String balanceCode) {
        this.balanceCode = balanceCode;
    }

    @Column(name = "registartion_fee")
    public float getRegistartionFee() {
        return registartionFee;
    }

    public void setRegistartionFee(float registartionFee) {
        this.registartionFee = registartionFee;
    }

    @Column(name = "subscription_fee")
    public float getSubscriptionFee() {
        return subscriptionFee;
    }

    public void setSubscriptionFee(float subscriptionFee) {
        this.subscriptionFee = subscriptionFee;
    }

    @Column(name = "flutter_public_key")
    public String getFlutterPublicKey() {
        return flutterPublicKey;
    }

    public void setFlutterPublicKey(String flutterPublicKey) {
        this.flutterPublicKey = flutterPublicKey;
    }

    @Column(name = "flutter_secret_key")
    public String getFlutterSecretKey() {
        return flutterSecretKey;
    }

    public void setFlutterSecretKey(String flutterSecretKey) {
        this.flutterSecretKey = flutterSecretKey;
    }

    @Column(name = "smtp_address")
    public String getSmtpAddress() {
        return smtpAddress;
    }

    public void setSmtpAddress(String smtpAddress) {
        this.smtpAddress = smtpAddress;
    }

    @Column(name = "smtp_password")
    public String getSmtpPassword() {
        return smtpPassword;
    }

    public void setSmtpPassword(String smtpPassword) {
        this.smtpPassword = smtpPassword;
    }

    @Column(name = "smtp_host")
    public String getSmtpHost() {
        return smtpHost;
    }

    public void setSmtpHost(String smtpHost) {
        this.smtpHost = smtpHost;
    }

    @Column(name = "smtp_port")
    public String getSmtpPort() {
        return smtpPort;
    }

    public void setSmtpPort(String smtpPort) {
        this.smtpPort = smtpPort;
    }

    @Column(name = "is_send_grid")
    public boolean isSendGrid() {
        return sendGrid;
    }

    public void setSendGrid(boolean sendGrid) {
        this.sendGrid = sendGrid;
    }

    @Column(name = "sendgrid_url")
    public String getSendGridUrl() {
        return sendGridUrl;
    }

    public void setSendGridUrl(String sendGridUrl) {
        this.sendGridUrl = sendGridUrl;
    }

    @Column(name = "sendgrid_sender_address")
    public String getSendGridSenderAddress() {
        return sendGridSenderAddress;
    }

    public void setSendGridSenderAddress(String sendGridSenderAddress) {
        this.sendGridSenderAddress = sendGridSenderAddress;
    }

    @Column(name = "sendgrid_bearer_token")
    public String getSendGridBearerToken() {
        return sendGridBearerToken;
    }

    public void setSendGridBearerToken(String sendGridBearerToken) {
        this.sendGridBearerToken = sendGridBearerToken;
    }

    @ManyToOne
    @JoinColumn(name = "proffesionals_database")
    public LookUpField getProfessional() {
        return professional;
    }

    public void setProfessional(LookUpField professional) {
        this.professional = professional;
    }

     @Column(name = "rave_public_key")
    public String getFlutterwavePublicKey() {
        return flutterwavePublicKey;
    }

    public void setFlutterwavePublicKey(String flutterwavePublicKey) {
        this.flutterwavePublicKey = flutterwavePublicKey;
    }

      @Column(name = "rave_encryption_key")
    public String getFlutterwaveEncryptionKey() {
        return flutterwaveEncryptionKey;
    }

    public void setFlutterwaveEncryptionKey(String flutterwaveEncryptionKey) {
        this.flutterwaveEncryptionKey = flutterwaveEncryptionKey;
    }

      @Column(name = "rave_secret_key")
    public String getFlutterwaveSecretKey() {
        return flutterwaveSecretKey;
    }

    public void setFlutterwaveSecretKey(String flutterwaveSecretKey) {
        this.flutterwaveSecretKey = flutterwaveSecretKey;
    }

      @Column(name = "rave_base_url")
    public String getFlutterwaveUrl() {
        return flutterwaveUrl;
    }

    public void setFlutterwaveUrl(String flutterwaveUrl) {
        this.flutterwaveUrl = flutterwaveUrl;
    }

    @Column(name = "rave_redirect_url")
    public String getFlutterwaveReditectUrl() {
        return flutterwaveReditectUrl;
    }

    public void setFlutterwaveReditectUrl(String flutterwaveReditectUrl) {
        this.flutterwaveReditectUrl = flutterwaveReditectUrl;
    }

     @Column(name = "posts_endpoint_url")
    public String getPostEndpoint() {
        return postEndpoint;
    }

    public void setPostEndpoint(String postEndpoint) {
        this.postEndpoint = postEndpoint;
    }
    
    
    

    @Override
    public boolean equals(Object object) {
        return object instanceof SystemSetting && (super.getId() != null) ? super.getId().equals(((SystemSetting) object).getId())
                : (object == this);
    }

    @Override
    public int hashCode() {
        return super.getId() != null ? this.getClass().hashCode() + super.getId().hashCode() : super.hashCode();
    }
}
