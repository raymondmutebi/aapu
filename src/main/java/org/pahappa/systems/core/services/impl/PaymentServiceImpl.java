package org.pahappa.systems.core.services.impl;

import com.googlecode.genericdao.search.Search;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.pahappa.systems.core.services.MemberService;
import org.pahappa.systems.models.Payment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.pahappa.systems.core.services.PaymentService;
import org.pahappa.systems.core.services.SubscriptionService;
import org.pahappa.systems.core.services.SystemSettingService;
import org.pahappa.systems.core.utils.AppUtils;
import org.pahappa.systems.core.utils.FlutterReponse;
import org.pahappa.systems.core.utils.FlutterwaveClient;
import org.pahappa.systems.models.Member;
import org.pahappa.systems.models.PaymentReasonType;
import org.pahappa.systems.constants.TransactionStatus;
import org.sers.webutils.model.RecordStatus;
import org.sers.webutils.model.exception.OperationFailedException;
import org.sers.webutils.model.exception.ValidationFailedException;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;

@Service
@Transactional
public class PaymentServiceImpl extends GenericServiceImpl<Payment> implements PaymentService {

    @Override
    public boolean isDeletable(Payment entity) throws OperationFailedException {
        return true;
    }

    @Override
    public Payment saveInstance(Payment instance) throws ValidationFailedException, OperationFailedException {
        return super.save(instance);

    }

    @Override
    public Payment getPaymentByTransactionId(String transactionId) {
        if (transactionId == null || transactionId.isEmpty()) {
            return null;
        }
        return super.searchUniqueByPropertyEqual("transactionId", transactionId, RecordStatus.ACTIVE);

    }

    @Override
    public String initiateRegistrationFeePayment(Member member) throws Exception {

        Payment newPayment = new Payment();
      
        newPayment.setMember(member);
        newPayment.setReasonType(PaymentReasonType.Registration_fee);
       
        newPayment.setTrasanctionStatus(org.pahappa.systems.constants.TransactionStatus.INITIATED);
        newPayment.setAmount(ApplicationContextProvider.getBean(SystemSettingService.class).getAppSetting().getRegistartionFee());
        newPayment.setTrasanctionDate(new Date().toString());
        newPayment.setRecordStatus(RecordStatus.ACTIVE);

        newPayment = createNewPaymentInstanceWithTransactionId(newPayment);

        member.setRegistrationFeePayment(newPayment);
        ApplicationContextProvider.getBean(MemberService.class).save(member);

        String redirectUrl = ApplicationContextProvider.getBean(SystemSettingService.class).getAppSetting().getFlutterwaveReditectUrl();
        String paymentUrl;
        FlutterReponse flutterReponse = new FlutterwaveClient().requestPaymentInitiation(newPayment.getTransactionId(), newPayment.getAmount(), member.getEmailAddress(), member.getPhoneNumber(), member.getFirstName(), redirectUrl);
        if ("success".equals(flutterReponse.status)) {
            paymentUrl = flutterReponse.data.link;
            return paymentUrl;
        }
        return null;

    }

