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
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.pahappa.systems.constants.Region;
import org.pahappa.systems.core.services.MemberService;
import org.pahappa.systems.core.services.SystemSettingService;
import org.pahappa.systems.models.LookUpValue;
import org.pahappa.systems.models.Member;
import org.pahappa.systems.models.ProfessionValue;
import org.pahappa.systems.security.HyperLinks;
import org.sers.webutils.model.Gender;
import org.sers.webutils.model.exception.OperationFailedException;
import org.sers.webutils.model.exception.ValidationFailedException;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;

/**
 *
 * @author RayGdhrt
 */
@ManagedBean(name = "memberFormDialog", eager = true)
@SessionScoped
public class MemberFormDialog extends DialogForm<Member> {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(MemberFormDialog.class.getSimpleName());

    private MemberService memberService;
    private List<Gender> genders;
    private List<LookUpValue> professions;
    private List<Region> regions;

    @PostConstruct
    public void init() {

        memberService = ApplicationContextProvider.getApplicationContext().getBean(MemberService.class);
        this.genders = Arrays.asList(Gender.values());
        this.professions = new ArrayList<>(ApplicationContextProvider.getBean(SystemSettingService.class).getAppSetting().getProfessional().getLookUpValues());
        this.regions = Arrays.asList(Region.values());
    }

    public MemberFormDialog() {
        super(HyperLinks.MEMBER_DIALOG_FORM, 800, 600);
    }

    @Override
    public void persist() throws ValidationFailedException, OperationFailedException, Exception {

        this.memberService.saveUploadedMember(super.model);

    }

    @Override
    public void resetModal() {
        super.resetModal();
        super.model = new Member();
    }

    @Override
    public void setFormProperties() {
        super.setFormProperties();
    }

    public List<Gender> getGenders() {
        return genders;
    }

    public void setGenders(List<Gender> genders) {
        this.genders = genders;
    }

    public List<LookUpValue> getProfessions() {
        return professions;
    }

    public void setProfessions(List<LookUpValue> professions) {
        this.professions = professions;
    }

    public List<Region> getRegions() {
        return regions;
    }

    public void setRegions(List<Region> regions) {
        this.regions = regions;
    }
    

}
