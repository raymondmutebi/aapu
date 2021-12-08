/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.pahappa.systems.models;

import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import org.pahappa.systems.constants.SubscriptionStatus;
import org.sers.webutils.model.BaseEntity;

/**
 *
 * @author RayGdhrt
 */
@Entity
@Table(name = "subscriptions")
public class Subscription extends BaseEntity{

    private Date startDate;
    private Date endDate;
    private int duration = 365;
    private float amountPaid;
    private Member member;
    private float charge;
    private SubscriptionStatus status = SubscriptionStatus.ACTIVE;


     @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "member_id")
    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }
 

    @Temporal(javax.persistence.TemporalType.DATE)
    @Column(name = "start_date", nullable = true)
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @Temporal(javax.persistence.TemporalType.DATE)
    @Column(name = "end_date", nullable = true)
    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Column(name = "duration", nullable = true)
    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Column(name = "amount", nullable = true)
    public float getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(float amountPaid) {
        this.amountPaid = amountPaid;
    }

    /**
	 * @return the charge
	 */
    @Column(name = "charge", nullable = true)
	public float getCharge() {
		return charge;
	}

	/**
	 * @param charge the charge to set
	 */
	public void setCharge(float charge) {
		this.charge = charge;
	}

	@Enumerated(EnumType.ORDINAL)
    @Column(name = "status", nullable = true)
    public SubscriptionStatus getStatus() {
        return status;
    }

    public void setStatus(SubscriptionStatus status) {
        this.status = status;
    }
    
    

    @Override
    public String toString() {
        return "";
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof Subscription && (super.getId() != null)
                ? super.getId().equals(((Subscription) object).getId())
                : (object == this);
    }

    @Override
    public int hashCode() {
        return super.getId() != null ? this.getClass().hashCode() + super.getId().hashCode() : super.hashCode();
    }

}
