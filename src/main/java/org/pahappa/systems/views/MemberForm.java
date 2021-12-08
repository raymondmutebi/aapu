package org.pahappa.systems.views;

import com.googlecode.genericdao.search.Search;
import java.util.Arrays;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.pahappa.systems.security.HyperLinks;
import org.sers.webutils.client.views.presenters.ViewPath;
import org.sers.webutils.client.views.presenters.WebFormView;
import org.sers.webutils.model.Gender;
import org.sers.webutils.model.RecordStatus;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;
import org.pahappa.systems.core.services.MemberService;
import org.pahappa.systems.models.Member;

@ManagedBean(name = "manufacturerForm")
@SessionScoped
@ViewPath(path = HyperLinks.MAMBER_DIALOG_FORM)
public class MemberForm extends WebFormView<Member, MemberForm, MembersView> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private MemberService contactService;
    Search search= new Search().addFilterEqual("recordStatus", RecordStatus.ACTIVE);
   
    private List<Gender> genders;
    
    @Override
    public void beanInit() {
       this.contactService = ApplicationContextProvider.getBean(MemberService.class);
    }
    
    @Override
    public void pageLoadInit() {
       this.genders=Arrays.asList(Gender.values());
    }
    
    @Override
    public void persist() throws Exception {
        this.contactService.save(super.model);
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
