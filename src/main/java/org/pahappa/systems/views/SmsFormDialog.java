/*
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pahappa.systems.views;

import java.util.Date;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.pahappa.systems.constants.CommunicationType;
import org.pahappa.systems.core.services.CommunicationService;
import org.pahappa.systems.models.Communication;
import org.pahappa.systems.security.HyperLinks;
import org.sers.webutils.model.exception.OperationFailedException;
import org.sers.webutils.model.exception.ValidationFailedException;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;

/**
 *
 * @author RayGdhrt
 */
@ManagedBean(name = "smsFormDialog", eager = true)
@SessionScoped
public class SmsFormDialog extends DialogForm<Communication> {
    
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(EmailFormDialog.class.getSimpleName());
    
    private CommunicationService currencyService;
    private Boolean immediate = false;
    
    
    
    @PostConstruct
    public void init() {
        
        this.currencyService = ApplicationContextProvider.getApplicationContext().getBean(CommunicationService.class);
        
    }
    
    public SmsFormDialog() {
        super(HyperLinks.SMS_DIALOG, 800, 500);
    }
    
    @Override
    public void persist() throws ValidationFailedException, OperationFailedException {
        this.model.setCommunicationType(CommunicationType.Sms);
        if (this.model.getScheduleDate() == null) {
            this.model.setScheduleDate(new Date());
             this.model.setScheduleTime(new Date());
        }
         System.out.println("Saving SMS communication..at "+super.model.getScheduleDate().toString());
        this.currencyService.saveInstance(super.model,immediate);
        
    }
    
    @Override
    public void resetModal() {
        super.resetModal();
        this.immediate= null;
        super.model = new Communication();
    }
    
    @Override
    public void setFormProperties() {
        super.setFormProperties();
    }

    public Boolean getImmediate() {
        return immediate;
    }

    public void setImmediate(Boolean immediate) {
        this.immediate = immediate;
    }
    
    
    
}
