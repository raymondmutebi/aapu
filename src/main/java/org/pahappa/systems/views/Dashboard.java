package org.pahappa.systems.views;

import com.googlecode.genericdao.search.Search;
import java.io.Serializable;
import java.text.SimpleDateFormat;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.pahappa.systems.models.SystemSetting;
import org.pahappa.systems.security.HyperLinks;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.sers.webutils.client.controllers.WebAppExceptionHandler;
import org.sers.webutils.client.views.presenters.ViewPath;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.primefaces.model.chart.DateAxis;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;
import org.primefaces.model.chart.PieChartModel;
import org.sers.webutils.model.RecordStatus;
import org.pahappa.systems.core.services.SystemSettingService;
import org.pahappa.systems.core.services.MemberService;
import org.sers.webutils.model.security.User;

@ManagedBean
@ViewScoped
@ViewPath(path = HyperLinks.DASHBOARD)
public class Dashboard extends WebAppExceptionHandler implements Serializable {

    private static final long serialVersionUID = 1L;
    private User  loggedinUser;
    private String logoutUrl = "ServiceLogout";
    @SuppressWarnings("unused")
    private String viewPath;
    private int manufacturers;
    private int packages;
    private int totalInboxMessages;
    private int totalOutBoxMessages;
    private int totalOutBoxBatches;
    private int totalSubscribers;
    private SystemSetting appSetting;
    private LineChartModel dateModel;

    private PieChartModel pieModel1;
    private float yoPaymentbalance, egosmsBalance;
    private BarChartModel sectorsBarModel;

    private BarChartModel churchManufacturerBarModel;
    Search search = new Search().addFilterEqual("recordStatus", RecordStatus.ACTIVE);

    @PostConstruct
    public void init() {

        this.appSetting = ApplicationContextProvider.getBean(SystemSettingService.class).getAppSetting();

        this.manufacturers = ApplicationContextProvider.getBean(MemberService.class).countManufacturers(search);
      
       

    }

    public void showReminder(String widgetId, String header, String message) {
        FacesContext context = FacesContext.getCurrentInstance();

        context.addMessage(widgetId, new FacesMessage(FacesMessage.SEVERITY_WARN, header, message));
    }

    private void createDateModel() {
        dateModel = new LineChartModel();
        LineChartSeries series1 = new LineChartSeries();
        series1.setLabel("Daily Devotionals");

        LineChartSeries series2 = new LineChartSeries();
        series2.setLabel("Single/Pdf Books");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        search = new Search();
        search.addFilterEqual("recordStatus", RecordStatus.ACTIVE);
        //search.addFilterGreaterOrEqual("dateCreated", DateUtils.getFirstDateForMonth(DateUtils.currentMonth()));
        // search.addFilterLessOrEqual("dateCreated", DateUtils.getLastDateForMonth(DateUtils.currentMonth()));

        series1.set("2020-01-01", 52000);
        series1.set("2020-02-01", 46700);
        series1.set("2020-03-01", 783000);
        series1.set("2020-04-01", 567000);
        series1.set("2020-05-01", 713000);
        series1.set("2020-06-01", 893000);
        series1.set("2020-07-01", 670000);

        series2.set("2020-01-01", 356000);
        series2.set("2020-02-01", 289000);
        series2.set("2020-03-01", 367000);
        series2.set("2020-04-01", 463000);
        series2.set("2020-05-01", 213000);
        series2.set("2020-06-01", 287000);
        series2.set("2020-07-01", 316000);

        dateModel.addSeries(series1);
        dateModel.addSeries(series2);

      
        dateModel.setZoom(true);
        dateModel.getAxis(AxisType.Y).setLabel("Revenue");
        DateAxis axis = new DateAxis("Dates");
        axis.setTickAngle(-50);
        //axis.setMax("2014-02-01");
        axis.setTickFormat("%b %#d, %y");

        dateModel.getAxes().put(AxisType.X, axis);
    }

    public User getLoggedinUser() {
        return loggedinUser;
    }

    public void setLoggedinUser(User loggedinUser) {
        this.loggedinUser = loggedinUser;
    }

    public String getLogoutUrl() {
        return logoutUrl;
    }

    public void setLogoutUrl(String logoutUrl) {
        this.logoutUrl = logoutUrl;
    }

    public String getViewPath() {
        return viewPath;
    }

    public void setViewPath(String viewPath) {
        this.viewPath = viewPath;
    }

    public int getManufacturers() {
        return manufacturers;
    }

    public void setManufacturers(int manufacturers) {
        this.manufacturers = manufacturers;
    }

    public int getPackages() {
        return packages;
    }

    public void setPackages(int packages) {
        this.packages = packages;
    }

    public int getTotalInboxMessages() {
        return totalInboxMessages;
    }

    public void setTotalInboxMessages(int totalInboxMessages) {
        this.totalInboxMessages = totalInboxMessages;
    }

    public int getTotalOutBoxMessages() {
        return totalOutBoxMessages;
    }

    public void setTotalOutBoxMessages(int totalOutBoxMessages) {
        this.totalOutBoxMessages = totalOutBoxMessages;
    }

    public int getTotalOutBoxBatches() {
        return totalOutBoxBatches;
    }

    public void setTotalOutBoxBatches(int totalOutBoxBatches) {
        this.totalOutBoxBatches = totalOutBoxBatches;
    }

    public int getTotalSubscribers() {
        return totalSubscribers;
    }

    public void setTotalSubscribers(int totalSubscribers) {
        this.totalSubscribers = totalSubscribers;
    }

    public SystemSetting getAppSetting() {
        return appSetting;
    }

    public void setAppSetting(SystemSetting appSetting) {
        this.appSetting = appSetting;
    }

    public LineChartModel getDateModel() {
        return dateModel;
    }

    public void setDateModel(LineChartModel dateModel) {
        this.dateModel = dateModel;
    }

    public PieChartModel getPieModel1() {
        return pieModel1;
    }

    public void setPieModel1(PieChartModel pieModel1) {
        this.pieModel1 = pieModel1;
    }

    public float getYoPaymentbalance() {
        return yoPaymentbalance;
    }

    public void setYoPaymentbalance(float yoPaymentbalance) {
        this.yoPaymentbalance = yoPaymentbalance;
    }

    public float getEgosmsBalance() {
        return egosmsBalance;
    }

    public void setEgosmsBalance(float egosmsBalance) {
        this.egosmsBalance = egosmsBalance;
    }

    public BarChartModel getSectorsBarModel() {
        return sectorsBarModel;
    }

    public void setSectorsBarModel(BarChartModel sectorsBarModel) {
        this.sectorsBarModel = sectorsBarModel;
    }

    public BarChartModel getChurchManufacturerBarModel() {
        return churchManufacturerBarModel;
    }

    public void setChurchManufacturerBarModel(BarChartModel churchManufacturerBarModel) {
        this.churchManufacturerBarModel = churchManufacturerBarModel;
    }

    public Search getSearch() {
        return search;
    }

    public void setSearch(Search search) {
        this.search = search;
    }

    

   

  

}
