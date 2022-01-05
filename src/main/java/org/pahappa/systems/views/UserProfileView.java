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

import org.pahappa.systems.core.utils.AppUtils;
import org.pahappa.systems.core.utils.UiUtils;
import org.pahappa.systems.security.HyperLinks;
import org.primefaces.event.FileUploadEvent;
import org.sers.webutils.client.views.presenters.ViewPath;
import org.sers.webutils.client.views.presenters.WebFormView;
import org.sers.webutils.model.Gender;
import org.sers.webutils.model.exception.ValidationFailedException;
import org.sers.webutils.model.security.User;
import org.sers.webutils.server.core.service.UserService;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;
import org.sers.webutils.server.shared.SharedAppData;


@ManagedBean(name = "userProfileView")
@SessionScoped
@ViewPath(path = HyperLinks.USER_PROFILE_VIEW)
public class UserProfileView extends WebFormView<User, UserProfileView, UserProfileView> {

    private static final long serialVersionUID = 1L;

    private List<Gender> listOfGenders;
    private String currentPassword, newPassword, confirmedPassword;
    private UserService userService;
    private String imageUrl;

    @Override
    public void persist() throws Exception {
        this.userService.saveUser(super.getModel());
    }

    @Override
    public void resetModal() {
        super.model = SharedAppData.getLoggedInUser();
    }

    @Override
    @PostConstruct
    public void beanInit() {
        super.model = SharedAppData.getLoggedInUser();
        this.userService = ApplicationContextProvider.getApplicationContext().getBean(UserService.class);

        this.listOfGenders = new ArrayList<Gender>();
        this.listOfGenders.addAll(Arrays.asList(Gender.values()));
    }

    @Override
    public void pageLoadInit() {
        super.model = SharedAppData.getLoggedInUser();
    }

    @Override
    public String getViewUrl() {
        return this.getViewPath();
    }

    public void updatePassword() {

        try {
            validatePasswords();
            ApplicationContextProvider.getBean(UserService.class).changePassword(newPassword, this.model);
        } catch (ValidationFailedException ex) {
            UiUtils.ComposeFailure("Action failed", ex.getLocalizedMessage());
        }

    }

    public void validatePasswords() throws ValidationFailedException {
        if (!this.confirmedPassword.equals(this.currentPassword)) {

            throw new ValidationFailedException("New passwords dont match");
        }

    }

      /*
    Upload images to cloudinary
     */
    public void cloudinaryImageUploadEvent(FileUploadEvent event) {
        try {
            byte[] contents = IOUtils.toByteArray(event.getFile().getInputstream());
 this.imageUrl = new AppUtils().uploadCloudinaryImage(contents, "aapu_user_profile_images/"+super.model.getId());
            System.out.println("Image url = " + imageUrl);
            super.model.setCustomPropOne(this.imageUrl);
            super.model = this.userService.saveUser(super.model);

            FacesMessage msg = new FacesMessage("Successful", event.getFile().getFileName() + " is uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, msg);

        } catch (Exception ex) {
            FacesMessage msg = new FacesMessage("Failed", "Image upload failed");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            Logger.getLogger(UserProfileView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    /**
     * @return the listOfGenders
     */
    public List<Gender> getListOfGenders() {
        return listOfGenders;
    }

    /**
     * @param listOfGenders the listOfGenders to set
     */
    public void setListOfGenders(List<Gender> listOfGenders) {
        this.listOfGenders = listOfGenders;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmedPassword() {
        return confirmedPassword;
    }

    public void setConfirmedPassword(String confirmedPassword) {
        this.confirmedPassword = confirmedPassword;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
    

}
