package org.pahappa.systems.core.services.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.pahappa.systems.models.Member;
import org.sers.webutils.model.RecordStatus;
import org.sers.webutils.model.exception.ValidationFailedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.googlecode.genericdao.search.Search;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.pahappa.systems.constants.AccountStatus;
import org.pahappa.systems.constants.MemberRegistrationType;
import org.pahappa.systems.constants.SubscriptionStatus;
import org.pahappa.systems.constants.TemplateType;
import org.pahappa.systems.core.services.EmailTemplateService;
import org.pahappa.systems.core.utils.CustomAppUtils;
import org.pahappa.systems.core.services.MemberService;
import org.pahappa.systems.core.services.SubscriptionService;
import org.pahappa.systems.core.utils.AppUtils;
import org.pahappa.systems.core.utils.EmailService;
import org.pahappa.systems.models.EmailTemplate;
import org.pahappa.systems.models.Subscription;
import org.sers.webutils.model.exception.OperationFailedException;
import org.sers.webutils.model.security.User;
import org.sers.webutils.server.core.dao.RoleDao;
import org.sers.webutils.server.core.service.UserService;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;
import org.sers.webutils.server.core.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;

@Service
@Transactional
public class MemberServiceImpl extends GenericServiceImpl<Member> implements MemberService {

    @Autowired
    RoleDao roleDao;

    @Override
    public Member saveInstance(Member member) throws ValidationFailedException {

        if (StringUtils.isBlank(member.getPhoneNumber())) {
            throw new ValidationFailedException("Phonenumber should not be empty");
        }

        String validatedPhoneNumber = CustomAppUtils.validatePhoneNumber(member.getPhoneNumber());

        if (validatedPhoneNumber == null) {
            throw new ValidationFailedException("Invalid Phone number");
        }
        member.setPhoneNumber(validatedPhoneNumber);

        Member existingWithPhone = getMemberByPhoneNumber(member.getPhoneNumber());

        if (existingWithPhone != null && !existingWithPhone.getId().equals(member.getId())) {
            throw new ValidationFailedException("A member with the same phone number already exists!");
        }

        Member existingWithEmail = getMemberByEmail(member.getEmailAddress());

        if (existingWithEmail != null && !existingWithEmail.getId().equals(member.getId())) {
            throw new ValidationFailedException("A member with the same email already exists!");
        }
        member.setRegistrationType(MemberRegistrationType.SYSTEM);
        return super.merge(member);
    }

    @Override
    public Member saveUploadedMember(Member member) throws ValidationFailedException, Exception, OperationFailedException {

        if (StringUtils.isBlank(member.getPhoneNumber())) {
            throw new ValidationFailedException("Phonenumber should not be empty");
        }

        String validatedPhoneNumber = CustomAppUtils.validatePhoneNumber(member.getPhoneNumber());

        if (validatedPhoneNumber == null) {
            throw new ValidationFailedException("Invalid Phone number");
        }

        if (member.getDateCreated() == null) {
            throw new ValidationFailedException("Missing registartion date");
        }
        if (member.getDateChanged() == null) {
            throw new ValidationFailedException("Missing subscription");
        }
        member.setPhoneNumber(validatedPhoneNumber);

        Member existingWithPhone = getMemberByPhoneNumber(member.getPhoneNumber());

        if (existingWithPhone != null && !existingWithPhone.getId().equals(member.getId())) {
            throw new ValidationFailedException("A member with the same phone number already exists!");
        }
        Member existingWithEmail = getMemberByEmail(member.getEmailAddress());

        if (existingWithEmail != null && !existingWithEmail.getId().equals(member.getId())) {
            throw new ValidationFailedException("A member with the same email already exists!");
        }

        Member existsWithBoth = getMemberByPhoneNumberAndEmail(member.getPhoneNumber(), member.getEmailAddress());
        if (existsWithBoth != null && !existsWithBoth.getId().equals(member.getId())) {
            existsWithBoth.setLastEmailVerificationCode(member.getLastEmailVerificationCode());
            member.setId(existsWithBoth.getId());

        }
        member.setRegistrationType(MemberRegistrationType.MANUAL);

        member = super.save(member);
        Subscription subscription = new Subscription();
        subscription.setMember(member);
        subscription.setStartDate(member.getDateChanged());
        subscription.setEndDate(DateUtils.getDateAfterDays(member.getDateChanged(), 365));
        subscription.setStatus(SubscriptionStatus.ACTIVE);
        ApplicationContextProvider.getBean(SubscriptionService.class).saveInstance(subscription);
        member = activateMemberAccount(member);
        return member;

    }

