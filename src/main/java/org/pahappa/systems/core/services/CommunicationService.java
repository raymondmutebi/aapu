package org.pahappa.systems.core.services;

import org.pahappa.systems.models.Communication;
import org.sers.webutils.model.exception.OperationFailedException;
import org.sers.webutils.model.exception.ValidationFailedException;

/**
 * Responsible for CRUD operations on {@link Communication}
 *
 * @author RayGdhrt
 *
 */
public interface CommunicationService extends GenericService<Communication> {

    /**
     * 
     */
    public void sendCommunications();
    
    /**
     * 
     * @param communication
     * @param instant
     * @return
     * @throws ValidationFailedException
     * @throws OperationFailedException 
     */
   Communication saveInstance(Communication communication, boolean instant) throws ValidationFailedException, OperationFailedException;


}
