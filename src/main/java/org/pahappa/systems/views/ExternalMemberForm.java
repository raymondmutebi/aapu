package org.pahappa.systems.views;

import com.googlecode.genericdao.search.Search;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.pahappa.systems.constants.AccountStatus;
import org.pahappa.systems.constants.Region;
import org.pahappa.systems.constants.TemplateType;
import org.pahappa.systems.core.services.EmailTemplateService;

import org.pahappa.systems.security.HyperLinks;
import org.sers.webutils.client.views.presenters.ViewPath;
import org.sers.webutils.client.views.presenters.WebFormView;
import org.sers.webutils.model.Gender;
import org.sers.webutils.model.RecordStatus;
import org.sers.webutils.model.exception.ValidationFailedException;
import org.sers.webutils.server.core.service.UserService;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;
import org.pahappa.systems.core.services.MemberService;
import org.pahappa.systems.core.services.PaymentService;
import org.pahappa.systems.core.services.SystemSettingService;
import org.pahappa.systems.core.utils.AppUtils;
import org.pahappa.systems.core.utils.EmailService;
import org.pahappa.systems.core.utils.UiUtils;
import org.pahappa.systems.models.EmailTemplate;
import org.pahappa.systems.models.LookUpValue;
import org.pahappa.systems.models.Member;
import org.pahappa.systems.models.ProfessionValue;
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
    private List<LookUpValue> professions;
    private List<Region> regions;

    private String customUiMessage;
    private String verificationCode;
    private boolean successResponse = true;
    private String paymentPhoneNumber;
    private boolean showForm = true;
    private String paymentUrl;
    private boolean showCodeSection, showPaymentSection, showSuccessMessageSection;

    @Override
    public void beanInit() {
        super.model = new Member();
        this.userService = ApplicationContextProvider.getBean(UserService.class);
        this.contactService = ApplicationContextProvider.getBean(MemberService.class);
        this.systemSettingService = ApplicationContextProvider.getBean(SystemSettingService.class);
        this.genders = Arrays.asList(Gender.values());
           this.professions = new ArrayList<>(ApplicationContextProvider.getBean(SystemSettingService.class).getAppSetting().getProfessional().getLookUpValues());
     
        this.regions = Arrays.asList(Region.values());
        super.model = new Member();
    }

    @Override
    public void pageLoadInit() {
        // TODO Auto-generated method stub
        // super.model = new Member();

    }

    @Override
    public void persist() throws Exception {
        super.model.setAccountStatus(AccountStatus.Created);
        this.contactService.saveOutsideContext(super.model);
        super.model = new Member();
        resetModal();
        UiUtils.showMessageBox("Thank you for regisering with UMA, check " + super.model.getEmailAddress() + " inbox for login credentials", "Registration successfull");

        // createDefaultUser(super.model);
    }

    public void createMember() {
        try {
            EmailTemplate emailTemplate = ApplicationContextProvider.getBean(EmailTemplateService.class)
                    .getEmailTemplateByType(TemplateType.EMAIL_VERIFICATION);
            if (emailTemplate != null) {
                String code = AppUtils.generateOTP(6);
                super.model.setLastEmailVerificationCode(code);
                super.model.setAccountStatus(AccountStatus.Created);
                super.model = this.contactService.saveOutsideContext(super.model);

                String html = emailTemplate.getTemplate();

                html = html.replace("{fullName}", super.model.composeFullName());
                html = html.replace("{code}", code);

                new EmailService().sendMail(super.model.getEmailAddress(), "AAPU verify email address", html);
                showCodeForm();
                customUiMessage = "Details saved, check your email ";
                successResponse = true;
                PrimeFaces.current().ajax().update("externalMemberForm");
            } else {
                customUiMessage = "No email templates set";
                successResponse = false;
                PrimeFaces.current().ajax().update("externalMemberForm");
            }
        } catch (Exception ex) {
            customUiMessage = "Ops, some error occured\n " + ex.getLocalizedMessage();
            successResponse = false;
            Logger.getLogger(ExternalMemberForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void verifyCode() {
        try {
            if (verificationCode.equalsIgnoreCase(super.model.getLastEmailVerificationCode())) {

                super.model.setAccountStatus(AccountStatus.Verified);
                super.model = this.contactService.saveOutsideContext(super.model);
                makePayment();
                showPaymentSection();
                customUiMessage = "Succeess";
                successResponse = true;

                PrimeFaces.current().ajax().update("externalMemberForm");
            } else {

                customUiMessage = "Invalid code ";
                successResponse = false;

            }
            verificationCode = null;
        } catch (ValidationFailedException ex) {
            customUiMessage = "Ops, some error occured\n " + ex.getLocalizedMessage();
            successResponse = false;
            Logger.getLogger(ExternalMemberForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void makePayment() {

        try {
            
            paymentUrl = ApplicationContextProvider.getBean(PaymentService.class).initiateRegistrationFeePayment(super.model);

            System.out.println("Generated url ==>" + paymentUrl);

            if (paymentUrl != null) {
                this.successResponse = true;
            }
        } catch (Exception ex) {
            customUiMessage = "Ops, some error occured\n " + ex.getLocalizedMessage();
            successResponse = false;
            Logger.getLogger(ExternalMemberForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void showRegForm() {
        this.showForm = true;
        this.showCodeSection = false;
        this.showPaymentSection = false;
        PrimeFaces.current().ajax().update("externalMemberForm");

    }

    public void showCodeForm() {
        this.showForm = false;
        this.showCodeSection = true;
        this.showPaymentSection = false;
        PrimeFaces.current().ajax().update("externalMemberForm");

    }

    public void showPaymentSection() {
        this.showForm = false;
        this.showCodeSection = false;
        this.showPaymentSection = true;
        PrimeFaces.current().ajax().update("externalMemberForm");

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

    public List<LookUpValue> getProfessions() {
        return professions;
    }

    public void setProfessions(List<LookUpValue> professions) {
        this.professions = professions;
    }

    public List<Region> getRegions() {
        return regions;
    }

    public void setRegions(List<Region> regions) {
        this.regions = regions;
    }

    public String getPaymentUrl() {
        return paymentUrl;
    }

    public void setPaymentUrl(String paymentUrl) {
        this.paymentUrl = paymentUrl;
    }

}