    @Override
    public Member saveOutsideContext(Member member) throws ValidationFailedException {

        if (StringUtils.isBlank(member.getPhoneNumber())) {
            throw new ValidationFailedException("Phone number should not be empty");
        }

        String validatedPhoneNumber = CustomAppUtils.validatePhoneNumber(member.getPhoneNumber());

        if (validatedPhoneNumber == null) {
            throw new ValidationFailedException("Invalid Phone number");
        }
        member.setPhoneNumber(validatedPhoneNumber);

        Member existingWithPhone = getMemberByPhoneNumber(member.getPhoneNumber());

        if (existingWithPhone != null && !existingWithPhone.getId().equals(member.getId())) {
            throw new ValidationFailedException("A member with the same phone number already exists!");
        }

        Member existingWithEmail = getMemberByEmail(member.getEmailAddress());

        if (existingWithEmail != null && !existingWithEmail.getId().equals(member.getId())) {
            throw new ValidationFailedException("A member with the same email already exists!");
        }

        Member existsWithBoth = getMemberByPhoneNumberAndEmail(member.getPhoneNumber(), member.getEmailAddress());
        if (existsWithBoth != null && !existsWithBoth.getId().equals(member.getId())) {
            existsWithBoth.setLastEmailVerificationCode(member.getLastEmailVerificationCode());
            member.setId(existsWithBoth.getId());

        }

        member.setRegistrationType(MemberRegistrationType.SYSTEM);

        return super.mergeBG(member);
    }

    public Member getMemberByPhoneNumberAndEmail(String phoneNumber, String email) {
        Search search = new Search();
        search.addFilterEqual("emailAddress", email);
        search.addFilterEqual("phoneNumber", phoneNumber);

        search.addFilterNotEqual("accountStatus", AccountStatus.Registered);
        search.addFilterEqual("recordStatus", RecordStatus.ACTIVE);
        List<Member> members = super.search(search);
        if (members.isEmpty()) {
            return null;
        }
        return members.get(0);
    }

    @Override
    public Member getMemberByPhoneNumber(String phoneNumber) {
        Search search = new Search();
        search.addFilterEqual("phoneNumber", phoneNumber);

        search.addFilterEqual("accountStatus", AccountStatus.Registered);
        search.addFilterEqual("recordStatus", RecordStatus.ACTIVE);
        List<Member> members = super.search(search);
        if (members.isEmpty()) {
            return null;
        }
        return members.get(0);
    }

    @Override
    public Member getMemberByEmail(String email) {
        Search search = new Search();
        search.addFilterEqual("emailAddress", email);
        search.addFilterEqual("accountStatus", AccountStatus.Registered);
        search.addFilterEqual("recordStatus", RecordStatus.ACTIVE);
        List<Member> members = super.search(search);
        if (members.isEmpty()) {
            return null;
        }
        return members.get(0);
    }

    @Override
    public Member getMemberByUserAccount(User user) {
        if (user == null) {
            return null;
        }
        return super.searchUniqueByPropertyEqual("userAccount", user, RecordStatus.ACTIVE);
    }

    @Override
    public List<Member> getMembers(Search search, int offset, int limit) {
        search.setFirstResult(offset);
        search.setMaxResults(limit);
        return super.search(search);
    }

    @Override
    public int countMembers(Search search) {
        return super.count(search);
    }

    @Override
    public Member getMemberById(String memberId) {
        return super.searchUniqueByPropertyEqual("id", memberId, RecordStatus.ACTIVE);
    }

