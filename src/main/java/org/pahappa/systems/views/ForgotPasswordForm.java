package org.pahappa.systems.views;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.apache.commons.lang3.StringUtils;
import org.pahappa.systems.constants.AccountStatus;
import org.pahappa.systems.constants.TemplateType;
import org.pahappa.systems.core.services.EmailTemplateService;

import org.pahappa.systems.security.HyperLinks;
import org.sers.webutils.client.views.presenters.ViewPath;
import org.sers.webutils.client.views.presenters.WebFormView;
import org.sers.webutils.server.core.service.UserService;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;
import org.pahappa.systems.core.services.MemberService;
import org.pahappa.systems.core.utils.AppUtils;
import org.pahappa.systems.core.utils.EmailService;
import org.pahappa.systems.core.utils.UiUtils;
import org.pahappa.systems.models.EmailTemplate;
import org.pahappa.systems.models.Member;
import org.pahappa.systems.security.LoginController;
import org.primefaces.PrimeFaces;

@ManagedBean(name = "forgotPasswordForm")
@ViewScoped
@ViewPath(path = HyperLinks.FORGOT_PASSWORD_FORM)
public class ForgotPasswordForm extends WebFormView<Member, ForgotPasswordForm, LoginController> {

    private static final long serialVersionUID = 1L;
    private MemberService memberService;

    private UserService userService;

    private String email, customUiMessage;
    private String verificationCode, newPassword, confirmedNePassword;
    private boolean successResponse = true;
    private Member member;
    private boolean showCodeSection, showEmailsection, showPasswordSection, showSuccessSection;

    @Override
    public void beanInit() {
        member = new Member();
        this.showEmailsection=true;
        this.userService = ApplicationContextProvider.getBean(UserService.class);
        this.memberService = ApplicationContextProvider.getBean(MemberService.class);

        member = new Member();
    }

    @Override
    public void pageLoadInit() {

    }

    @Override
    public void persist() throws Exception {
        member.setAccountStatus(AccountStatus.Created);
        this.memberService.saveOutsideContext(member);
        member = new Member();
        resetModal();
        UiUtils.showMessageBox("Thank you for regisering with UMA, check " + member.getEmailAddress() + " inbox for login credentials", "Registration successfull");

        // createDefaultUser(member);
    }

    public void fetchMember() {
        try {

            this.member = memberService.getMemberByEmail(email);
            if (member == null) {
                customUiMessage = "No member found";
                successResponse = false;
                PrimeFaces.current().ajax().update("forgotPasswordForm");
                return;
            }

            EmailTemplate emailTemplate = ApplicationContextProvider.getBean(EmailTemplateService.class)
                    .getEmailTemplateByType(TemplateType.RESET_PASSWORD);
            if (emailTemplate != null) {
                String code = AppUtils.generateOTP(6);
                member.setLastEmailVerificationCode(code);
                member = this.memberService.saveOutsideContext(member);

                String html = emailTemplate.getTemplate();

                html = html.replace("{fullName}", member.composeFullName());
                html = html.replace("{code}", code);

                new EmailService().sendMail(member.getEmailAddress(), "AAPU reset password", html);

                customUiMessage = "Enter code sent to your email";
                successResponse = true;
                showCodeForm();

            } else {
                customUiMessage = "No email templates set";
                successResponse = false;

            }
        } catch (Exception ex) {
            customUiMessage = "Ops, some error occured\n " + ex.getLocalizedMessage();
            successResponse = false;
            Logger.getLogger(ForgotPasswordForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        PrimeFaces.current().ajax().update("forgotPasswordForm");
    }

    public void verifyCode() {
        if (verificationCode.equalsIgnoreCase(member.getLastEmailVerificationCode())) {

            customUiMessage = "Succeess";
            successResponse = true;
            showPasswordSection();

        } else {

            customUiMessage = "Invalid code ";
            successResponse = false;

        }
        PrimeFaces.current().ajax().update("forgotPasswordForm");
        verificationCode = null;
    }

    public void changePassword() {

        if (StringUtils.isNotBlank( newPassword)|| !newPassword.equals(confirmedNePassword)) {
            customUiMessage = "Passwords dont match ";
            successResponse = false;
            PrimeFaces.current().ajax().update("forgotPasswordForm");
            return;
        }

        try {
            ApplicationContextProvider.getBean(UserService.class).changePassword(newPassword, member.getUserAccount());
           showSuccessSection();
        } catch (Exception ex) {
            customUiMessage = "Ops, some error occured\n " + ex.getLocalizedMessage();
            successResponse = false;
            Logger.getLogger(ForgotPasswordForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        PrimeFaces.current().ajax().update("forgotPasswordForm");
    }

    public void showEmailForm() {
        this.showEmailsection = true;
        this.showCodeSection = false;
        this.showPasswordSection = false;
        this.showSuccessSection = false;
        PrimeFaces.current().ajax().update("forgotPasswordForm");

    }

    public void showCodeForm() {
        this.showEmailsection = false;
        this.showCodeSection = true;
        this.showPasswordSection = false;
        this.showSuccessSection = false;
        PrimeFaces.current().ajax().update("forgotPasswordForm");

    }

    public void showPasswordSection() {
        this.showEmailsection = false;
        this.showCodeSection = false;
        this.showPasswordSection = true;
        this.showSuccessSection = false;
        PrimeFaces.current().ajax().update("forgotPasswordForm");

    }

    public void showSuccessSection() {
        this.showEmailsection = false;
        this.showCodeSection = false;
        this.showPasswordSection = false;
        this.showSuccessSection = true;
        PrimeFaces.current().ajax().update("forgotPasswordForm");

    }
    
    public void doLogin(){
        try {
            redirectToLogin();
        } catch (IOException ex) {
            Logger.getLogger(ForgotPasswordForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void resetModal() {
        super.resetModal();
        member = new Member();
    }

    public void setFormProperties() {
        super.setFormProperties();
    }

    @Override
    public String getViewUrl() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmedNePassword() {
        return confirmedNePassword;
    }

    public void setConfirmedNePassword(String confirmedNePassword) {
        this.confirmedNePassword = confirmedNePassword;
    }

    public boolean isSuccessResponse() {
        return successResponse;
    }

    public void setSuccessResponse(boolean successResponse) {
        this.successResponse = successResponse;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public boolean isShowCodeSection() {
        return showCodeSection;
    }

    public void setShowCodeSection(boolean showCodeSection) {
        this.showCodeSection = showCodeSection;
    }

    public boolean isShowEmailsection() {
        return showEmailsection;
    }

    public void setShowEmailsection(boolean showEmailsection) {
        this.showEmailsection = showEmailsection;
    }

    public boolean isShowPasswordSection() {
        return showPasswordSection;
    }

    public void setShowPasswordSection(boolean showPasswordSection) {
        this.showPasswordSection = showPasswordSection;
    }

    public boolean isShowSuccessSection() {
        return showSuccessSection;
    }

    public void setShowSuccessSection(boolean showSuccessSection) {
        this.showSuccessSection = showSuccessSection;
    }

}
