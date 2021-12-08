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
public class TransactionStatusUpdater {

	@Autowired
	BackgroundTaskService backgroundTaskService;

	@Autowired
	TaskCreatorService taskCreatorService;

	@Migration(orderNumber = 1)
	public void chechTransactionUpdates() {
		taskToCheckTransactionUpdates();
	}

	public void taskToCheckTransactionUpdates() {
        CustomLogger.log(TransactionStatusUpdater.class, LogSeverity.LEVEL_DEBUG, "== Inside taskToCheckTransactionUpdates ==");
        BackgroundTask backgroundTask = new BackgroundTask();
		backgroundTask.setName("Update status of pending transactions");
		backgroundTask.setTaskType(TaskType.Interval);
		backgroundTask.setIntervalInMinutes(2);
		backgroundTask.setPackageName("org.pahappa.systems.core.services");
		backgroundTask.setClassName("PaymentLogService");
		backgroundTask.setMethodName("checkAndUpdatePendingTransactions");
		try {
			backgroundTaskService.saveOutsideSecurityContext(backgroundTask);
			taskCreatorService.ensureExecuted();
			CustomLogger.log(TransactionStatusUpdater.class, LogSeverity.LEVEL_DEBUG, "== Saved taskToCheckTransactionUpdates ==");
		} catch (ValidationFailedException ex) {
			CustomLogger.log(TransactionStatusUpdater.class, LogSeverity.LEVEL_ERROR, ex.getMessage());
		}

	}

}
