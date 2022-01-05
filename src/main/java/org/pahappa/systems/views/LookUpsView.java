package org.pahappa.systems.views;

import com.googlecode.genericdao.search.Search;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.pahappa.systems.security.HyperLinks;
import org.sers.webutils.server.core.service.excel.reports.ExcelReport;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;
import org.sers.webutils.model.RecordStatus;
import org.pahappa.systems.core.services.LookUpService;
import org.pahappa.systems.core.utils.UiUtils;
import org.pahappa.systems.models.LookUpField;
import org.sers.webutils.client.views.presenters.PaginatedTableView;
import org.sers.webutils.client.views.presenters.ViewPath;
import org.sers.webutils.model.exception.ValidationFailedException;

@ManagedBean(name = "lookUpsView")
@SessionScoped
@ViewPath(path = HyperLinks.LOOKUPS_VIEW)
public class LookUpsView extends PaginatedTableView<LookUpField, LookUpsView, LookUpsView> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private LookUpService lookUpFieldService;
    private String searchTerm;
    private LookUpField selectedLookUpField;
    
    private Search search = new Search().addFilterEqual("recordStatus", RecordStatus.ACTIVE).addSortDesc("dateCreated");

    @PostConstruct
    public void init() {
        this.lookUpFieldService = ApplicationContextProvider.getBean(LookUpService.class);
        super.setMaximumresultsPerpage(10);
    }

    @Override
    public void reloadFromDB(int offset, int limit, Map<String, Object> arg2) throws Exception {
        super.setDataModels(this.lookUpFieldService.getLookUpFields(search, offset, limit));
    }

    @Override
    public void reloadFilterReset() {
        super.setTotalRecords(this.lookUpFieldService.countLookUpFields(search));
        this.resetInput();
    }

    @Override
    public List<ExcelReport> getExcelReportModels() {
       
        return null;
    }

    public void saveLookUpField(LookUpField lookUpField) {

        try {

            this.lookUpFieldService.save(lookUpField);
            reloadFilterReset();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Action successfull"));

        } catch (ValidationFailedException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Action failed"));

            Logger.getLogger(LookUpsView.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void loadSelectedLookUp(LookUpField lookUpField) {
        
        if(lookUpField==null){
        this.selectedLookUpField = new LookUpField();
        }else{
        this.selectedLookUpField = lookUpField;
        }
    }

    @Override
    public String getFileName() {
       
        return null;
    }

    /**
     * @return the searchTerm
     */
    public String getSearchTerm() {
        return searchTerm;
    }

    /**
     * @param searchTerm the searchTerm to set
     */
    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    public LookUpService getLookUpFieldService() {
        return lookUpFieldService;
    }

    public void setLookUpFieldService(LookUpService lookUpFieldService) {
        this.lookUpFieldService = lookUpFieldService;
    }

    public LookUpField getSelectedLookUpField() {
        return selectedLookUpField;
    }

    public void setSelectedLookUpField(LookUpField selectedLookUpField) {
        this.selectedLookUpField = selectedLookUpField;
    }

    public void deleteLookUp(LookUpField lookUpField) {
        try {
            this.lookUpFieldService.delete(lookUpField);
            super.reloadFilterReset();
            UiUtils.showMessageBox(lookUpField.getName() + " has been deleted", "Action Successful");
        } catch (Exception e) {
           UiUtils.showMessageBox(e.getMessage(), "Action failed");
        }
    }

    private void resetInput() {
        this.searchTerm = "";
    }

   

}
