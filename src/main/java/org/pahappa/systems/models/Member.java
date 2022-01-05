package org.pahappa.systems.models;


import org.pahappa.systems.constants.AccountStatus;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.sers.webutils.model.BaseEntity;
import org.sers.webutils.model.Gender;
import org.sers.webutils.model.security.User;

@Entity
@Table(name = "members")
@Inheritance(strategy = InheritanceType.JOINED)
public class Member extends BaseEntity {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

   private User userAccount;
    private String profileImageUrl;
    private String location;
    private String bioInformation;
    private AccountStatus accountStatus=AccountStatus.Created;
    private String twitterHandle;
    private String facebookUsername;
    private String website;
    private LookUpValue profession;
    private String clearTextPassword;
    private String emailAddress;
    private String lastName;
    private String firstName;
    private Gender gender;
    private String phoneNumber;
    private String lastEmailVerificationCode;
    private String lastPhoneVerificationCode;
    

    @OneToOne
    @JoinColumn(name = "user_id")
    public User getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(User userAccount) {
        this.userAccount = userAccount;
    }

    @Column(name = "location")
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

  

    @Enumerated(EnumType.STRING)
    @Column(name = "accountStatus",length = 20)
    public AccountStatus getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(AccountStatus accountStatus) {
        this.accountStatus = accountStatus;
    }

     @Column(name = "twitter_handle",length = 50)
    public String getTwitterHandle() {
        return twitterHandle;
    }

    public void setTwitterHandle(String twitterHandle) {
        this.twitterHandle = twitterHandle;
    }

     @Column(name = "facebook_username",length = 50)
    public String getFacebookUsername() {
        return facebookUsername;
    }

    public void setFacebookUsername(String facebookUsername) {
        this.facebookUsername = facebookUsername;
    }

     @Column(name = "website",length = 200)
    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

     @Column(name = "bio",columnDefinition = "TEXT")
    public String getBioInformation() {
        return bioInformation;
    }

    public void setBioInformation(String bioInformation) {
        this.bioInformation = bioInformation;
    }

     @Column(name = "profession")
    public LookUpValue getProfession() {
        return profession;
    }

    public void setProfession(LookUpValue profession) {
        this.profession = profession;
    }

     @Column(name = "password")
    public String getClearTextPassword() {
        return clearTextPassword;
    }

    public void setClearTextPassword(String clearTextPassword) {
        this.clearTextPassword = clearTextPassword;
    }

     @Column(name = "email_address")
    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

     @Column(name = "last_name")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

     @Column(name = "first_name")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Enumerated(EnumType.ORDINAL)
     @Column(name = "gender")
    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

     @Column(name = "phone_number")
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

     @Column(name = "last_email_verification_code")
    public String getLastEmailVerificationCode() {
        return lastEmailVerificationCode;
    }

    public void setLastEmailVerificationCode(String lastEmailVerificationCode) {
        this.lastEmailVerificationCode = lastEmailVerificationCode;
    }

     @Column(name = "last_phone_verification_code")
    public String getLastPhoneVerificationCode() {
        return lastPhoneVerificationCode;
    }

    public void setLastPhoneVerificationCode(String lastPhoneVerificationCode) {
        this.lastPhoneVerificationCode = lastPhoneVerificationCode;
    }
    
    
    
    
    
    
     @Override
    public String toString() {
        return this.userAccount.toString();
    }

   
    @Override
    public boolean equals(Object object) {
        return object instanceof Member && (super.getId() != null) ? super.getId().equals(((Member) object).getId())
                : (object == this);
    }

    @Override
    public int hashCode() {
        return super.getId() != null ? this.getClass().hashCode() + super.getId().hashCode() : super.hashCode();
    }
}
