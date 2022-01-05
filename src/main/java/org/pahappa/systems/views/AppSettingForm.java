package org.pahappa.systems.views;

import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.pahappa.systems.core.services.SystemSettingService;
import org.pahappa.systems.core.utils.EmailService;
import org.pahappa.systems.core.utils.UiUtils;
import org.pahappa.systems.models.SystemSetting;
import org.pahappa.systems.security.HyperLinks;
import org.sers.webutils.client.views.presenters.ViewPath;
import org.sers.webutils.client.views.presenters.WebFormView;
import org.sers.webutils.model.exception.OperationFailedException;
import org.sers.webutils.model.exception.ValidationFailedException;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;

@ManagedBean(name = "appSettingForm")
@ViewScoped
@ViewPath(path = HyperLinks.SETTING_FORM)
public class AppSettingForm extends WebFormView<SystemSetting, AppSettingForm, AppSettingForm> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private SystemSettingService defaultSettingService;

    private boolean showPrice = true;
    private boolean flutterLive;
    private String testEmail="raymond@pahappa.com";

    private String testMessage = "This is a test email from CRAMS";

    @Override
    public void beanInit() {
        this.defaultSettingService = ApplicationContextProvider.getBean(SystemSettingService.class);
        super.model = this.defaultSettingService.getAppSetting();

        if (this.model == null) {
            this.model = new SystemSetting();
        } else {
            super.model = this.defaultSettingService.getAppSetting();
        }

    }

    @Override
    public void pageLoadInit() {
        super.model = this.defaultSettingService.getAppSetting();
    }

    @Override
    public void persist() throws Exception {
        System.out.println("-----------Saving setting------");
        this.defaultSettingService.saveInstance(super.model);
        UiUtils.showMessageBox("Default settings updated", "Action Successful");
    }

    public void saveSettings() throws ValidationFailedException, OperationFailedException {
        System.out.println("-----------Saving setting------");
        this.defaultSettingService.saveInstance(super.model);
        UiUtils.showMessageBox("Default settings updated", "Action Successful");

    }

    @Override
    public String getViewUrl() {
        return HyperLinks.SETTING_FORM;
    }

    public void hidePrice() {
        this.showPrice = false;
    }

    @Override
    public void resetModal() {
        super.resetModal();
        super.model = new SystemSetting();
    }

    @Override
    public void setFormProperties() {
        super.setFormProperties();
    }

    public void sendTestEmail() {

        try {

            new EmailService().sendMail(this.testEmail, this.testMessage, "Sent this test email from RGW web on " + new Date());

        } catch (Exception ex) {
            UiUtils.ComposeFailure("Action failed", ex.getLocalizedMessage());
            ex.printStackTrace();
        }

    }

    public boolean isShowPrice() {
        return showPrice;
    }

    public void setShowPrice(boolean showPrice) {
        this.showPrice = showPrice;
    }

    public String getTestEmail() {
        return testEmail;
    }

    public void setTestEmail(String testEmail) {
        this.testEmail = testEmail;
    }

    public String getTestMessage() {
        return testMessage;
    }

    public void setTestMessage(String testMessage) {
        this.testMessage = testMessage;
    }

    public boolean isFlutterLive() {
        return flutterLive;
    }

    public void setFlutterLive(boolean flutterLive) {
        this.flutterLive = flutterLive;
    }

}
