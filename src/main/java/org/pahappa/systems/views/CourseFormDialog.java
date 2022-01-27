/*
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pahappa.systems.views;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.apache.commons.io.IOUtils;
import org.pahappa.systems.core.services.CourseService;
import org.pahappa.systems.core.services.LookUpService;
import org.pahappa.systems.core.utils.AppUtils;
import org.pahappa.systems.models.Course;
import org.pahappa.systems.models.LookUpValue;
import org.pahappa.systems.security.HyperLinks;
import org.primefaces.event.FileUploadEvent;
import org.sers.webutils.model.exception.OperationFailedException;
import org.sers.webutils.model.exception.ValidationFailedException;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;

/**
 *
 * @author RayGdhrt
 */
@ManagedBean(name = "courseFormDialog", eager = true)
@SessionScoped
public class CourseFormDialog extends DialogForm<Course> {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(CourseFormDialog.class.getSimpleName());

    private CourseService courseService;
    private List<LookUpValue> types;
   

    @PostConstruct
    public void init() {

        courseService = ApplicationContextProvider.getApplicationContext().getBean(CourseService.class);
        this.types= new ArrayList<>(ApplicationContextProvider.getBean(LookUpService.class).getLookUpFieldByName(AppUtils.COURSE_TYPES_DATASET_NAME).getLookUpValues());
      
    }
    
      /*
    Upload images to cloudinary
     */
    public void imageUploadEvent(FileUploadEvent event) {
        try {
            byte[] contents = IOUtils.toByteArray(event.getFile().getInputstream());
            String imageUrl = new AppUtils().uploadCloudinaryImage(contents, "aapu__courses_images/" + super.model.getId());
            System.out.println("Image url = " + imageUrl);
            super.model.setImageUrl(imageUrl);
            super.model = courseService.saveInstance(super.model);

        } catch (Exception ex) {
            FacesMessage msg = new FacesMessage("Failed", "Image upload failed");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            Logger.getLogger(MemberProfileView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public CourseFormDialog() {
        super(HyperLinks.COURSE_FORM_DIALOG, 800, 600);
    }

    @Override
    public void persist() throws ValidationFailedException, OperationFailedException, Exception {

        this.courseService.saveInstance(super.model);

    }

    @Override
    public void resetModal() {
        super.resetModal();
        super.model = new Course();
    }

    @Override
    public void setFormProperties() {
        super.setFormProperties();
    }

    public List<LookUpValue> getTypes() {
        return types;
    }

    public void setTypes(List<LookUpValue> types) {
        this.types = types;
    }

    
    
   

}
