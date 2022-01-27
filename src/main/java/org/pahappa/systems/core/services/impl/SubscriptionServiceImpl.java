package org.pahappa.systems.core.services.impl;

import com.googlecode.genericdao.search.Search;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.pahappa.systems.constants.AccountStatus;
import org.pahappa.systems.constants.SubscriptionStatus;
import org.pahappa.systems.constants.TemplateType;
import org.pahappa.systems.core.services.EmailTemplateService;
import org.pahappa.systems.core.services.MemberService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.pahappa.systems.core.services.SubscriptionService;
import org.pahappa.systems.core.utils.AppUtils;
import org.pahappa.systems.core.utils.EmailService;
import org.pahappa.systems.models.EmailTemplate;
import org.pahappa.systems.models.Member;
import org.pahappa.systems.models.Payment;
import org.pahappa.systems.models.Subscription;
import org.sers.webutils.model.RecordStatus;
import org.sers.webutils.model.exception.OperationFailedException;
import org.sers.webutils.model.exception.ValidationFailedException;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;
import org.sers.webutils.server.core.utils.DateUtils;

@Service
@Transactional
public class SubscriptionServiceImpl extends GenericServiceImpl<Subscription> implements SubscriptionService {
    
    @Override
    public boolean isDeletable(Subscription entity) throws OperationFailedException {
        return false;
    }
    
    @Override
    public Subscription saveInstance(Subscription instance) throws ValidationFailedException, OperationFailedException {
        return super.save(instance);
        
    }
    