    @Override
    public boolean isDeletable(Member entity) throws OperationFailedException {
        return true;

    }

    @Override
    public void block(Member member, String blockNotes) throws ValidationFailedException, OperationFailedException {
        System.out.println("Starting member blocking...");

        member.setAccountStatus(AccountStatus.Blocked);
        blockUser(member.getUserAccount());
        System.out.println("Deleted user account...");

        Member savedMember = saveInstance(member);
        System.out.println("Saved member...");

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    new EmailService().sendMail(savedMember.getEmailAddress(), "AAPU account blocking", blockNotes);

                } catch (Exception ex) {
                    Logger.getLogger(MemberServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }).start();

    }

    @Override
    public void unblock(Member member, String unblockNotes) throws ValidationFailedException, OperationFailedException {
        System.out.println("Starting member unblocking...");

        member.setAccountStatus(AccountStatus.Active);
        unBlockUser(member.getUserAccount());
        System.out.println("Unblocked user account...");

        Member savedMember = saveInstance(member);
        System.out.println("Saved member...");

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    new EmailService().sendMail(savedMember.getEmailAddress(), "AAPU account activation", unblockNotes);

                } catch (Exception ex) {
                    Logger.getLogger(MemberServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }).start();

    }

    @Override
    public Member activateMemberAccount(Member member) throws Exception {
        System.out.println("Starting member activation...");
        String password = "business" + AppUtils.generateOTP(4);
        member.setLastEmailVerificationCode(password);
        member.setAccountStatus(AccountStatus.Registered);
        member.setUserAccount(createDefaultUser(member, password));
        System.out.println("Created user account...");

        Member savedMember = saveInstance(member);
        System.out.println("Saved member...");

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("Sending email...");
                    EmailTemplate emailTemplate = ApplicationContextProvider.getBean(EmailTemplateService.class)
                            .getEmailTemplateByType(TemplateType.LOGIN_CREDENTIALS);

                    if (emailTemplate != null) {
                        String html = emailTemplate.getTemplate();

                        html = html.replace("{fullName}", savedMember.composeFullName());
                        html = html.replace("{password}", password);
                        new EmailService().sendMail(savedMember.getEmailAddress(), "AAPU Login details", html);
                    } else {
                        new EmailService().sendMail(savedMember.getEmailAddress(), "AAPU Login details", "Your account was successfully created, Your login credentials are <p>Username: <b>" + savedMember.getEmailAddress() + "</b> </p> <p>Password: <b>" + password + "</b> </p>");

                    }
                } catch (Exception ex) {
                    Logger.getLogger(MemberServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }).start();

        return savedMember;

    }

    private User createDefaultUser(Member member, String password) throws ValidationFailedException {
        System.out.println("Creating user account...");

        User user = new User();
        user.setUsername(member.getEmailAddress());
        user.setFirstName(member.getFirstName());
        user.setLastName(member.getLastName());
        user.setClearTextPassword(password);
        UserService userService = ApplicationContextProvider.getBean(UserService.class);
        user.addRole(userService.getRoleByRoleName(AppUtils.MEMBER_ROLE_NAME));

        return userService.saveUser(user);

    }

    private User blockUser(User user) throws ValidationFailedException {
        System.out.println("Bolcking user account...");

        user.setRecordStatus(RecordStatus.ACTIVE_LOCKED);

        UserService userService = ApplicationContextProvider.getBean(UserService.class);
        user.removeRole(userService.getRoleByRoleName(AppUtils.MEMBER_ROLE_NAME));

        return userService.saveUser(user);

    }

    private User unBlockUser(User user) throws ValidationFailedException {
        System.out.println("Unblocking user account...");

        user.setRecordStatus(RecordStatus.ACTIVE);

        UserService userService = ApplicationContextProvider.getBean(UserService.class);
        user.addRole(userService.getRoleByRoleName(AppUtils.MEMBER_ROLE_NAME));

        return userService.saveUser(user);

    }

}
