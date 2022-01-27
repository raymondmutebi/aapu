package org.pahappa.systems.core.services.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.util.StringUtil;
import org.pahappa.systems.core.services.CourseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.pahappa.systems.models.Course;
import org.sers.webutils.model.exception.OperationFailedException;
import org.sers.webutils.model.exception.ValidationFailedException;

@Service
@Transactional
public class CourseServiceImpl extends GenericServiceImpl<Course> implements CourseService {

    @Override
    public boolean isDeletable(Course entity) throws OperationFailedException {
        return true;
    }

    @Override
    public Course saveInstance(Course course) throws ValidationFailedException, OperationFailedException {

        if (StringUtils.isEmpty(course.getTitle())) {
            throw new ValidationFailedException("Missing title");
        }

        if (StringUtils.isEmpty(course.getDescription())) {
            throw new ValidationFailedException("Missing description");
        }

        if (course.getStartDate() == null) {
            throw new ValidationFailedException("Missing start date");
        }

        if (course.getEndDate() == null) {
            throw new ValidationFailedException("Missing end date");
        }

        return super.save(course);
    }

    @Override
    public void sendCommunications() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
