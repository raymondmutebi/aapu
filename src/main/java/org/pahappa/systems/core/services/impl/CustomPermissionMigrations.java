package org.pahappa.systems.core.services.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;

import org.hibernate.SessionFactory;
import org.pahappa.systems.core.services.LookUpService;
import org.pahappa.systems.core.services.SystemSettingService;
import org.pahappa.systems.core.utils.AppUtils;
import org.pahappa.systems.models.LookUpField;
import org.pahappa.systems.models.LookUpValue;
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
            }

            roleDao.mergeBG(role);
            System.out.println("Saved roles...");
        } catch (Exception exe) {
            System.out.println("Role already exists");
        }
    }

    @Migration(orderNumber = 3)
    public void saveSetting() {

        try {
            lookUpService = ApplicationContextProvider.getBean(LookUpService.class);
            settingService = ApplicationContextProvider.getBean(SystemSettingService.class);

            LookUpField field = new LookUpField();
            LookUpValue value1 = new LookUpValue();
            value1.setName("Medical");

            LookUpValue value2 = new LookUpValue();
            value2.setName("Engineering");

            field.setIsMigration(Boolean.TRUE);
            field.setName(AppUtils.PROF_DATASET_NAME);
            field.setDescription(AppUtils.PROF_DATASET_NAME);
            System.out.println("Started saving settings..");
            field = lookUpService.save(field);
            System.out.println("Prof lookup saved...");
            SystemSetting setting = new SystemSetting();
            setting.setProfessional(field);

            settingService.save(setting);
            System.out.println("Setting saved...");

        } catch (Exception exe) {
            System.out.println("Role already exists");
        }
    }

}

