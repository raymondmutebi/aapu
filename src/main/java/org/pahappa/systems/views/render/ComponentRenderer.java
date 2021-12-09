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

   
    private static final long serialVersionUID = 1L;

    private boolean administrator, manageMembers, manageCommunications, managePayments, memberPerm = false;
    private User loggedInUser;
    private SystemSetting appSetting;

    @PostConstruct
    public void init() {
        loggedInUser = SharedAppData.getLoggedInUser();

        this.appSetting = ApplicationContextProvider.getBean(SystemSettingService.class).getAppSetting();
        this.administrator = loggedInUser.hasAdministrativePrivileges();
        this.manageCommunications = loggedInUser.hasPermission(PermissionConstants.PERM_MANAGE_COMMUNICATIONS);
        this.managePayments = loggedInUser.hasPermission(PermissionConstants.PERM_MANAGE_PAYMENTS);
        this.manageMembers = loggedInUser.hasPermission(PermissionConstants.PERM_MANAGE_MEMBERS);
        this.memberPerm = loggedInUser.hasPermission(PermissionConstants.PERM_MEMBER);

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

    public boolean isManageMembers() {
        return manageMembers;
    }

    public void setManageMembers(boolean manageMembers) {
        this.manageMembers = manageMembers;
    }

    public boolean isManageCommunications() {
        return manageCommunications;
    }

    public void setManageCommunications(boolean manageCommunications) {
        this.manageCommunications = manageCommunications;
    }

    public boolean isManagePayments() {
        return managePayments;
    }

    public void setManagePayments(boolean managePayments) {
        this.managePayments = managePayments;
    }

    public boolean isMemberPerm() {
        return memberPerm;
    }

    public void setMemberPerm(boolean memberPerm) {
        this.memberPerm = memberPerm;
    }
    
    

}
