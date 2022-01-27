package org.pahappa.systems.views;

import com.googlecode.genericdao.search.Search;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.pahappa.systems.models.SystemSetting;
import org.pahappa.systems.security.HyperLinks;
import org.sers.webutils.client.controllers.WebAppExceptionHandler;
import org.sers.webutils.client.views.presenters.ViewPath;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.pahappa.systems.constants.AccountStatus;
import org.pahappa.systems.constants.CommunicationType;
import org.pahappa.systems.core.services.CommunicationService;
import org.sers.webutils.model.RecordStatus;
import org.pahappa.systems.core.services.SystemSettingService;
import org.pahappa.systems.core.services.MemberService;
import org.pahappa.systems.core.services.PaymentService;
import org.pahappa.systems.core.services.SubscriptionService;
import org.pahappa.systems.models.Communication;
import org.pahappa.systems.models.Member;
import org.pahappa.systems.models.Payment;
import org.pahappa.systems.models.PaymentReasonType;
import org.pahappa.systems.models.Subscription;
import org.pahappa.systems.constants.TransactionStatus;
import org.pahappa.systems.core.services.CourseService;
import org.pahappa.systems.core.utils.wp.WordPressClient;
import org.pahappa.systems.core.utils.wp.WordPressPost;
import org.pahappa.systems.models.Course;

import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.axes.cartesian.CartesianScales;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearAxes;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearTicks;
import org.primefaces.model.charts.bar.BarChartDataSet;
import org.primefaces.model.charts.bar.BarChartModel;
import org.primefaces.model.charts.bar.BarChartOptions;
import org.primefaces.model.charts.optionconfig.title.Title;
import org.sers.webutils.model.security.User;
import org.sers.webutils.server.core.utils.DateUtils;
import org.sers.webutils.server.shared.SharedAppData;

@ManagedBean
@ViewScoped
@ViewPath(path = HyperLinks.DASHBOARD)
public class Dashboard extends WebAppExceptionHandler implements Serializable {

    private static final long serialVersionUID = 1L;
    private User loggedinUser;
    private Member loggedInMember;
    private String logoutUrl = "ServiceLogout";
    @SuppressWarnings("unused")
    private String viewPath, subscriptionStatus;
    private int totalMembers, monthySubscriptions, monthyCommunications, dailymembers;
    private int subscripionDaysRemaining, myTotalSubscriptions, subscriptionAmount;
    private List<Member> members;
    private List<Communication> smsCommunications, emailCommunications;
    private Subscription activeSubscription;
    private SubscriptionService subscriptionService;
    private List<Subscription> memberSubscriptions;
    private SystemSetting appSetting;
    private MemberService memberService;
    private CommunicationService communicationService;
    private BarChartModel clientsBarChartModel;
    private List<WordPressPost> previousPosts;
    private List<Course> courses;
    private Course selecetedCourse;

    private float egosmsBalance;
    private float totalRegistartionPayment, totalSubscriptionPayment;

    Search search = new Search().addFilterEqual("recordStatus", RecordStatus.ACTIVE);

    @PostConstruct
    public void init() {

        this.appSetting = ApplicationContextProvider.getBean(SystemSettingService.class).getAppSetting();
        this.totalMembers = ApplicationContextProvider.getBean(MemberService.class).countMembers(search);
        this.communicationService = ApplicationContextProvider.getBean(CommunicationService.class);
        this.memberService = ApplicationContextProvider.getBean(MemberService.class);
        this.subscriptionService = ApplicationContextProvider.getBean(SubscriptionService.class);
        this.loggedinUser = SharedAppData.getLoggedInUser();

        if (loggedinUser.hasAdministrativePrivileges()) {
            initSystemAdminData();
        } else {
            initMemberData();
        }

    }

    public void showReminder(String widgetId, String header, String message) {
        FacesContext context = FacesContext.getCurrentInstance();

        context.addMessage(widgetId, new FacesMessage(FacesMessage.SEVERITY_WARN, header, message));
    }

    public void showProfileUpdateReminder() {

    }

