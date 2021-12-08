package org.pahappa.systems.views.render;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;


import org.pahappa.systems.models.security.PermissionConstants;
import org.sers.webutils.model.security.User;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;
import org.sers.webutils.server.shared.SharedAppData;
import org.pahappa.systems.core.services.SystemSettingService;
import org.pahappa.systems.models.SystemSetting;

@ManagedBean(name = "componentRenderer")
@SessionScoped
public class ComponentRenderer implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private boolean administrator = false;
    private boolean companyPerm = false;
    private boolean dashboardsPerm = false;
    private User loggedInUser;
    private SystemSetting appSetting = new SystemSetting();
    private SystemSettingService appSettingService;

    @PostConstruct
    public void init() {
        this.administrator = SharedAppData.getLoggedInUser().hasAdministrativePrivileges();
        this.companyPerm = SharedAppData.getLoggedInUser()
                .hasPermission(PermissionConstants.PERM_MANAGE_COMPANIES);

        this.dashboardsPerm = SharedAppData.getLoggedInUser()
                .hasPermission(PermissionConstants.PERM_VIEW_DASHBOARD);

        this.loggedInUser = SharedAppData.getLoggedInUser();

       this.appSettingService = ApplicationContextProvider.getBean(SystemSettingService.class);

        

        if (this.appSettingService.getAppSetting() != null) {
            this.appSetting = this.appSettingService.getAppSetting();
        }
    }

    /**
     * @return the loggedInUser
     */
    public User getLoggedInUser() {
        return loggedInUser;
    }

    /**
     * @param loggedInUser the loggedInUser to set
     */
    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    /**
     * @return the administrator
     */
    public boolean isAdministrator() {
        return administrator;
    }

    /**
     * @param administrator the administrator to set
     */
    public void setAdministrator(boolean administrator) {
        this.administrator = administrator;
    }

    public boolean isCompanyPerm() {
        return companyPerm;
    }

    public void setCompanyPerm(boolean companyPerm) {
        this.companyPerm = companyPerm;
    }

    /**
     * @return the dashboardsPerm
     */
    public boolean isDashboardsPerm() {
        return dashboardsPerm;
    }

    /**
     * @param dashboardsPerm the dashboardsPerm to set
     */
    public void setDashboardsPerm(boolean dashboardsPerm) {
        this.dashboardsPerm = dashboardsPerm;
    }

 

    /**
     * @return the appSetting
     */
    public SystemSetting getAppSetting() {
        return appSetting;
    }

    /**
     * @param appSetting the appSetting to set
     */
    public void setAppSetting(SystemSetting appSetting) {
        this.appSetting = appSetting;
    }

}
