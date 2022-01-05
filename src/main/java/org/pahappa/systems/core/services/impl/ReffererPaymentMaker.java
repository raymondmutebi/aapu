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
public class ReffererPaymentMaker {

	@Autowired
	BackgroundTaskService backgroundTaskService;

	@Autowired
	TaskCreatorService taskCreatorService;

	@Migration(orderNumber = 1)
	public void checkAndPayRefferers() {
		taskToCheckAndPayRefferers();
	}

	public void taskToCheckAndPayRefferers() {
        CustomLogger.log(ReffererPaymentMaker.class, LogSeverity.LEVEL_DEBUG, "== Inside taskToCheckAndPayRefferers ==");
        BackgroundTask backgroundTask = new BackgroundTask();
		backgroundTask.setName("Make payments to refferers");
		backgroundTask.setTaskType(TaskType.Interval);
		backgroundTask.setIntervalInMinutes(2);
		backgroundTask.setPackageName("org.pahappa.systems.core.services");
		backgroundTask.setClassName("SubscriptionService");
		backgroundTask.setMethodName("checkAndPayRefferers");
		try {
			backgroundTaskService.saveOutsideSecurityContext(backgroundTask);
			taskCreatorService.ensureExecuted();
			CustomLogger.log(ReffererPaymentMaker.class, LogSeverity.LEVEL_DEBUG, "== Saved taskToCheckAndPayRefferers ==");
		} catch (ValidationFailedException ex) {
			CustomLogger.log(ReffererPaymentMaker.class, LogSeverity.LEVEL_ERROR, ex.getMessage());
		}

	}

}
