package org.pahappa.systems.core.services;

import org.pahappa.systems.models.Member;
import org.pahappa.systems.models.Payment;
import org.pahappa.systems.models.PaymentReasonType;

/**
 * Responsible for CRUD operations on {@link Payment}
 *
 * @author Ray Gdhrt
 *
 */
public interface PaymentService extends GenericService<Payment> {

    /**
     *
     * @param member
     * @param transactionId
     * @return
     * @throws java.lang.Exception
     */
    String initiateRegistrationFeePayment(Member member) throws Exception;

    Payment createNewPaymentInstanceWithTransactionId(Payment payment);
    
    /**
     *
     * @param transactionId
     * @return
     */
    Payment getPaymentByTransactionId(String transactionId);

    /**
     *
     * @param transactionid
     * @param raveId
     * @return
     */
    Payment updatePayment(String transactionid, String raveId);
    
    /**
     * 
     */
     void checkAndUpdatePendingTransactions();

}
