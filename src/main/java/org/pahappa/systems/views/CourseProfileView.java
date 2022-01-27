package org.pahappa.systems.views;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.googlecode.genericdao.search.Search;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.apache.commons.io.IOUtils;
import org.pahappa.systems.core.services.CourseService;
import org.pahappa.systems.core.utils.AppUtils;
import org.pahappa.systems.core.utils.UiUtils;
import org.pahappa.systems.models.Course;
import org.pahappa.systems.security.HyperLinks;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.sers.webutils.client.views.presenters.ViewPath;
import org.sers.webutils.client.views.presenters.WebFormView;
import org.sers.webutils.model.Gender;
import org.sers.webutils.model.RecordStatus;
import org.sers.webutils.model.exception.ValidationFailedException;
import org.sers.webutils.model.security.User;
import org.sers.webutils.server.core.service.UserService;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;
import org.sers.webutils.server.shared.SharedAppData;

/**
 *
 * @author Mzee Sr.
 *
 */
@ManagedBean(name = "courseProfileView")
@SessionScoped
@ViewPath(path = HyperLinks.COURSE_PROFILE_VIEW)
public class CourseProfileView extends WebFormView<Course, CourseProfileView, CoursesListView> {

    private static final long serialVersionUID = 1L;

    private List<Course> relatedCourses;

    private CourseService userService;

    @Override
    @PostConstruct
    public void beanInit() {
        this.userService = ApplicationContextProvider.getApplicationContext().getBean(CourseService.class);
        if (this.model != null) {
            this.relatedCourses = this.userService.getInstances(new Search().addFilterEqual("recordStatus", RecordStatus.ACTIVE).addFilterEqual("courseType", super.model.getCourseType()), 0, 5);
        }
    }

    @Override
    public void pageLoadInit() {
        if (this.model != null) {
            this.relatedCourses = this.userService.getInstances(new Search().addFilterEqual("recordStatus", RecordStatus.ACTIVE).addFilterEqual("courseType", super.model.getCourseType()), 0, 5);

        }
    }

    @Override
    public void persist() throws Exception {
        this.userService.saveInstance(super.model);
    }

    @Override
    public void resetModal() {

    }

    @Override
    public String getViewUrl() {
        return this.getViewPath();
    }

    public List<Course> getRelatedCourses() {
        return relatedCourses;
    }

    public void setRelatedCourses(List<Course> relatedCourses) {
        this.relatedCourses = relatedCourses;
    }

}
