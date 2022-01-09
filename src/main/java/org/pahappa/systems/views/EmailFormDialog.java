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
@ManagedBean(name = "emailFormDialog", eager = true)
@SessionScoped
public class EmailFormDialog extends DialogForm<Communication> {
    
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(EmailFormDialog.class.getSimpleName());
    
    private CommunicationService currencyService;
    
    @PostConstruct
    public void init() {
        
        this.currencyService = ApplicationContextProvider.getApplicationContext().getBean(CommunicationService.class);
        
    }
    
    public EmailFormDialog() {
        super(HyperLinks.EMAIL_DIALOG, 800, 600);
    }
    
    @Override
    public void persist() throws ValidationFailedException, OperationFailedException {
        this.model.setCommunicationType(CommunicationType.Email);
        if (this.model.getScheduleDate() == null) {
            this.model.setScheduleDate(new Date());
             this.model.setScheduleTime(new Date());
        }
        this.currencyService.saveInstance(super.model);
        
    }
    
    @Override
    public void resetModal() {
        super.resetModal();
        super.model = new Communication();
    }
    
    @Override
    public void setFormProperties() {
        super.setFormProperties();
    }
    
}
