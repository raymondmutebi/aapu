package org.pahappa.systems.views;

import com.googlecode.genericdao.search.Search;
import java.util.Arrays;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.pahappa.systems.security.HyperLinks;
import org.primefaces.context.RequestContext;
import org.sers.webutils.client.utils.UiUtils;
import org.sers.webutils.client.views.presenters.ViewPath;
import org.sers.webutils.client.views.presenters.WebFormView;
import org.sers.webutils.model.Gender;
import org.sers.webutils.model.RecordStatus;
import org.sers.webutils.model.exception.ValidationFailedException;
import org.sers.webutils.model.security.Role;
import org.sers.webutils.model.security.User;
import org.sers.webutils.server.core.service.UserService;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;
import org.pahappa.systems.core.services.MemberService;
import org.pahappa.systems.models.Member;

@ManagedBean(name = "externalMemberForm")
@ViewScoped
@ViewPath(path = HyperLinks.EXTERNAL_MEMBER_FORM)
public class ExternalMemberForm extends WebFormView<Member, ExternalMemberForm, ExternalMemberForm> {

    private static final long serialVersionUID = 1L;
    private MemberService contactService;
    Search search = new Search().addFilterEqual("recordStatus", RecordStatus.ACTIVE);

    private UserService userService;
    private List<Gender> genders;

    @Override
    public void beanInit() {
        super.model = new Member();
        this.userService = ApplicationContextProvider.getBean(UserService.class);
        this.contactService = ApplicationContextProvider.getBean(MemberService.class);

    }

    @Override
    public void pageLoadInit() {
        // TODO Auto-generated method stub
        super.model = new Member();

        this.genders = Arrays.asList(Gender.values());
    }

    @Override
    public void persist() throws Exception {
        this.contactService.saveOutsideContext(super.model);
        super.model = new Member();
        resetModal();
        UiUtils.showMessageBox("Thank you for regisering with UMA, check " + super.model.getEmailAddress() + " inbox for login credentials", "Registration successfull", RequestContext.getCurrentInstance());

        // createDefaultUser(super.model);
    }

    public void sendEmail(User user) {

    }

    private void createDefaultUser(Member manufacturer) throws ValidationFailedException {
        User user = new User();
        user.setUsername(manufacturer.getEmailAddress());
        user.setFirstName(manufacturer.getFirstName());
        user.setLastName(manufacturer.getFirstName());
        user.setClearTextPassword("business");
        List<Role> roles = userService.getRoles();
        for (Role role : roles) {
            if (!role.getName().equals(Role.DEFAULT_ADMIN_ROLE)) {
                user.addRole(role);
            }
        }

        this.userService.saveUser(user);
        sendEmail(user);

    }

    public void resetModal() {
        super.resetModal();
        super.model = new Member();
    }

    public void setFormProperties() {
        super.setFormProperties();
    }

    @Override
    public String getViewUrl() {
        // TODO Auto-generated method stub
        return null;
    }

    public List<Gender> getGenders() {
        return genders;
    }

    public void setGenders(List<Gender> genders) {
        this.genders = genders;
    }

}
