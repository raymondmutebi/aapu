package org.pahappa.systems.views;

import com.googlecode.genericdao.search.Search;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.pahappa.systems.constants.AccountStatus;

import org.pahappa.systems.security.HyperLinks;
import org.primefaces.context.RequestContext;
import org.sers.webutils.client.utils.UiUtils;
import org.sers.webutils.client.views.presenters.ViewPath;
import org.sers.webutils.client.views.presenters.WebFormView;
import org.sers.webutils.model.Gender;
import org.sers.webutils.model.RecordStatus;
import org.sers.webutils.model.exception.ValidationFailedException;
import org.sers.webutils.server.core.service.UserService;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;
import org.pahappa.systems.core.services.MemberService;
import org.pahappa.systems.core.services.SystemSettingService;
import org.pahappa.systems.core.utils.AppUtils;
import org.pahappa.systems.models.LookUpValue;
import org.pahappa.systems.models.Member;
import org.primefaces.PrimeFaces;

@ManagedBean(name = "externalMemberForm")
@ViewScoped
@ViewPath(path = HyperLinks.EXTERNAL_MEMBER_FORM)
public class ExternalMemberForm extends WebFormView<Member, ExternalMemberForm, ExternalMemberForm> {

    private static final long serialVersionUID = 1L;
    private MemberService contactService;
    Search search = new Search().addFilterEqual("recordStatus", RecordStatus.ACTIVE);

    private UserService userService;
    private SystemSettingService systemSettingService;
    private List<Gender> genders;

    private List<LookUpValue> professionals;
    private String customUiMessage;
    private String verificationCode;
    private boolean successResponse = true;
    private String paymentPhoneNumber;
    private boolean showForm=true;
    private boolean showCodeSection, showPaymentSection, showSuccessMessageSection;

    @Override
    public void beanInit() {
        super.model = new Member();
        this.userService = ApplicationContextProvider.getBean(UserService.class);
        this.contactService = ApplicationContextProvider.getBean(MemberService.class);
    }

    @Override
    public void pageLoadInit() {
        // TODO Auto-generated method stub
        super.model = new Member();
        this.professionals = new ArrayList<>(this.systemSettingService.getAppSetting().getProfessional().getLookUpValues());
        this.genders = Arrays.asList(Gender.values());
    }

    @Override
    public void persist() throws Exception {
        super.model.setAccountStatus(AccountStatus.Created);
        this.contactService.saveOutsideContext(super.model);
        super.model = new Member();
        resetModal();
        UiUtils.showMessageBox("Thank you for regisering with UMA, check " + super.model.getEmailAddress() + " inbox for login credentials", "Registration successfull", RequestContext.getCurrentInstance());

        // createDefaultUser(super.model);
    }

    public void createMember() {
        try {
            String code = String.valueOf(new Random(6).nextInt());
            super.model.setLastEmailVerificationCode(code);
            super.model.setAccountStatus(AccountStatus.Created);
            super.model = this.contactService.saveOutsideContext(super.model);

            AppUtils.sendEmail(super.model.getEmailAddress(), "AAPU registartion", "Confirm your email address with this code\n ");
            this.showCodeSection = true;
            this.showForm = false;
            this.showPaymentSection = false;
            customUiMessage = "Details saved, chech you email ";
            PrimeFaces.current().ajax().update("externalMemberForm");
        } catch (ValidationFailedException ex) {
            customUiMessage = "Ops, some error occured\n " + ex.getLocalizedMessage();
            Logger.getLogger(ExternalMemberForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void verifyCode() {
        try {
            if (verificationCode.equalsIgnoreCase(super.model.getLastEmailVerificationCode())) {

                super.model.setAccountStatus(AccountStatus.Verified);
                super.model = this.contactService.saveOutsideContext(super.model);
                this.showCodeSection = true;
                this.showForm = false;
                this.showPaymentSection = true;
                PrimeFaces.current().ajax().update("externalMemberForm");
            } else {

            }
        } catch (ValidationFailedException ex) {
            customUiMessage = "Ops, some error occured\n " + ex.getLocalizedMessage();
            Logger.getLogger(ExternalMemberForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void makePayment() {
        try {
            if (verificationCode.equalsIgnoreCase(super.model.getLastEmailVerificationCode())) {

                super.model.setAccountStatus(AccountStatus.Verified);
                this.contactService.saveOutsideContext(super.model);
                this.showCodeSection = true;
                this.showForm = false;
                this.showPaymentSection = true;
                PrimeFaces.current().ajax().update("externalMemberForm");
            } else {

            }
        } catch (ValidationFailedException ex) {
            customUiMessage = "Ops, some error occured\n " + ex.getLocalizedMessage();
            Logger.getLogger(ExternalMemberForm.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void resetModal() {
        super.resetModal();
        super.model = new Member();
    }

    public void setFormProperties() {
        super.setFormProperties();
    }

    @Override
    public String getViewUrl() {
        // TODO Auto-generated method stub
        return null;
    }

    public List<Gender> getGenders() {
        return genders;
    }

    public void setGenders(List<Gender> genders) {
        this.genders = genders;
    }

    public List<LookUpValue> getProfessionals() {
        return professionals;
    }

    public void setProfessionals(List<LookUpValue> professionals) {
        this.professionals = professionals;
    }

    public String getCustomUiMessage() {
        return customUiMessage;
    }

    public void setCustomUiMessage(String customUiMessage) {
        this.customUiMessage = customUiMessage;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public boolean isSuccessResponse() {
        return successResponse;
    }

    public void setSuccessResponse(boolean successResponse) {
        this.successResponse = successResponse;
    }

    public boolean isShowForm() {
        return showForm;
    }

    public void setShowForm(boolean showForm) {
        this.showForm = showForm;
    }

    public boolean isShowCodeSection() {
        return showCodeSection;
    }

    public void setShowCodeSection(boolean showCodeSection) {
        this.showCodeSection = showCodeSection;
    }

    public boolean isShowPaymentSection() {
        return showPaymentSection;
    }

    public void setShowPaymentSection(boolean showPaymentSection) {
        this.showPaymentSection = showPaymentSection;
    }

    public String getPaymentPhoneNumber() {
        return paymentPhoneNumber;
    }

    public void setPaymentPhoneNumber(String paymentPhoneNumber) {
        this.paymentPhoneNumber = paymentPhoneNumber;
    }

    public boolean isShowSuccessMessageSection() {
        return showSuccessMessageSection;
    }

    public void setShowSuccessMessageSection(boolean showSuccessMessageSection) {
        this.showSuccessMessageSection = showSuccessMessageSection;
    }
    
    

}