    @Override
    public Payment updatePayment(String transactionid, String raveId) {
        try {
            Payment payment = getPaymentByTransactionId(transactionid);
            if (raveId != null) {
                payment.setRaveId(raveId);
                payment = saveInstance(payment);

            }

            FlutterReponse flutterReponse = new FlutterwaveClient().checkPaymentStatusV2(payment.getRaveId());
            if ("success".equals(flutterReponse.status) && flutterReponse.data != null) {

                if ("successful".equals(flutterReponse.data.status)) {
                    payment.setStatus(200);
                    payment.setAmount(flutterReponse.data.amount);
                    payment.setDescription(flutterReponse.data.toString());

                    payment.setTrasanctionStatus(org.pahappa.systems.constants.TransactionStatus.SUCESSFULL);
                    payment.setDateChanged(new Date());
                    payment = save(payment);
                    if (payment.getReasonType().equals(PaymentReasonType.Registration_fee)) {
                        ApplicationContextProvider.getBean(MemberService.class).activateMemberAccount(payment.getMember());

                    }

                    if (payment.getReasonType().equals(PaymentReasonType.Subscription)) {
                        ApplicationContextProvider.getBean(SubscriptionService.class).createNewSubscription(payment);

                    }
                    return payment;
                }

                if ("failed".equalsIgnoreCase(flutterReponse.data.status)) {

                    payment.setStatus(200);
                    payment.setDescription(flutterReponse.data.toString());
                    payment.setTrasanctionStatus(org.pahappa.systems.constants.TransactionStatus.FAILED);
                    payment.setDateChanged(new Date());
                    return save(payment);
                } else {

                    payment.setStatus(200);
                    payment.setDescription(flutterReponse.data.toString());
                    payment.setTrasanctionStatus(org.pahappa.systems.constants.TransactionStatus.PENDING);
                    payment.setDateChanged(new Date());
                    return save(payment);
                }

            } else {
                System.out.println("Some error occured ==>" + flutterReponse.message);
            }
        } catch (IOException ex) {
            Logger.getLogger(PaymentServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(PaymentServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public void checkAndUpdatePendingTransactions() {
        Search search = new Search()
                .addFilterEqual("recordStatus", RecordStatus.ACTIVE)
                .addFilterEqual("trasanctionStatus", TransactionStatus.PENDING);
        List<Payment> payments = getInstances(search, 0, 0);
        for (Payment payment : payments) {
            try {

                FlutterReponse flutterReponse = new FlutterwaveClient().checkPaymentStatusV2(payment.getRaveId());
                if ("success".equals(flutterReponse.status) && flutterReponse.data != null) {

                    if ("successfull".equals(flutterReponse.data.status)) {
                        payment.setStatus(200);
                        payment.setAmount(flutterReponse.data.amount);
                        payment.setDescription(flutterReponse.data.toString());

                        payment.setTrasanctionStatus(org.pahappa.systems.constants.TransactionStatus.SUCESSFULL);
                        payment.setDateChanged(new Date());
                        payment = save(payment);
                        if (payment.getReasonType().equals(PaymentReasonType.Registration_fee)) {
                            ApplicationContextProvider.getBean(MemberService.class).activateMemberAccount(payment.getMember());

                        }

                        if (payment.getReasonType().equals(PaymentReasonType.Subscription)) {
                            ApplicationContextProvider.getBean(SubscriptionService.class).createNewSubscription(payment);

                        }
                        return;
                    }

                    if ("failed".equalsIgnoreCase(flutterReponse.data.status)) {

                        payment.setStatus(200);
                        payment.setDescription(flutterReponse.data.toString());
                        payment.setTrasanctionStatus(org.pahappa.systems.constants.TransactionStatus.FAILED);
                        payment.setDateChanged(new Date());
                        save(payment);
                        return;
                    } else {

                        payment.setStatus(200);
                        payment.setDescription(flutterReponse.data.toString());
                        payment.setTrasanctionStatus(org.pahappa.systems.constants.TransactionStatus.PENDING);
                        payment.setDateChanged(new Date());
                        save(payment);
                        return;
                    }

                } else {
                    System.out.println("Some error occured ==>" + flutterReponse.message);
                }
            } catch (IOException ex) {
                Logger.getLogger(PaymentServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(PaymentServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    @Override
    public Payment createNewPaymentInstanceWithTransactionId(Payment payment) {
        payment.setTransactionId(AppUtils.getRandomString(30));
        Payment existsWithTransactionId = getPaymentByTransactionId(payment.getTransactionId());
        while (existsWithTransactionId != null) {
            payment.setTransactionId(AppUtils.getRandomString(30));
        }

        try {
            return saveInstance(payment);
        } catch (ValidationFailedException | OperationFailedException ex) {
            Logger.getLogger(PaymentServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;

    }

}
