package org.pahappa.systems.views;

import com.google.common.collect.Sets;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.pahappa.systems.security.HyperLinks;
import org.sers.webutils.client.views.presenters.PaginatedTableView;
import org.sers.webutils.client.views.presenters.ViewPath;
import org.sers.webutils.model.exception.OperationFailedException;
import org.sers.webutils.model.exception.ValidationFailedException;
import org.sers.webutils.model.security.Permission;
import org.sers.webutils.model.security.Role;
import org.sers.webutils.server.core.dao.RoleDao;
import org.sers.webutils.server.core.service.PermissionService;
import org.sers.webutils.server.core.service.RoleService;
import org.sers.webutils.server.core.service.excel.reports.ExcelReport;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;
import org.sers.webutils.server.shared.SharedAppData;
import org.springframework.beans.factory.annotation.Autowired;

@ManagedBean(name = "rolesView")
@SessionScoped
@ViewPath(path = HyperLinks.ROLES_VIEW)
public class RolesView extends PaginatedTableView<Role, RolesView, RolesView> {
    
    private static final long serialVersionUID = 1L;
    private RoleService roleService;
    private PermissionService permissionService;
    private String searchTerm;
    private Role selectedRole;
    private Set<Permission> permissionsList = new HashSet<>();
    private Set<Permission> selectedPermissionsList = new HashSet<>();
    
    @Autowired
    RoleDao roleDao;
    
    @PostConstruct
    public void init() {
        this.roleService = ApplicationContextProvider.getApplicationContext().getBean(RoleService.class);
        this.permissionService = ApplicationContextProvider.getApplicationContext().getBean(PermissionService.class);
        this.permissionsList = Sets.newHashSet(permissionService.getPermissions());
        super.setMaximumresultsPerpage(10);
    }
    
    @Override
    public void reloadFromDB(int offset, int limit, Map<String, Object> filters) throws Exception {
        if (SharedAppData.getLoggedInUser() != null && SharedAppData.getLoggedInUser().hasAdministrativePrivileges()) {
            super.setDataModels(roleService.getRoles());
            
        }
    }
    
    @Override
    public void reloadFilterReset() {
        if (SharedAppData.getLoggedInUser() != null && SharedAppData.getLoggedInUser().hasAdministrativePrivileges()) {
            super.setTotalRecords(
                    this.roleService.getRoles().size());
            this.resetInput();
        }
    }
    
    @Override
    public List<ExcelReport> getExcelReportModels() {
       
        return null;
    }
    
    @Override
    public String getFileName() {
       
        return null;
    }
    
    public void saveSelectedRole() throws ValidationFailedException {
        System.err.println("------saving role---------" + this.selectedRole);
        
        this.selectedRole.setPermissions(this.selectedPermissionsList);
        roleService.saveRole(this.selectedRole);
    }
    
    public void deleteSelectedRole(Role role) throws OperationFailedException {
        roleService.deleteRole(role);
    }
    
    public void loadSelectedRole(Role role) {
        if (role != null) {
            System.err.println("------fetched roles---------" + role.getPermissions());
            this.selectedRole = role;
            this.selectedPermissionsList = new HashSet<Permission>(role.getPermissions());
            
        } else {
            System.err.println("------New role---------");
            this.selectedPermissionsList = new HashSet<Permission>();
            this.selectedRole = new Role();
        }
        
    }
    
    public String getSearchTerm() {
        return searchTerm;
    }
    
    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }
    
    private void resetInput() {
        this.searchTerm = "";
    }
    
    public Role getSelectedRole() {
        return selectedRole;
    }
    
    public void setSelectedRole(Role selectedRole) {
        this.selectedRole = selectedRole;
    }
    
    public Set<Permission> getPermissionsList() {
        return permissionsList;
    }
    
    public void setPermissionsList(Set<Permission> permissionsList) {
        this.permissionsList = permissionsList;
    }
    
    public Set<Permission> getSelectedPermissionsList() {
        return selectedPermissionsList;
    }
    
    public void setSelectedPermissionsList(Set<Permission> selectedPermissionsList) {
        this.selectedPermissionsList = selectedPermissionsList;
    }
    
}
