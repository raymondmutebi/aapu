package org.pahappa.systems.views;

import com.googlecode.genericdao.search.Search;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.pahappa.systems.core.services.LookUpService;

import org.pahappa.systems.models.SystemSetting;
import org.pahappa.systems.security.HyperLinks;
import org.primefaces.context.RequestContext;
import org.sers.webutils.client.utils.UiUtils;
import org.sers.webutils.client.views.presenters.ViewPath;
import org.sers.webutils.client.views.presenters.WebFormView;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;
import org.pahappa.systems.core.services.SystemSettingService;
import org.pahappa.systems.core.utils.EmailClient;
import org.pahappa.systems.models.LookUpField;
import org.sers.webutils.model.RecordStatus;
import org.sers.webutils.server.shared.SharedAppData;

@ManagedBean(name = "appSettingForm")
@SessionScoped
@ViewPath(path = HyperLinks.SETTING_FORM)
public class AppSettingForm extends WebFormView<SystemSetting, AppSettingForm, Dashboard> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private SystemSettingService appSettingService;
    private List<LookUpField> lookupFields;

    @Override
    public void beanInit() {
        this.appSettingService = ApplicationContextProvider.getBean(SystemSettingService.class);
        if (this.appSettingService.getAppSetting() != null) {
            super.model = this.appSettingService.getAppSetting();
        }
    }

    @Override
    public void pageLoadInit() {
        if (this.appSettingService.getAppSetting() != null) {
            super.model = this.appSettingService.getAppSetting();
        }
        this.lookupFields = ApplicationContextProvider.getBean(LookUpService.class).getInstances(new Search().addFilterEqual("recordStatus", RecordStatus.ACTIVE), 0, 0);
    }

    @Override
    public void persist() throws Exception {
        this.appSettingService.save(super.model);
        UiUtils.showMessageBox("App settings updated", "Action Successful", RequestContext.getCurrentInstance());
    }

    @Override
    public void resetModal() {
        super.resetModal();
        super.model = new SystemSetting();
    }

    public void sendTestEmail() {

        try {
            new EmailClient().sendSimpleMessage(SharedAppData.getLoggedInUser().getEmailAddress(), "CRM account creation", "This is a text email");
        } catch (Exception ex) {
            Logger.getLogger(AppSettingForm.class.getName()).log(Level.SEVERE, null, ex);
       
        }

    }

    @Override
    public void setFormProperties() {
        super.setFormProperties();
    }

    @Override
    public String getViewUrl() {
        return this.getViewPath();
    }

    public SystemSettingService getAppSettingService() {
        return appSettingService;
    }

    public void setAppSettingService(SystemSettingService appSettingService) {
        this.appSettingService = appSettingService;
    }

}