    public void initMemberData() {
        Date endDate = new Date();
        subscripionDaysRemaining = 0;
        subscriptionStatus = "Expired";
        this.loggedInMember = this.memberService.getMemberByUserAccount(loggedinUser);
        this.activeSubscription = this.subscriptionService.getActiveSubscription(loggedInMember);
        if (this.activeSubscription != null) {
            endDate = activeSubscription.getEndDate();
            subscripionDaysRemaining = (int) DateUtils.calculateDaysBetween(new Date(), activeSubscription.getEndDate());
            subscriptionStatus = "Active";
            this.previousPosts = loadPosts();
            this.courses = ApplicationContextProvider.getBean(CourseService.class).getInstances(new Search()
                    .addFilterEqual("recordStatus", RecordStatus.ACTIVE)
                    .addFilterGreaterOrEqual("endDate", new Date()), 0, 0);
            this.smsCommunications = this.communicationService.getInstances(new Search()
                    .addFilterEqual("communicationType", CommunicationType.Sms)
                    .addFilterLessOrEqual("scheduleDate", DateUtils.getDateAfterDays(endDate, 3)), 0, 0);

            this.emailCommunications = this.communicationService.getInstances(new Search()
                    .addFilterEqual("communicationType", CommunicationType.Email)
                    .addFilterLessOrEqual("scheduleDate", DateUtils.getDateAfterDays(endDate, 3)), 0, 0);

        }

        this.memberSubscriptions = this.subscriptionService.getInstances(new Search()
                .addFilterEqual("member", this.loggedInMember), 0, 0);

    }

    public void initSystemAdminData() {
        this.monthyCommunications = this.communicationService.countInstances(new Search()
                .addFilterGreaterOrEqual("dateCreated", DateUtils.getFirstDateOfThisMonth()));

        this.monthySubscriptions = this.subscriptionService.countInstances(new Search()
                .addFilterGreaterOrEqual("dateCreated", DateUtils.getFirstDateOfThisMonth()));

        this.totalMembers = this.memberService.countInstances(new Search()
                .addFilterNotEqual("accountStatus", AccountStatus.Created)
                .addFilterNotEqual("accountStatus", AccountStatus.Verified));

        this.dailymembers = this.memberService.countInstances(new Search()
                .addFilterGreaterOrEqual("dateCreated", DateUtils.getDateAfterDays(-1)));

        createMembersBarModel();
    }

    public String initiateSubscriptionPayment() {
        Payment payment = new Payment();

        payment.setReasonType(PaymentReasonType.Subscription);
        payment.setAmount(appSetting.getSubscriptionFee());
        payment.setMember(loggedInMember);
        payment.setTrasanctionStatus(TransactionStatus.INITIATED);
        payment = ApplicationContextProvider.getBean(PaymentService.class).createNewPaymentInstanceWithTransactionId(payment);

        return payment.getTransactionId();
    }

    public void createMembersBarModel() {
        clientsBarChartModel = new BarChartModel();
        ChartData data = new ChartData();

        BarChartDataSet hbarDataSet = new BarChartDataSet();
        hbarDataSet.setLabel("Number of clients");

        List<Number> values = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        List<String> bgColor = new ArrayList<>();
        List<String> borderColor = new ArrayList<>();
        Search clientCountGraphSearch;

        for (int monthNumber = 1; monthNumber <= DateUtils.currentMonth() + 1; monthNumber++) {

            clientCountGraphSearch = new Search();
            clientCountGraphSearch.addFilterEqual("recordStatus", RecordStatus.ACTIVE);
            clientCountGraphSearch.addFilterNotEqual("accountStatus", AccountStatus.Created)
                    .addFilterNotEqual("accountStatus", AccountStatus.Verified)
                    .addFilterGreaterOrEqual("dateCreated", DateUtils.getFirstDateForMonth(monthNumber, DateUtils.currentYear()));
            clientCountGraphSearch.addFilterLessOrEqual("dateCreated", DateUtils.getLastDateForMonth(monthNumber, DateUtils.currentYear()));
            int numberOfClients = memberService.countInstances(clientCountGraphSearch);
            values.add(numberOfClients);
            labels.add(new SimpleDateFormat("MMM").format(DateUtils.getFirstDateForMonth(monthNumber, DateUtils.currentYear())));

            Random random = new Random();

            int colorR = random.nextInt(255);
            int colorG = random.nextInt(255);
            int colorB = random.nextInt(255);
            bgColor.add("rgba(" + colorR + "," + colorG + "," + colorB + ", 0.2)");
            borderColor.add("rgb(" + colorR + "," + colorG + "," + colorB);
        }

        hbarDataSet.setData(values);
        hbarDataSet.setBackgroundColor(bgColor);
        hbarDataSet.setBorderColor(borderColor);
        hbarDataSet.setBorderWidth(1);
        data.addChartDataSet(hbarDataSet);
        data.setLabels(labels);
        clientsBarChartModel.setData(data);

        //Options
        BarChartOptions options = new BarChartOptions();
        CartesianScales cScales = new CartesianScales();
        CartesianLinearAxes linearAxes = new CartesianLinearAxes();
        linearAxes.setOffset(true);
        CartesianLinearTicks ticks = new CartesianLinearTicks();
        ticks.setBeginAtZero(true);
        linearAxes.setTicks(ticks);
        cScales.addXAxesData(linearAxes);
        options.setScales(cScales);

        Title title = new Title();
        title.setDisplay(false);
        title.setText("Your clients this year");
        options.setTitle(title);

        clientsBarChartModel.setOptions(options);
    }

