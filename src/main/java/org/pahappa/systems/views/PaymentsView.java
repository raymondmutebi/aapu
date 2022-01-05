package org.pahappa.systems.views;

import com.googlecode.genericdao.search.Search;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.pahappa.systems.constants.TransactionStatus;
import org.pahappa.systems.core.services.PaymentService;
import org.pahappa.systems.core.utils.CustomSearchUtils;
import org.pahappa.systems.models.Payment;
import org.pahappa.systems.models.PaymentReasonType;

import org.pahappa.systems.security.HyperLinks;
import org.sers.webutils.client.views.presenters.PaginatedTableView;
import org.sers.webutils.client.views.presenters.ViewPath;
import org.sers.webutils.model.utils.SortField;
import org.sers.webutils.server.core.service.excel.reports.ExcelReport;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;

@ManagedBean(name = "paymentsView")
@SessionScoped
@ViewPath(path = HyperLinks.PAYMENTS_VIEW)
public class PaymentsView extends PaginatedTableView<Payment, PaymentsView, PaymentsView> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private PaymentService paymentService;
    private String searchTerm;
    private Search search;
    private Payment selectedTransaction;
    private SortField selectedSortField = new SortField("dateCreated", "dateCreated", true);
    private List<SortField> sortFields;
    private List<TransactionStatus> transactionStatuses;
    private List<TransactionStatus> selectedTransactionStatuses;
    private List<PaymentReasonType> paymentReasonTypes;
    private PaymentReasonType selectedReasonType = PaymentReasonType.Registration_fee;

    private int total, success, failed, pending;

    @PostConstruct
    public void init() {
        this.paymentService = ApplicationContextProvider.getBean(PaymentService.class);
        this.transactionStatuses = Arrays.asList(TransactionStatus.values());
        this.paymentReasonTypes = Arrays.asList(PaymentReasonType.values());
        
        super.setMaximumresultsPerpage(10);
        this.reloadFilterReset();
    }

    @Override
    public void reloadFromDB(int offset, int limit, Map<String, Object> arg2) throws Exception {

        super.setDataModels(this.paymentService.getInstances(search, offset, limit));

    }

    @Override
    public void reloadFilterReset() {
        search = CustomSearchUtils.genereateSearchObjectForPayments(searchTerm, selectedTransactionStatuses, selectedReasonType, selectedSortField);
        super.setTotalRecords(this.paymentService.countInstances(search));
        this.total = super.getTotalRecords();
        this.success = this.paymentService.countInstances(search.copy().addFilterEqual("trasanctionStatus", TransactionStatus.SUCESSFULL));
        this.failed = this.paymentService.countInstances(search.copy().addFilterEqual("trasanctionStatus", TransactionStatus.FAILED));
        this.pending = this.paymentService.countInstances(search.copy().addFilterEqual("trasanctionStatus", TransactionStatus.PENDING));
        this.resetInput();
    }

    public void fetchTransactions() {

        // ApplicationContextProvider.getBean(PaymentService.class).checkAndUpdatePendingTransactions();
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

    public Payment getSelectedTransaction() {
        return selectedTransaction;
    }

    public void setSelectedTransaction(Payment selectedTransaction) {
        this.selectedTransaction = selectedTransaction;
    }

    private void resetInput() {
        this.searchTerm = "";
    }

    public List<TransactionStatus> getTransactionStatuses() {
        return transactionStatuses;
    }

    public void setTransactionStatuses(List<TransactionStatus> transactionStatuses) {
        this.transactionStatuses = transactionStatuses;
    }

    public List<TransactionStatus> getSelectedTransactionStatuses() {
        return selectedTransactionStatuses;
    }

    public void setSelectedTransactionStatuses(List<TransactionStatus> selectedTransactionStatuses) {
        this.selectedTransactionStatuses = selectedTransactionStatuses;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public int getFailed() {
        return failed;
    }

    public void setFailed(int failed) {
        this.failed = failed;
    }

    public int getPending() {
        return pending;
    }

    public void setPending(int pending) {
        this.pending = pending;
    }

    public List<PaymentReasonType> getPaymentReasonTypes() {
        return paymentReasonTypes;
    }

    public void setPaymentReasonTypes(List<PaymentReasonType> paymentReasonTypes) {
        this.paymentReasonTypes = paymentReasonTypes;
    }

    public PaymentReasonType getSelectedReasonType() {
        return selectedReasonType;
    }

    public void setSelectedReasonType(PaymentReasonType selectedReasonType) {
        this.selectedReasonType = selectedReasonType;
    }
    
    

}
