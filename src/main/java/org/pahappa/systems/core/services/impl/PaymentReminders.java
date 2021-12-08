package org.pahappa.systems.core.services.impl;

import org.sers.webutils.model.bgtasks.BackgroundTask;
import org.sers.webutils.model.bgtasks.RoutineTaskData;
import org.sers.webutils.model.bgtasks.constants.DayOfWeek;
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
public class PaymentReminders {

	@Autowired
	BackgroundTaskService backgroundTaskService;

	@Autowired
	TaskCreatorService taskCreatorService;

	@Migration(orderNumber = 1)
	public void paymentReminders() {
		taskForPaymentRemiders();
	}

	public void taskForPaymentRemiders() {
        CustomLogger.log(PaymentReminders.class, LogSeverity.LEVEL_DEBUG, "== Inside taskForPaymentRemiders ==");
		BackgroundTask routineTask = new BackgroundTask();
		routineTask.setName("Payment Reminder");
		routineTask.setTaskType(TaskType.Routine);
		routineTask.addRoutineMetaData(new RoutineTaskData(07, 00, DayOfWeek.FRIDAY));
		routineTask.setPackageName("org.pahappa.systems.core.services");
		routineTask.setClassName("ChurchService");
		routineTask.setMethodName("sendReminderForPayment");
		
		try {
			backgroundTaskService.saveOutsideSecurityContext(routineTask);
			taskCreatorService.ensureExecuted();
			CustomLogger.log(PaymentReminders.class, LogSeverity.LEVEL_DEBUG, "== Saved taskForPaymentRemiders ==");
		} catch (ValidationFailedException ex) {
			CustomLogger.log(PaymentReminders.class, LogSeverity.LEVEL_ERROR, ex.getMessage());
		}

	}

}
