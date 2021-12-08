package org.pahappa.systems.views;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.pahappa.systems.security.HyperLinks;
import org.primefaces.context.RequestContext;
import org.sers.webutils.client.utils.UiUtils;
import org.sers.webutils.client.views.presenters.PaginatedTableView;
import org.sers.webutils.client.views.presenters.ViewPath;
import org.sers.webutils.model.bgtasks.TaskSchedule;
import org.sers.webutils.model.exception.OperationFailedException;
import org.sers.webutils.server.core.service.BackgroundTaskService;
import org.sers.webutils.server.core.service.excel.reports.ExcelReport;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;
import org.sers.webutils.server.core.utils.SystemCrashHandler;
import org.sers.webutils.server.shared.SharedAppData;

@ManagedBean
@SessionScoped
@ViewPath(path = HyperLinks.TASK_SCHEDULES_VIEW)
public class TaskSchedulesView extends
		PaginatedTableView<TaskSchedule, TaskSchedulesView, Dashboard> {

	private static final long serialVersionUID = 1L;
	private BackgroundTaskService backgroundTaskService;
	private String searchTerm;
	private String active;
	private String executed;
	private String successful;
	private Date from;
	private Date to;

	@PostConstruct
	public void init() {
		this.backgroundTaskService = ApplicationContextProvider
				.getApplicationContext().getBean(BackgroundTaskService.class);
	}

	@Override
	public void reloadFromDB(int offset, int limit, Map<String, Object> filters)
			throws Exception {
		super.setTotalRecords(backgroundTaskService.countScheduledTasks(
				searchTerm, convertToBoolean(active),
				convertToBoolean(executed), convertToBoolean(successful), from,
				to));
		super.setDataModels(backgroundTaskService.getScheduledTasks(searchTerm,
				convertToBoolean(active), convertToBoolean(executed),
				convertToBoolean(successful), from, to, offset, limit));
		this.resetInput();
	}

	public void pause(TaskSchedule taskSchedule) {
		try {
			backgroundTaskService.cancelExecution(taskSchedule);
			UiUtils.showMessageBox(
					"Execution for this task has been CANCELLED.",
					"Action Successful", RequestContext.getCurrentInstance());
		} catch (OperationFailedException e) {
			UiUtils.showMessageBox(e.getMessage(), "Action Failed",
					RequestContext.getCurrentInstance());
			SystemCrashHandler.reportSystemCrash(e,
					SharedAppData.getLoggedInUser());
		}
	}

	public void resume(TaskSchedule taskSchedule) {
		try {
			backgroundTaskService.resumeExecution(taskSchedule);
			UiUtils.showMessageBox("Execution for this task has been RESUMED.",
					"Action Successful", RequestContext.getCurrentInstance());
		} catch (OperationFailedException e) {
			UiUtils.showMessageBox(e.getMessage(), "Action Failed",
					RequestContext.getCurrentInstance());
			SystemCrashHandler.reportSystemCrash(e,
					SharedAppData.getLoggedInUser());
		}
	}

	/**
	 * @return the from
	 */
	public Date getFrom() {
		return from;
	}

	/**
	 * @return the to
	 */
	public Date getTo() {
		return to;
	}

	/**
	 * @return the active
	 */
	public String getActive() {
		return active;
	}

	/**
	 * @return the executed
	 */
	public String getExecuted() {
		return executed;
	}

	/**
	 * @return the successful
	 */
	public String getSuccessful() {
		return successful;
	}

	/**
	 * @param active
	 *            the active to set
	 */
	public void setActive(String active) {
		this.active = active;
	}

	/**
	 * @param executed
	 *            the executed to set
	 */
	public void setExecuted(String executed) {
		this.executed = executed;
	}

	/**
	 * @param successful
	 *            the successful to set
	 */
	public void setSuccessful(String successful) {
		this.successful = successful;
	}

	/**
	 * @param from
	 *            the from to set
	 */
	public void setFrom(Date from) {
		this.from = from;
	}

	/**
	 * @param to
	 *            the to to set
	 */
	public void setTo(Date to) {
		this.to = to;
	}

	/**
	 * @return the searchTerm
	 */
	public String getSearchTerm() {
		return searchTerm;
	}

	/**
	 * @param searchTerm
	 *            the searchTerm to set
	 */
	public void setSearchTerm(String searchTerm) {
		this.searchTerm = searchTerm;
	}

	@Override
	public List<ExcelReport> getExcelReportModels() {
		return null;
	}

	@Override
	public String getFileName() {
		return null;
	}

	public Boolean convertToBoolean(String selected) {
		if (selected == null || selected.isEmpty())
			return null;
		else if (selected.equals("1"))
			return true;
		else if (selected.equals("2"))
			return false;
		else
			return null;
	}
	
	private void resetInput() {
		this.searchTerm = "";
	}
	
}