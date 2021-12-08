package org.pahappa.systems.views;


import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.*;

import org.pahappa.systems.core.services.EmailTemplateService;
import org.pahappa.systems.models.EmailTemplate;
import org.pahappa.systems.security.HyperLinks;
import org.sers.webutils.client.views.presenters.PaginatedTableView;
import org.sers.webutils.client.views.presenters.ViewPath;
import org.sers.webutils.model.utils.SortField;
import org.sers.webutils.server.core.service.excel.reports.ExcelReport;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;

@ManagedBean
@ViewScoped
@ViewPath(path = HyperLinks.MAIL_TEMPLATE_VIEWS)
public class EmailTemplateView extends PaginatedTableView<EmailTemplate, EmailTemplateView, EmailTemplateView>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<EmailTemplate> filteredTemplate;
	private EmailTemplateService emailTemplateService;
	
	private String searchTerm;
	private Date createdFrom;
	private Date createdTo;
	private SortField sortField = new SortField("dateCreated", "dateCreated", true);

	@PostConstruct
	public void init() {
		this.emailTemplateService = ApplicationContextProvider.getApplicationContext().getBean(EmailTemplateService.class);
		super.setMaximumresultsPerpage(10);
	}
	
	@Override
	public String getFileName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void reloadFromDB(int arg0, int arg1, Map<String, Object> arg2) throws Exception {
		super.setDataModels(emailTemplateService.getTemplates());
		
	}
	
	@Override
	public void reloadFilterReset() throws Exception {
		super.setTotalRecords(emailTemplateService.countTemplates());
		try {
			super.reloadFilterReset();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<ExcelReport> getExcelReportModels() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<EmailTemplate> getFilteredTemplate() {
		return filteredTemplate;
	}

	public void setFilteredTemplate(List<EmailTemplate> filteredTemplate) {
		this.filteredTemplate = filteredTemplate;
	}

	public String getSearchTerm() {
		return searchTerm;
	}

	public Date getCreatedFrom() {
		return createdFrom;
	}

	public Date getCreatedTo() {
		return createdTo;
	}

	public SortField getSortField() {
		return sortField;
	}

	public void setSearchTerm(String searchTerm) {
		this.searchTerm = searchTerm;
	}

	public void setCreatedFrom(Date createdFrom) {
		this.createdFrom = createdFrom;
	}

	public void setCreatedTo(Date createdTo) {
		this.createdTo = createdTo;
	}

	public void setSortField(SortField sortField) {
		this.sortField = sortField;
	}
}
