package org.pahappa.systems.views;

import com.googlecode.genericdao.search.Search;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.pahappa.systems.core.services.MemberService;

import org.pahappa.systems.core.services.SubscriptionService;
import org.pahappa.systems.models.Subscription;
import org.pahappa.systems.security.HyperLinks;
import org.sers.webutils.client.views.presenters.PaginatedTableView;
import org.sers.webutils.client.views.presenters.ViewPath;
import org.sers.webutils.model.RecordStatus;
import org.sers.webutils.model.utils.SortField;
import org.sers.webutils.server.core.service.excel.reports.ExcelReport;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;
import org.sers.webutils.server.shared.SharedAppData;

@ManagedBean(name = "subscriptionsView")
@SessionScoped
@ViewPath(path = HyperLinks.SUBSCRIPTIONS_VIEW)
public class SubscriptionsView extends PaginatedTableView<Subscription, SubscriptionsView, SubscriptionsView> {

    private static final long serialVersionUID = 1L;
    private SubscriptionService subscriptionService;
    private String searchTerm;
    private Search search = new Search().addFilterEqual("recordStatus", RecordStatus.ACTIVE);
    private SortField selectedSortField = new SortField("dateCreated", "dateCreated", true);
    private List<SortField> sortFields;
    private Date startDate = null;
    private Date endDate = null;

    @PostConstruct
    @Override
    public void init() {
        this.subscriptionService = ApplicationContextProvider.getBean(SubscriptionService.class);
        super.setMaximumresultsPerpage(10);
        reloadFilterReset();
    }

    @Override
    public void reloadFromDB(int offset, int limit, Map<String, Object> arg2) throws Exception {
        super.setDataModels(this.subscriptionService.getInstances(search, offset, limit));

    }

    @Override
    public void reloadFilterReset() {
        search = new Search().addFilterEqual("recordStatus", RecordStatus.ACTIVE);
        if (!SharedAppData.getLoggedInUser().hasAdministrativePrivileges()) {

            search.addFilterEqual("member", ApplicationContextProvider.getBean(MemberService.class).getMemberByUserAccount(SharedAppData.getLoggedInUser()));
        }

        super.setTotalRecords(this.subscriptionService.countInstances(search));

        this.resetInput();
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

    public SubscriptionService getPaymentService() {
        return subscriptionService;
    }

    public void setPaymentService(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    public SortField getSelectedSortField() {
        return selectedSortField;
    }

    public void setSelectedSortField(SortField selectedSortField) {
        this.selectedSortField = selectedSortField;
    }

    public List<SortField> getSortFields() {
        return sortFields;
    }

    public void setSortFields(List<SortField> sortFields) {
        this.sortFields = sortFields;
    }

    public SubscriptionService getSubscriptionService() {
        return subscriptionService;
    }

    public void setSubscriptionService(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    private void resetInput() {
        this.searchTerm = "";
    }

}
