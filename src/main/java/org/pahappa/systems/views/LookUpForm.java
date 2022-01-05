package org.pahappa.systems.views;

import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.pahappa.systems.core.services.LookUpService;
import org.pahappa.systems.core.utils.UiUtils;

import org.pahappa.systems.models.LookUpField;
import org.pahappa.systems.models.LookUpValue;
import org.pahappa.systems.security.HyperLinks;
import org.primefaces.event.CellEditEvent;
import org.sers.webutils.client.views.presenters.ViewPath;
import org.sers.webutils.client.views.presenters.WebFormView;
import org.sers.webutils.model.exception.ValidationFailedException;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;

@ManagedBean(name = "lookUpForm")
@SessionScoped
@ViewPath(path = HyperLinks.LOOKUP_FORM)
public class LookUpForm extends WebFormView<LookUpField, LookUpForm, LookUpsView> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private LookUpService lookUpFieldService;

    private String lookUpFieldName = "";
    private String lookUpFieldNumber = "";
    private boolean newView;
    private String[] stringLookupValues;
    private LookUpValue selectedLookUpValue;

    @Override
    public void beanInit() {

        this.lookUpFieldService = ApplicationContextProvider.getBean(LookUpService.class);
        this.resetModal();

    }

    @Override
    public void pageLoadInit() {

    }

    @Override
    public void persist() throws Exception {

        this.lookUpFieldService.save(super.model);
    }

    public void loadLookupValue(LookUpValue lookUpValue) {
        if (lookUpValue == null) {
            lookUpValue = new LookUpValue();
        }
        this.selectedLookUpValue = lookUpValue;

    }

    @Override
    public void resetModal() {
        super.resetModal();
        super.model = new LookUpField();
    }

    @Override
    public void setFormProperties() {
        super.setFormProperties();
    }

    public void saveNewValue(LookUpValue lookUpValue) {

        try {
            super.model.addLookupValue(lookUpValue);
            super.model = lookUpFieldService.save(super.model);
            UiUtils.showMessageBox("Value saved", "Action successfull");

        } catch (ValidationFailedException ex) {
            UiUtils.ComposeFailure(ex.getLocalizedMessage(), "Action failed");

        }

    }

    public void removeLookUpValue(LookUpValue lookUpValue) {
        super.model.removeLookupValue(lookUpValue);

    }

    public void onCellEdit(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();

        if (newValue != null && !newValue.equals(oldValue)) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Change made", oldValue + " changed to " + newValue);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    @Override
    public String getViewUrl() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @return the newView
     */
    public boolean isNewView() {
        return newView;
    }

    /**
     * @param newView the newView to set
     */
    public void setNewView(boolean newView) {
        this.newView = newView;
    }

    public String getLookUpFieldName() {
        return lookUpFieldName;
    }

    public void setLookUpFieldName(String lookUpFieldName) {
        this.lookUpFieldName = lookUpFieldName;
    }

    public String getLookUpFieldNumber() {
        return lookUpFieldNumber;
    }

    public void setLookUpFieldNumber(String lookUpFieldNumber) {
        this.lookUpFieldNumber = lookUpFieldNumber;
    }

    public String[] getStringLookupValues() {
        return stringLookupValues;
    }

    public void setStringLookupValues(String[] stringLookupValues) {
        this.stringLookupValues = stringLookupValues;
    }

    public LookUpService getLookUpFieldService() {
        return lookUpFieldService;
    }

    public void setLookUpFieldService(LookUpService lookUpFieldService) {
        this.lookUpFieldService = lookUpFieldService;
    }

    public LookUpValue getSelectedLookUpValue() {
        return selectedLookUpValue;
    }

    public void setSelectedLookUpValue(LookUpValue selectedLookUpValue) {
        this.selectedLookUpValue = selectedLookUpValue;
    }

}
