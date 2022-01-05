package org.pahappa.systems.views;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.pahappa.systems.core.utils.CustomSearchUtils;
import org.pahappa.systems.security.HyperLinks;
import org.sers.webutils.client.views.presenters.PaginatedTableView;
import org.sers.webutils.client.views.presenters.ViewPath;
import org.sers.webutils.model.security.User;
import org.sers.webutils.server.core.service.UserService;
import org.sers.webutils.server.core.service.excel.reports.ExcelReport;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;
import org.sers.webutils.server.shared.SharedAppData;

@ManagedBean
@SessionScoped
@ViewPath(path = HyperLinks.USER_VIEW)
public class UsersView extends PaginatedTableView<User, UsersView, UsersView> {

	private static final long serialVersionUID = 1L;
	private UserService userService;
	private String searchTerm;

	@PostConstruct
	public void init() {
		this.userService = ApplicationContextProvider.getApplicationContext().getBean(UserService.class);
		super.setMaximumresultsPerpage(10);
	}

	@Override
	public void reloadFromDB(int offset, int limit, Map<String, Object> filters) throws Exception {
		if(SharedAppData.getLoggedInUser() != null && !SharedAppData.getLoggedInUser().hasAdministrativePrivileges()) {
			super.setDataModels(userService.getUsers(CustomSearchUtils.genereateSearchObjectForUsers(this.searchTerm, null), offset, limit));
		}
	}

	@Override
	public void reloadFilterReset() {
		if(SharedAppData.getLoggedInUser() != null && !SharedAppData.getLoggedInUser().hasAdministrativePrivileges()) {
			super.setTotalRecords(
					this.userService.countUsers(CustomSearchUtils.genereateSearchObjectForUsers(this.searchTerm, null)));
			this.resetInput();
		}
	}

	@Override
	public List<ExcelReport> getExcelReportModels() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getFileName() {
		// TODO Auto-generated method stub
		return null;
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
	
}