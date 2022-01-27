package org.pahappa.systems.core.services;

import org.pahappa.systems.models.Communication;
import org.pahappa.systems.models.Course;

/**
 * Responsible for CRUD operations on {@link Communication}
 *
 * @author RayGdhrt
 *
 */
public interface CourseService extends GenericService<Course> {

    
    public void sendCommunications();

}
