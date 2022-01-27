package org.pahappa.systems.core.services.impl;

import com.googlecode.genericdao.search.Search;
import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.pahappa.systems.constants.AccountStatus;
import org.pahappa.systems.constants.CommunicationType;
import org.pahappa.systems.models.Communication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.pahappa.systems.core.services.CommunicationService;
import org.pahappa.systems.core.services.MemberService;
import org.pahappa.systems.core.services.SystemSettingService;
import org.pahappa.systems.core.utils.CustomAppUtils;
import org.pahappa.systems.core.utils.EgoResponse;
import org.pahappa.systems.core.utils.EgoSMSClient;
import org.pahappa.systems.core.utils.EmailService;
import org.pahappa.systems.models.Member;
import org.pahappa.systems.models.SystemSetting;
import org.sers.webutils.model.exception.OperationFailedException;
import org.sers.webutils.model.exception.ValidationFailedException;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;
import org.sers.webutils.server.core.utils.DateUtils;

@Service
@Transactional
public class CommunicationServiceImpl extends GenericServiceImpl<Communication> implements CommunicationService {

    @Override
    public boolean isDeletable(Communication entity) throws OperationFailedException {
        return true;
    }

    @Override
    public Communication saveInstance(Communication communication, boolean instant) throws ValidationFailedException, OperationFailedException {

        communication = super.save(communication);
        if (instant) {
            sendInstantCommunication(communication);
        } else {
            sendCommunications();
        }
        return communication;

    }
    
     @Override
    public Communication saveInstance(Communication communication) throws ValidationFailedException, OperationFailedException {

        return super.save(communication);

    }

    @Override
    public void sendCommunications() {

        List<Communication> communications = ApplicationContextProvider.getBean(CommunicationService.class).getInstances(
                new Search().addFilterGreaterOrEqual("scheduleDate", new Date())
                        .addFilterEqual("emailsSent", false)
                        .addFilterEqual("smsSent", false)
                        .addFilterGreaterOrEqual("scheduleTime", CustomAppUtils.addMinutesToJavaUtilDate(new Date(), -50))
                        .addFilterLessOrEqual("scheduleTime", CustomAppUtils.addMinutesToJavaUtilDate(new Date(), 10)), 0, 0);

        List<Member> members = ApplicationContextProvider.getBean(MemberService.class).getInstances(new Search().addFilterEqual("accountStatus", AccountStatus.Registered), 0, 0);
        for (Communication communication : communications) {
            if (communication.getCommunicationType().equals(CommunicationType.Email)) {
                sendEmail(communication, members);
            }

            if (communication.getCommunicationType().equals(CommunicationType.Sms)) {
                sendSMS(communication, members);
            }
        }
    }

    public void sendInstantCommunication(Communication communication) {

        List<Member> members = ApplicationContextProvider.getBean(MemberService.class)
                .getInstances(
                        new Search()
                                .addFilterEqual("accountStatus", AccountStatus.Registered), 0, 0);

        if (communication.getCommunicationType().equals(CommunicationType.Email)) {
            sendEmail(communication, members);
        }

        if (communication.getCommunicationType().equals(CommunicationType.Sms)) {
            sendSMS(communication, members);
        }

    }

    private void sendEmail(final Communication communication, List<Member> members) {
        System.out.println("Sending email " + communication.getEmailSubject() + "....");
        final Set<String> emails = new HashSet<>();

        for (Member member : members) {
            emails.add(member.getEmailAddress());
        }

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    new EmailService().sendMails(emails, communication.getEmailSubject(), communication.getEmailBody());

                } catch (Exception ex) {
                    Logger.getLogger(CommunicationServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        t1.start();
        communication.setSentEmails(emails.size());
        communication.setEmailsSent(true);
        try {
            saveInstance(communication);
        } catch (ValidationFailedException | OperationFailedException ex) {
            Logger.getLogger(CommunicationServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void sendSMS(final Communication communication, List<Member> members) {
        System.out.println("Sending sms " + communication.getSmsMessage() + "....");
        final Set<String> phoneNumbers = new HashSet<>();

        for (Member member : members) {
            phoneNumbers.add(member.getPhoneNumber());
        }

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    SystemSetting setting = ApplicationContextProvider.getBean(SystemSettingService.class).getAppSetting();
                    JSONObject jSONObject = EgoSMSClient.generateEgoSmsJsonObject(phoneNumbers,
                            communication.getSmsMessage(), "AAPU", setting.getEgoSmsApiUsername(),
                            setting.getEgoSmsApiPassword());

                    EgoResponse egoResponse = EgoSMSClient.sendEgoSmsMessages(setting.getEgosmsUrl(), jSONObject);

                } catch (IOException | JSONException | ValidationFailedException ex) {
                    Logger.getLogger(CommunicationServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        t1.start();

        communication.setSentPhones(phoneNumbers.size());
        communication.setSmsSent(true);
        try {
            saveInstance(communication);
        } catch (ValidationFailedException | OperationFailedException ex) {
            Logger.getLogger(CommunicationServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
