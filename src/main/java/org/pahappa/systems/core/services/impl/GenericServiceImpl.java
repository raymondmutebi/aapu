/**
 * 
 */
package org.pahappa.systems.core.services.impl;

import java.util.Date;
import java.util.List;

import org.sers.webutils.model.BaseEntity;
import org.sers.webutils.model.RecordStatus;
import org.sers.webutils.model.exception.OperationFailedException;
import org.sers.webutils.server.core.dao.impl.BaseDAOImpl;
import org.sers.webutils.server.shared.CustomLogger;
import org.sers.webutils.server.shared.CustomLogger.LogSeverity;
import org.sers.webutils.server.shared.SharedAppData;
import org.springframework.transaction.annotation.Transactional;

import com.googlecode.genericdao.search.Search;
import org.pahappa.systems.core.services.GenericService;

/**
 * Provides for generic implementation of the {@link GenericService}.Concrete classes need to provide implementation of methods that are specific
 to that class or the associated entity.
 * 
 * 
 * @author Mzee Sr.
 * @param <T>
 *
 */
@Transactional
public abstract class GenericServiceImpl<T extends BaseEntity> extends BaseDAOImpl<T> implements GenericService<T> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.pahappa.systems.akinamama.utils.backend.core.services.GenericService#
	 * countInstances(com.googlecode.genericdao.search.Search)
	 */
	@Override
	public int countInstances(Search arg0) {
		// TODO Auto-generated method stub
		return super.count(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.pahappa.systems.akinamama.utils.backend.core.services.GenericService#
	 * deleteInstance(java.lang.Object)
	 */
	@Override
	public void deleteInstance(T arg0) throws OperationFailedException {
		if (!isDeletable(arg0))
			throw new OperationFailedException("Deletion is yet supported for this instance.");
		changeStatusToDeleted(arg0);
	}

	/**
	 * Deactivates the instance by changing its status to deleted
	 * 
	 * @param arg0
	 */
	private void changeStatusToDeleted(T arg0) {
		CustomLogger.log(getClass(), LogSeverity.LEVEL_DEBUG,
				String.format("Instance is deletable! Now setting the audit trail."));
		arg0.setChangedBy(SharedAppData.getLoggedInUser());
		arg0.setDateChanged(new Date());
		arg0.setRecordStatus(RecordStatus.DELETED);
		super.save(arg0);
		CustomLogger.log(getClass(), LogSeverity.LEVEL_DEBUG, String.format("Set record to deleted!"));
	}

	@Override
	public void deleteInstances(Search search) throws OperationFailedException {
		if (isDeletable((T) super.searchUnique(new Search().setFirstResult(0).setMaxResults(1))))
			throw new OperationFailedException("Deletion is yet supported for this instance.");

		search.setFirstResult(0);
		search.setMaxResults(10);
		// Manage memory using recursion and step loading
		deleteRecursively(search);
	}

	private void deleteRecursively(Search search) throws OperationFailedException {
		List<T> instances = super.search(search);

		if (instances.isEmpty())
			return;

		for (T instance : instances)
			changeStatusToDeleted(instance);

		deleteRecursively(search);
	}

	/**
	 * Must be implemented by all classes that extend this abstract class.
	 * 
	 * @param entity
	 * @return
	 */
	public abstract boolean isDeletable(T entity) throws OperationFailedException;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.pahappa.systems.akinamama.utils.backend.core.services.GenericService#
	 * getInstanceByID(java.lang.String)
	 */
	@Override
	public T getInstanceByID(String arg0) {
		// TODO Auto-generated method stub
		return super.searchUniqueByPropertyEqual("id", arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.pahappa.systems.akinamama.utils.backend.core.services.GenericService#
	 * getInstances(com.googlecode.genericdao.search.Search, int, int)
	 */
	@Override
	public List<T> getInstances(Search arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		return super.search(arg0.setFirstResult(arg1).setMaxResults(arg2));
	}
}