    public Course getSelecetedCourse() {
        return selecetedCourse;
    }

    public void setSelecetedCourse(Course selecetedCourse) {
        this.selecetedCourse = selecetedCourse;
    }

    public List<WordPressPost> loadPosts() {

        try {
            return new WordPressClient().fetchPosts("..");
        } catch (IOException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }

        return new ArrayList<>();

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

    public int getTotalMembers() {
        return totalMembers;
    }

    public void setTotalMembers(int totalMembers) {
        this.totalMembers = totalMembers;
    }

    public int getMonthySubscriptions() {
        return monthySubscriptions;
    }

    public void setMonthySubscriptions(int monthySubscriptions) {
        this.monthySubscriptions = monthySubscriptions;
    }

    public int getMonthyCommunications() {
        return monthyCommunications;
    }

    public void setMonthyCommunications(int monthyCommunications) {
        this.monthyCommunications = monthyCommunications;
    }

    public int getSubscripionDaysRemaining() {
        return subscripionDaysRemaining;
    }

    public void setSubscripionDaysRemaining(int subscripionDaysRemaining) {
        this.subscripionDaysRemaining = subscripionDaysRemaining;
    }

    public int getMyTotalSubscriptions() {
        return myTotalSubscriptions;
    }

    public void setMyTotalSubscriptions(int myTotalSubscriptions) {
        this.myTotalSubscriptions = myTotalSubscriptions;
    }

    public List<Communication> getSmsCommunications() {
        return smsCommunications;
    }

    public void setSmsCommunications(List<Communication> smsCommunications) {
        this.smsCommunications = smsCommunications;
    }

    public List<Communication> getEmailCommunications() {
        return emailCommunications;
    }

    public void setEmailCommunications(List<Communication> emailCommunications) {
        this.emailCommunications = emailCommunications;
    }

    public float getEgosmsBalance() {
        return egosmsBalance;
    }

    public void setEgosmsBalance(float egosmsBalance) {
        this.egosmsBalance = egosmsBalance;
    }

    public Member getLoggedInMember() {
        return loggedInMember;
    }

    public void setLoggedInMember(Member loggedInMember) {
        this.loggedInMember = loggedInMember;
    }

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }

    public Subscription getActiveSubscription() {
        return activeSubscription;
    }

    public void setActiveSubscription(Subscription activeSubscription) {
        this.activeSubscription = activeSubscription;
    }

    public List<Subscription> getMemberSubscriptions() {
        return memberSubscriptions;
    }

    public void setMemberSubscriptions(List<Subscription> memberSubscriptions) {
        this.memberSubscriptions = memberSubscriptions;
    }

    public int getSubscriptionAmount() {
        return subscriptionAmount;
    }

    public void setSubscriptionAmount(int subscriptionAmount) {
        this.subscriptionAmount = subscriptionAmount;
    }

    public SystemSetting getAppSetting() {
        return appSetting;
    }

    public void setAppSetting(SystemSetting appSetting) {
        this.appSetting = appSetting;
    }

    public BarChartModel getClientsBarChartModel() {
        return clientsBarChartModel;
    }

    public void setClientsBarChartModel(BarChartModel clientsBarChartModel) {
        this.clientsBarChartModel = clientsBarChartModel;
    }

    public List<WordPressPost> getPreviousPosts() {
        return previousPosts;
    }

    public void setPreviousPosts(List<WordPressPost> previousPosts) {
        this.previousPosts = previousPosts;
    }

    public String getSubscriptionStatus() {
        return subscriptionStatus;
    }

    public void setSubscriptionStatus(String subscriptionStatus) {
        this.subscriptionStatus = subscriptionStatus;
    }

    public int getDailymembers() {
        return dailymembers;
    }

    public void setDailymembers(int dailymembers) {
        this.dailymembers = dailymembers;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public float getTotalRegistartionPayment() {
        return totalRegistartionPayment;
    }

    public void setTotalRegistartionPayment(float totalRegistartionPayment) {
        this.totalRegistartionPayment = totalRegistartionPayment;
    }

    public float getTotalSubscriptionPayment() {
        return totalSubscriptionPayment;
    }

    public void setTotalSubscriptionPayment(float totalSubscriptionPayment) {
        this.totalSubscriptionPayment = totalSubscriptionPayment;
    }

}
