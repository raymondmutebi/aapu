package org.pahappa.systems.views;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
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
import org.pahappa.systems.core.services.MemberService;
import org.pahappa.systems.core.utils.AppUtils;
import org.pahappa.systems.core.utils.UiUtils;
import org.pahappa.systems.models.Member;
import org.pahappa.systems.security.HyperLinks;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.sers.webutils.client.views.presenters.ViewPath;
import org.sers.webutils.client.views.presenters.WebFormView;
import org.sers.webutils.model.Gender;
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
@ManagedBean(name = "memberProfileView")
@SessionScoped
@ViewPath(path = HyperLinks.MEMBER_PROFILE_VIEW)
public class MemberProfileView extends WebFormView<Member, MemberProfileView, MemberProfileView> {

    private static final long serialVersionUID = 1L;

    private List<Gender> listOfGenders;
    private String currentPassword, newPassword, confirmedPassword;
    private MemberService userService;

    private String imageUrl;

    @Override
    public void persist() throws Exception {
        this.userService.saveInstance(super.model);
    }

    @Override
    public void resetModal() {
        super.model = userService.getMemberByUserAccount(SharedAppData.getLoggedInUser());

    }

    @Override
    @PostConstruct
    public void beanInit() {
        this.userService = ApplicationContextProvider.getApplicationContext().getBean(MemberService.class);
        if (super.model == null) {
            super.model = userService.getMemberByUserAccount(SharedAppData.getLoggedInUser());
        }
        this.listOfGenders = Arrays.asList(Gender.values());

    }

    @Override
    public void pageLoadInit() {
        if (super.model == null) {
            super.model = userService.getMemberByUserAccount(SharedAppData.getLoggedInUser());
        }
    }

    @Override
    public String getViewUrl() {
        return this.getViewPath();
    }

    public void updatePassword() {

        try {
            validatePasswords();
            ApplicationContextProvider.getBean(UserService.class).changePassword(newPassword, this.model.getUserAccount());
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
    public void imageUploadEvent(FileUploadEvent event) {
        try {
            byte[] contents = IOUtils.toByteArray(event.getFile().getInputstream());
            this.imageUrl = new AppUtils().uploadCloudinaryImage(contents, "aapu__members_profile_images/" + super.model.getId());
            System.out.println("Image url = " + imageUrl);
            super.model.setProfileImageUrl(imageUrl);
            super.model = userService.saveInstance(super.model);

        } catch (Exception ex) {
            FacesMessage msg = new FacesMessage("Failed", "Image upload failed");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            Logger.getLogger(MemberProfileView.class.getName()).log(Level.SEVERE, null, ex);
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
