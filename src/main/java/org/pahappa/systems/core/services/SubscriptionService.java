package org.pahappa.systems.core.services;

import java.util.Date;
import org.pahappa.systems.models.Member;
import org.pahappa.systems.models.Payment;
import org.pahappa.systems.models.Subscription;

/**
 * Responsible for CRUD operations on {@link Payment}
 *
 * @author Ray Gdhrt
 *
 */
public interface SubscriptionService extends GenericService<Subscription> {

    /**
     * 
     * @param payment
     * @return 
     */
    public Subscription createNewSubscription(Payment payment);
    
    
     /**
     * 
     * @param member
     * @param payment
     * @return 
     */
    public Subscription getActiveSubscription(Member member);
    
    /**
     * 
     * @param member
     * @param startDate
     * @param attachment
     * @return 
     */
    public Subscription extendSubscription(Member member, Date startDate, byte[] attachment) ;
   
    /**
     * 
     */
   void subscriptionObserver() ;

}
