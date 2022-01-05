package org.pahappa.systems.core.services;

import org.pahappa.systems.models.Communication;

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

}