    @Override
    public void subscriptionObserver() {
        
        List<Subscription> endingSubscriptions = getInstances(new Search()
                .addFilterEqual("recordStatus", RecordStatus.ACTIVE)
                .addFilterEqual("status", SubscriptionStatus.ACTIVE)
                .addFilterLessOrEqual("endDate", new Date()), 0, 0);
        
        for (Subscription subscription : endingSubscriptions) {
            Member member = subscription.getMember();
            member.setAccountStatus(AccountStatus.Stopped);
            
            try {
                ApplicationContextProvider.getBean(MemberService.class).saveInstance(member);
                subscription.setStatus(SubscriptionStatus.STOPPED);
                saveInstance(subscription);
                 new EmailService().sendMail(subscription.getMember().getEmailAddress(), "AAPU Subscription expired", "Your annual subscription has expired. Please renew for continued access to the AAPU services. Thank you");
                       
            } catch (ValidationFailedException | OperationFailedException ex) {
                Logger.getLogger(SubscriptionServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(SubscriptionServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        
        List<Subscription> upcomingSubscriptions = getInstances(new Search()
                .addFilterEqual("recordStatus", RecordStatus.ACTIVE)
                .addFilterEqual("status", SubscriptionStatus.SCHEDULED)
                .addFilterLessOrEqual("startDate", new Date()), 0, 0);
        
        for (Subscription subscription : upcomingSubscriptions) {
            Member member = subscription.getMember();
            member.setAccountStatus(AccountStatus.Active);
            
            try {
                ApplicationContextProvider.getBean(MemberService.class).saveInstance(member);
                subscription.setStatus(SubscriptionStatus.ACTIVE);
                saveInstance(subscription);
            } catch (ValidationFailedException | OperationFailedException ex) {
                Logger.getLogger(SubscriptionServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        
    }
    
    @Override
    public Subscription getActiveSubscription(Member member) {
        
        Search search = new Search()
                .addFilterEqual("member", member)
                .addFilterEqual("status", SubscriptionStatus.ACTIVE);
        return super.searchUnique(search);
        
    }
    
    @Override
    public Subscription createNewSubscription(Payment payment) {
        if (payment == null || payment.getMember() == null) {
            return null;
        }
        Subscription subscription = new Subscription();
        Subscription existingActiveSubscription = getActiveSubscription(payment.getMember());
        subscription.setStartDate(new Date());
        if (existingActiveSubscription != null) {
            subscription.setStartDate(DateUtils.getDateAfterDays(existingActiveSubscription.getEndDate(), 1));
            
        }
        
        subscription.setMember(payment.getMember());
        subscription.setPayment(payment);
        
        subscription.setEndDate(DateUtils.getDateAfterDays(subscription.getStartDate(), 365));
        subscription.setStatus(SubscriptionStatus.ACTIVE);
        final Subscription savedSubscription = subscription = super.save(subscription);
        Member member = savedSubscription.getMember();
        member.setAccountStatus(AccountStatus.Active);
        try {
            ApplicationContextProvider.getBean(MemberService.class).quickSave(member);
        } catch (ValidationFailedException ex) {
            Logger.getLogger(SubscriptionServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    
                    System.out.println("Sending email...");
                    EmailTemplate emailTemplate = ApplicationContextProvider.getBean(EmailTemplateService.class)
                            .getEmailTemplateByType(TemplateType.SUCCESS_PAYMENT);
                    
                    if (emailTemplate != null) {
                        String html = emailTemplate.getTemplate();
                        
                        html = html.replace("{fullName}", savedSubscription.getMember().composeFullName());
                        html = html.replace("{transactionID}", savedSubscription.getPayment().getTransactionId());
                        new EmailService().sendMail(savedSubscription.getMember().getEmailAddress(), "AAPU Subscription", html);
                    } else {
                        new EmailService().sendMail(savedSubscription.getMember().getEmailAddress(), "AAPU Subscription", "Your subscription has been recieved");
                        
                    }
                } catch (Exception ex) {
                    Logger.getLogger(SubscriptionServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }).start();
        
        return subscription;
        
    }
    
    @Override
    public Subscription extendSubscription(Member member, Date startDate, byte[] attachment) {
        if (member == null|| attachment==null) {
            return null;
        }
        Subscription subscription = new Subscription();
        
        subscription.setStartDate(startDate);
        subscription.setStatus(SubscriptionStatus.ACTIVE);
        
        //Find existing active subscription
        Subscription existingActiveSubscription = getActiveSubscription(member);
        if (existingActiveSubscription != null) {
            
            //Check if exixting subscription is not due
            if (DateUtils.calculateDaysBetween(new Date(), existingActiveSubscription.getEndDate()) > 0) {
                subscription.setStartDate(DateUtils.getDateAfterDays(existingActiveSubscription.getEndDate(), 365));
                subscription.setStatus(SubscriptionStatus.SCHEDULED);
            } 
            //Else
            else {
                
                existingActiveSubscription.setStatus(SubscriptionStatus.STOPPED);
                super.save(existingActiveSubscription);
            }
        }
        
        subscription.setMember(member);
        subscription.setPayment(null);
        System.out.println("Starting first save...");
        subscription = super.save(subscription);
         System.out.println("Starting image upload...");
        
        subscription.setAttachmentUrl(new AppUtils().uploadCloudinaryImage(attachment, "aapu_subscriptions/" + subscription.getId()));
        
        subscription.setEndDate(DateUtils.getDateAfterDays(subscription.getStartDate(), 365));
        
        final Subscription savedSubscription =  super.save(subscription);
        
        member.setAccountStatus(AccountStatus.Active);
        try {
            ApplicationContextProvider.getBean(MemberService.class).quickSave(member);
        } catch (ValidationFailedException ex) {
            Logger.getLogger(SubscriptionServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
         System.out.println("Saved member...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    
                    System.out.println("Sending email...");
                    
                    new EmailService().sendMail(savedSubscription.getMember().getEmailAddress(), "AAPU Subscription", "Congratulations,<br>Your annual subscription has been successfully extended to " + savedSubscription.getEndDate());
                    
                } catch (Exception ex) {
                    Logger.getLogger(SubscriptionServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }).start();
        
        return subscription;
        
    }
    
}
