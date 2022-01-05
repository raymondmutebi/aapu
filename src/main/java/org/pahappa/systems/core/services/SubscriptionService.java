package org.pahappa.systems.core.services;

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
     */
   void subscriptionObserver() ;

}
