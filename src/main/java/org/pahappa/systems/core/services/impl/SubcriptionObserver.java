package org.pahappa.systems.core.services.impl;

import org.sers.webutils.model.bgtasks.BackgroundTask;
import org.sers.webutils.model.bgtasks.constants.TaskType;
import org.sers.webutils.model.exception.ValidationFailedException;
import org.sers.webutils.model.migrations.Migration;
import org.sers.webutils.server.core.service.BackgroundTaskService;
import org.sers.webutils.server.core.service.TaskCreatorService;
import org.sers.webutils.server.shared.CustomLogger;
import org.sers.webutils.server.shared.CustomLogger.LogSeverity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SubcriptionObserver {

	@Autowired
	BackgroundTaskService backgroundTaskService;

	@Autowired
	TaskCreatorService taskCreatorService;

	@Migration(orderNumber = 1)
	public void monitorSubscriptions() {
		taskToMonitorSubscriptions();
	}

	public void taskToMonitorSubscriptions() {
        CustomLogger.log(SubcriptionObserver.class, LogSeverity.LEVEL_DEBUG, "== Inside taskToMonitorSubscriptions ==");
		BackgroundTask backgroundTask = new BackgroundTask();
		backgroundTask.setName("Check ending subscriptions");
		backgroundTask.setTaskType(TaskType.Routine);
		backgroundTask.addDailyRoutineTasks(23, 0);
		backgroundTask.setPackageName("org.pahappa.systems.core.services");
		backgroundTask.setClassName("SubscriptionService");
		backgroundTask.setMethodName("subscriptionObserver");

		try {
			backgroundTaskService.saveOutsideSecurityContext(backgroundTask);
			taskCreatorService.ensureExecuted();
			CustomLogger.log(SubcriptionObserver.class, LogSeverity.LEVEL_DEBUG, "== Saved taskToMonitorSubscriptions ==");
		} catch (ValidationFailedException ex) {
			CustomLogger.log(SubcriptionObserver.class, LogSeverity.LEVEL_ERROR, ex.getMessage());
		}

	}

}
