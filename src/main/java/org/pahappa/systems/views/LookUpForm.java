package org.pahappa.systems.views;

import java.util.HashSet;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.pahappa.systems.core.services.LookUpService;

import org.pahappa.systems.models.LookUpField;
import org.pahappa.systems.models.LookUpValue;
import org.pahappa.systems.security.HyperLinks;
import org.primefaces.event.CellEditEvent;
import org.sers.webutils.client.views.presenters.ViewPath;
import org.sers.webutils.client.views.presenters.WebFormView;
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

    public void loadPastedValues() {
        super.model.setLookUpValues(new HashSet<LookUpValue>());
        for (String stringValue : stringLookupValues) {
            LookUpValue newLookUpValue = new LookUpValue();
            newLookUpValue.setName(stringValue);
            super.model.addLookupValue(newLookUpValue);

        }

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

    public void addNewValue() {
        LookUpValue newLookUpValue = new LookUpValue();
        newLookUpValue.setName("New value");
        super.model.addLookupValue(newLookUpValue);

    }

    public void removeLookUpValue(LookUpValue lookUpValue) {
        super.model.removeLookupValue(lookUpValue);

    }

    public void onCellEdit(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();

        if (newValue != null && !newValue.equals(oldValue)) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Change made",  oldValue + " changed to " + newValue);
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

}
