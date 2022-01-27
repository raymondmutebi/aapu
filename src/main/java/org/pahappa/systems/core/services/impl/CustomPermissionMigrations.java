package org.pahappa.systems.core.services.impl;

import com.googlecode.genericdao.search.Search;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;

import org.hibernate.SessionFactory;
import org.pahappa.systems.core.services.LookUpService;
import org.pahappa.systems.core.services.MemberService;
import org.pahappa.systems.core.services.SystemSettingService;
import org.pahappa.systems.core.utils.AppUtils;
import org.pahappa.systems.models.LookUpField;
import org.pahappa.systems.models.LookUpValue;
import org.pahappa.systems.models.Member;
import org.pahappa.systems.models.ProfessionValue;
import org.pahappa.systems.models.SystemSetting;
import org.pahappa.systems.models.security.PermissionConstants;
import org.pahappa.systems.models.security.PermissionInterpreter;
import org.sers.webutils.model.RecordStatus;
import org.sers.webutils.model.migrations.Migration;
import org.sers.webutils.model.security.Permission;
import org.sers.webutils.model.security.Role;
import org.sers.webutils.server.core.dao.PermissionDao;
import org.sers.webutils.server.core.dao.RoleDao;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CustomPermissionMigrations {

    @Autowired
    PermissionDao permissionDao;

    @Autowired
    RoleDao roleDao;

    @Autowired
    LookUpService lookUpService;

    @Autowired
    SystemSettingService settingService;

    @Migration(orderNumber = 1)
    public void savePermissions() {

        for (Permission permission : PermissionInterpreter.reflectivelyGetPermissions()) {
            if (permissionDao.searchUniqueByPropertyEqual("name", permission.getName()) == null) {

                try {
                    permission.setRecordStatus(RecordStatus.ACTIVE);
                    permission.setDateCreated(new Date());
                    permission.setDateChanged(new Date());
                    permissionDao.mergeBG(permission);

                } catch (Exception exe) {
                    System.out.println("Permission already exists");
                }
            }
        }

    }

    @Migration(orderNumber = 2)
    public void saveMemberRole() {

        try {

            Role role = new Role();
            role.setRecordStatus(RecordStatus.ACTIVE);
            role.setDescription(AppUtils.MEMBER_ROLE_NAME);
            role.setName(AppUtils.MEMBER_ROLE_NAME);
            for (Permission p : permissionDao.findAll()) {
                if (p.getName().equals(AppUtils.MEMBER_ROLE_NAME)) {
                    role.addPermission(p);
                }

                if (p.getName().equals(org.sers.webutils.model.security.PermissionConstants.PERM_WEB_ACCESS)) {
                    role.addPermission(p);
                }
            }

            roleDao.mergeBG(role);
            System.out.println("Saved roles...");
        } catch (Exception exe) {
            System.out.println("Role already exists");
        }
    }

    @Migration(orderNumber = 3)
    public void saveSettingsV1() {

        try {
            lookUpService = ApplicationContextProvider.getBean(LookUpService.class);
            settingService = ApplicationContextProvider.getBean(SystemSettingService.class);
            LookUpField field = lookUpService.getLookUpFieldByName(AppUtils.PROF_DATASET_NAME);

            if (field == null) {
                field = new LookUpField();
                System.out.println("No setting found..");
            }
            for (ProfessionValue professionValue : ProfessionValue.values()) {
                LookUpValue lookUpValue = new LookUpValue();
                lookUpValue.setName(professionValue.getName());
                field.addLookupValue(lookUpValue);

            }

            field.setIsMigration(Boolean.TRUE);
            field.setName(AppUtils.PROF_DATASET_NAME);
            field.setDescription(AppUtils.PROF_DATASET_NAME);

            System.out.println("Started saving settings..");

            field = lookUpService.saveInstance(field);
            System.out.println("Prof lookup saved...");

            SystemSetting setting = settingService.getAppSetting();
            setting.setProfessional(field);

            settingService.save(setting);
            System.out.println("Setting saved...");

        } catch (Exception exe) {
            System.out.println("Some error occurred \n" + exe.getLocalizedMessage());
        }
    }

    @Migration(orderNumber = 3)
    public void saveCourseTypesV1() {

        try {
            lookUpService = ApplicationContextProvider.getBean(LookUpService.class);
            LookUpField field = new LookUpField();
            field.setName(AppUtils.COURSE_TYPES_DATASET_NAME);
            field.setLookUpValues(new HashSet<>());
            lookUpService.saveInstance(field);
            System.out.println("Setting saved...");

        } catch (Exception exe) {
            System.out.println("Some error occurred \n" + exe.getLocalizedMessage());
        }
    }

    @Migration(orderNumber = 3)
    public void updateMembeProffessionalsV2() {

        MemberService memberService = ApplicationContextProvider.getBean(MemberService.class);

        LookUpService lookUpService2 = ApplicationContextProvider.getBean(LookUpService.class);

        for (Member member : memberService.getInstances(new Search(), 0, 0)) {
            try {
                System.out.println("Now on member ..." + member.composeFullName());
                if (member.getProfessionValue() != null) {
                    LookUpValue proffessionLookup = lookUpService2.findValueInLookUp(AppUtils.PROF_DATASET_NAME, member.getProfessionValue().getName());
                    if (proffessionLookup != null) {
                        System.out.println("Gotten lookup ..." + proffessionLookup.getName());
                        member.setProfession(proffessionLookup);
                    }
                    member.setProfessionValue(null);
                    memberService.saveInstance(member);
                    System.out.println("Saved member...");
                }
            } catch (Exception exe) {
                System.out.println("Some error occurred at " + member.composeFullName() + "\n" + exe.getLocalizedMessage());
            }

        }

        System.out.println("Setting saved...");

    }

}
