package org.pahappa.systems.views;

import com.googlecode.genericdao.search.Search;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.pahappa.systems.constants.CommunicationType;

import org.pahappa.systems.models.Communication;
import org.pahappa.systems.security.HyperLinks;
import org.sers.webutils.client.views.presenters.PaginatedTableView;
import org.sers.webutils.client.views.presenters.ViewPath;
import org.sers.webutils.model.RecordStatus;
import org.sers.webutils.server.core.service.excel.reports.ExcelReport;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;
import org.pahappa.systems.core.services.CommunicationService;
import org.pahappa.systems.core.utils.UiUtils;
import org.sers.webutils.model.exception.OperationFailedException;
import org.sers.webutils.model.exception.ValidationFailedException;

@ManagedBean(name = "communicationsView")
@SessionScoped
@ViewPath(path = HyperLinks.COMMUNICATIONS_VIEW)
public class CommunicationsView extends PaginatedTableView<Communication, CommunicationsView, CommunicationsView> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private CommunicationService communicationService;
    private String searchTerm;
    private Communication selectedCommunication;

    Search search = new Search().addFilterEqual("recordStatus", RecordStatus.ACTIVE);

    @PostConstruct
    public void init() {
        this.communicationService = ApplicationContextProvider.getBean(CommunicationService.class);
        super.setMaximumresultsPerpage(10);
    }

    @Override
    public void reloadFromDB(int offset, int limit, Map<String, Object> arg2) throws Exception {
        super.setDataModels(this.communicationService.getInstances(search, offset, limit));
    }

    @Override
    public void reloadFilterReset() {
        super.setTotalRecords(this.communicationService.countInstances(search));
        this.resetInput();
    }

    public void loadCommunication(Communication communication) {
        System.out.println("Loaded new communication..");
        if (communication == null) {
            communication = new Communication();
        }

        this.selectedCommunication = communication;

    }

    public void saveSMS(Communication communication) {
         System.out.println("Saving sms communication.. at"+communication.getScheduleDate().toString());
        communication.setCommunicationType(CommunicationType.Sms);
        try {
            this.communicationService.saveInstance(communication);
        } catch (ValidationFailedException | OperationFailedException ex) {
            Logger.getLogger(CommunicationsView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
     public void saveEmail(Communication communication) {
         System.out.println("Saving email communication..at"+communication.getScheduleDate().toString());
        communication.setCommunicationType(CommunicationType.Email);
        try {
            this.communicationService.saveInstance(communication);
        } catch (ValidationFailedException | OperationFailedException ex) {
            Logger.getLogger(CommunicationsView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public List<ExcelReport> getExcelReportModels() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getFileName() {
        // TODO Auto-generated method stub
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

    public void deleteManufacturer(Communication communication) {
        try {
            this.communicationService.deleteInstance(communication);
            super.reloadFilterReset();
            UiUtils.showMessageBox("Message has been deleted", "Action Successful");
        } catch (Exception e) {
            UiUtils.showMessageBox(e.getMessage(), "Action failed");
        }
    }

    private void resetInput() {
        this.searchTerm = "";
    }

    public Communication getSelectedCommunication() {
        return selectedCommunication;
    }

    public void setSelectedCommunication(Communication selectedCommunication) {
        this.selectedCommunication = selectedCommunication;
    }

}
