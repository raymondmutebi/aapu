package org.pahappa.systems.views;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.pahappa.systems.core.services.impl.CustomApplicationSettings;
import org.pahappa.systems.security.HyperLinks;
import org.primefaces.context.RequestContext;
import org.sers.webutils.client.utils.UiUtils;
import org.sers.webutils.client.views.presenters.ViewPath;
import org.sers.webutils.client.views.presenters.WebFormView;
import org.sers.webutils.model.exception.OperationFailedException;
import org.sers.webutils.model.exception.ValidationFailedException;
import org.sers.webutils.model.security.Role;
import org.sers.webutils.model.security.User;
import org.sers.webutils.server.core.service.RoleService;
import org.sers.webutils.server.core.service.UserService;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;
import org.sers.webutils.server.core.utils.MailUtils;
import org.sers.webutils.server.shared.SharedAppData;
import org.springframework.beans.BeansException;


@ManagedBean
@SessionScoped
@ViewPath(path = HyperLinks.USER_FORM)
public class UserForm extends WebFormView<User, UserForm, UsersView> {

	private static final long serialVersionUID = 1L;
	private UserService userService;
	private CustomApplicationSettings customApplicationSettings;
//	private Role selectedRole;
	private List<Role> selectedRoles = new ArrayList<Role>();
	private List<Role> roles;
	private boolean newView;
	
	@Override
	@PostConstruct
	public void beanInit() {
		userService = ApplicationContextProvider.getBean(UserService.class);
		roles = ApplicationContextProvider.getBean(RoleService.class).getRoles();
		customApplicationSettings = ApplicationContextProvider.getBean(CustomApplicationSettings.class);
	}

	@Override
	public void persist() throws Exception {
		if(SharedAppData.getLoggedInUser() != null && !SharedAppData.getLoggedInUser().hasAdministrativePrivileges()){
//			User exisitingWithEmail = churchService.getUserByEmailAddress(super.getModel().getEmailAddress());
			
			User existingWithUsername = userService.getUserByUsername(super.getModel().getUsername());
			
//			if(exisitingWithEmail != null && !exisitingWithEmail.getId().equals(super.model.getId()))
//				throw new ValidationFailedException("User with that email address already exists");
//			
			if(existingWithUsername != null && !existingWithUsername.getId().equals(super.model.getId()))
				throw new ValidationFailedException("User with that username already exists");

			if(!MailUtils.Util.getInstance().isValidEmail(super.getModel().getEmailAddress()))
				throw new ValidationFailedException("Invalid user email address");
			
//			if(existingWithUsername == null) {
//				super.getModel().setCustomPropOne(ChurchSharedAppData.getChurch().getAcronym());
//				super.getModel().setUsername(super.getModel().getUsername() + "@" + ChurchSharedAppData.getChurch().getAcronym());
//			}
			User user = userService.saveUser(super.getModel());
			
			if(user != null && !this.isEditing) {
//				sendMail(user);
			}
		}
	}
	
//	private void sendMail(User user) {
//		HttpServletRequest origRequest = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
//		String loginUrl = CustomAppUtils.getURLWithContextPath(origRequest) + "/ServiceLogin";
//        System.out.println("HttpServelet request " + origRequest);
//        System.out.println("Login Url " + loginUrl);
//
//        String mailContent = String.format("You have been added as an administrator at "
//        		+ ChurchSharedAppData.getChurch().getName() + " Thank you for joinining Church Application. "
//        		+ "<br /><br /> Below are your login details. <br /> Username: %s <br /> Password: %s <br /> ",
//                user.getUsername(), user.getClearTextPassword());
//
//        String mailInfo = HtmlEmailTemplates.registrationTemplate(user.getUsername(), mailContent, "Go to Login", loginUrl);
//        final MailCustomService mailService = new MailCustomService();
//        if(customApplicationSettings.getDefaultMailSenderAddress().isEmpty()) {
//			UiUtils.showMessageBox("Sender address not set ", "User created successfully but email not sent", RequestContext.getCurrentInstance());
//        }else if(customApplicationSettings.getDefaultMailSenderPassword().isEmpty()) {
//			UiUtils.showMessageBox("Sender password not set ", "User created successfully but email not sent", RequestContext.getCurrentInstance());
//        }else if(customApplicationSettings.getDefaultMailSenderSmtpHost().isEmpty()) {
//			UiUtils.showMessageBox("Sender host not set ", "User created successfully but email not sent", RequestContext.getCurrentInstance());
//        }else {
//            mailService.setSenderEmail(customApplicationSettings.getDefaultMailSenderAddress());
//            mailService.setSenderPassword(customApplicationSettings.getDefaultMailSenderPassword());
//            mailService.setSmtpHost(customApplicationSettings.getDefaultMailSenderSmtpHost());
//            mailService.setParameters("Church Application", mailInfo, user.getEmailAddress());
//            new Thread(new Runnable() {
//            	public void run() {
//                   mailService.sendHtmlEmail();
//                }
//           }).start();
//        }
//        
//	}

	@Override
	public void resetModal() {
		super.resetModal();
		super.model = new User();
	}

	@Override
	public void setFormProperties() {
		super.setFormProperties();
	}

	public void deleteUser(User user) throws BeansException,
			OperationFailedException, IOException, ValidationFailedException {
//		this.churchService.deleteUser(user);
		this.redirectTo(HyperLinks.USER_VIEW);
	}

	@Override
	public void pageLoadInit() {
		// TODO Auto-generated method stub
	}

	/**
	 * @return the selectedRoles
	 */
	public List<Role> getSelectedRoles() {
		return selectedRoles;
	}

	/**
	 * @param selectedRoles the selectedRoles to set
	 */
	public void setSelectedRoles(List<Role> selectedRoles) {
		this.selectedRoles = selectedRoles;
	}

	/**
	 * @return the roles
	 */
	public List<Role> getRoles() {
		return roles;
	}

	/**
	 * @param roles the roles to set
	 */
	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
	
	public void addSelectedRoles() throws ValidationFailedException {
		if(selectedRoles.size() > 0) {
			for (Role role : selectedRoles) {
				super.getModel().addRole(role);
			}
			this.userService.saveUser(super.getModel());
			UiUtils.showMessageBox("User roles updated", "Action Successful", RequestContext.getCurrentInstance());
		}
	}
		
	public void removeUserRole(Role role) {
		System.out.println("In remove user role function");
		
		try {
			super.getModel().removeRole(role);
			this.userService.saveUser(super.getModel());
			UiUtils.showMessageBox("User roles updated", "Action Successful", RequestContext.getCurrentInstance());
		} catch (Exception e) {
			UiUtils.showMessageBox(e.getMessage(), "Action failed", RequestContext.getCurrentInstance());
		}
	}

	/**
	 * @return the newView
	 */
	public boolean isNewView() {
		return newView;
	}

	/**
	 * @param newView the newView to set
	 */
	public void setNewView(boolean newView) {
		this.newView = newView;
	}
	
}
