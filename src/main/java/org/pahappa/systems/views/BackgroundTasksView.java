package org.pahappa.systems.views;

import java.util.Arrays;
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
import org.sers.webutils.model.bgtasks.BackgroundTask;
import org.sers.webutils.model.bgtasks.constants.TaskStatus;
import org.sers.webutils.model.bgtasks.constants.TaskType;
import org.sers.webutils.model.exception.OperationFailedException;
import org.sers.webutils.server.core.service.BackgroundTaskService;
import org.sers.webutils.server.core.service.excel.reports.ExcelReport;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;
import org.sers.webutils.server.core.utils.SystemCrashHandler;
import org.sers.webutils.server.shared.SharedAppData;

@ManagedBean
@SessionScoped
@ViewPath(path = HyperLinks.BACKGROUND_TASKS_VIEW)
public class BackgroundTasksView extends
		PaginatedTableView<BackgroundTask, BackgroundTasksView, Dashboard> {

	private static final long serialVersionUID = 1L;
	private BackgroundTaskService backgroundTaskService;
	private String searchTerm;
	private List<TaskType> taskTypes;
	private List<TaskStatus> taskStatus;
	private List<TaskType> selectedTaskTypes;
	private List<TaskStatus> selectedTaskStatus;

	@PostConstruct
	public void init() {
		this.backgroundTaskService = ApplicationContextProvider
				.getApplicationContext().getBean(BackgroundTaskService.class);
		this.taskTypes = Arrays.asList(TaskType.values());
		this.taskStatus = Arrays.asList(TaskStatus.values());
	}

	@Override
	public void reloadFromDB(int offset, int limit, Map<String, Object> filters)
			throws Exception {
		super.setTotalRecords(backgroundTaskService.countBackgroundTasks(
				searchTerm, selectedTaskTypes, selectedTaskStatus));
		super.setDataModels(backgroundTaskService.getBackgroundTasks(
				searchTerm, selectedTaskTypes, selectedTaskStatus, offset,
				limit));
		this.resetInput();
	}

	public void pause(BackgroundTask backgroundTask) {
		try {
			backgroundTaskService.pauseTask(backgroundTask);
			UiUtils.showMessageBox(
					"Generation of task schedules has been paused.",
					"Action Successful", RequestContext.getCurrentInstance());
		} catch (OperationFailedException e) {
			UiUtils.showMessageBox(e.getMessage(), "Action Failed",
					RequestContext.getCurrentInstance());
			SystemCrashHandler.reportSystemCrash(e,
					SharedAppData.getLoggedInUser());
		}
	}

	public void resume(BackgroundTask backgroundTask) {
		try {
			backgroundTaskService.resumeTask(backgroundTask);
			UiUtils.showMessageBox(
					"Generation of task schedules has been resumed.",
					"Action Successful", RequestContext.getCurrentInstance());
		} catch (OperationFailedException e) {
			UiUtils.showMessageBox(e.getMessage(), "Action Failed",
					RequestContext.getCurrentInstance());
			SystemCrashHandler.reportSystemCrash(e,
					SharedAppData.getLoggedInUser());
		}
	}

	/**
	 * @return the searchTerm
	 */
	public String getSearchTerm() {
		return searchTerm;
	}

	/**
	 * @return the taskTypes
	 */
	public List<TaskType> getTaskTypes() {
		return taskTypes;
	}

	/**
	 * @return the taskStatus
	 */
	public List<TaskStatus> getTaskStatus() {
		return taskStatus;
	}

	/**
	 * @return the selectedTaskTypes
	 */
	public List<TaskType> getSelectedTaskTypes() {
		return selectedTaskTypes;
	}

	/**
	 * @return the selectedTaskStatus
	 */
	public List<TaskStatus> getSelectedTaskStatus() {
		return selectedTaskStatus;
	}

	/**
	 * @param taskTypes
	 *            the taskTypes to set
	 */
	public void setTaskTypes(List<TaskType> taskTypes) {
		this.taskTypes = taskTypes;
	}

	/**
	 * @param taskStatus
	 *            the taskStatus to set
	 */
	public void setTaskStatus(List<TaskStatus> taskStatus) {
		this.taskStatus = taskStatus;
	}

	/**
	 * @param selectedTaskTypes
	 *            the selectedTaskTypes to set
	 */
	public void setSelectedTaskTypes(List<TaskType> selectedTaskTypes) {
		this.selectedTaskTypes = selectedTaskTypes;
	}

	/**
	 * @param selectedTaskStatus
	 *            the selectedTaskStatus to set
	 */
	public void setSelectedTaskStatus(List<TaskStatus> selectedTaskStatus) {
		this.selectedTaskStatus = selectedTaskStatus;
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
	
	private void resetInput() {
		this.searchTerm = "";
	}
	
}