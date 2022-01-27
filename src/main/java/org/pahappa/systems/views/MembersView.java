package org.pahappa.systems.views;

import com.googlecode.genericdao.search.Search;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.apache.commons.io.IOUtils;
import org.pahappa.systems.constants.AccountStatus;
import org.pahappa.systems.constants.MemberRegistrationType;
import org.pahappa.systems.constants.Region;

import org.pahappa.systems.models.Member;
import org.pahappa.systems.security.HyperLinks;
import org.sers.webutils.client.views.presenters.PaginatedTableView;
import org.sers.webutils.client.views.presenters.ViewPath;
import org.sers.webutils.model.RecordStatus;
import org.sers.webutils.server.core.service.excel.reports.ExcelReport;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;
import org.pahappa.systems.core.services.MemberService;
import org.pahappa.systems.core.services.SubscriptionService;
import org.pahappa.systems.core.utils.AppUtils;
import org.pahappa.systems.core.utils.CustomSearchUtils;
import org.pahappa.systems.core.utils.ExcelUploadHelper;
import org.pahappa.systems.core.utils.UiUtils;
import org.pahappa.systems.models.ProfessionValue;
import org.pahappa.systems.models.Subscription;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.sers.webutils.model.exception.OperationFailedException;
import org.sers.webutils.model.exception.ValidationFailedException;
import org.sers.webutils.model.utils.SortField;

@ManagedBean(name = "membersView")
@SessionScoped
@ViewPath(path = HyperLinks.MEMBERS_VIEW)
public class MembersView extends PaginatedTableView<Member, MembersView, MembersView> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private MemberService memberService;
    private Member selectedMember;
    private String searchTerm, counterMessage, blockNotes;
    private int total, regisered, created, verified, blocked;
    private List<SortField> sortFields;
    private SortField selectedSortField = new SortField("dateCreated", "dateCreated", true);
    private List<Region> regions;
    private Region selectedRegion;
    private List<ProfessionValue> professions;
    private ProfessionValue selectedProfession;
    private List<MemberRegistrationType> registrationTypes;
    private MemberRegistrationType selectedRegistrationType;
    private List<AccountStatus> accountStatuses;
    private List<Member> uploadedMembers;
    private AccountStatus selectedAccountStatus;
    private Search search;
    private byte[] attachement;
    private Date startDate;

    @PostConstruct
    public void init() {
        this.memberService = ApplicationContextProvider.getBean(MemberService.class);
        this.regions = Arrays.asList(Region.values());
        this.professions = Arrays.asList(ProfessionValue.values());
        this.accountStatuses = Arrays.asList(AccountStatus.values());
        super.setMaximumresultsPerpage(10);
        this.reloadFilterReset();
    }

    @Override
    public void reloadFromDB(int offset, int limit, Map<String, Object> arg2) throws Exception {
        super.setDataModels(this.memberService.getMembers(search, offset, limit));
    }

    @Override
    public void reloadFilterReset() {
        search = CustomSearchUtils.genereateSearchObjectForMembers(searchTerm, selectedProfession, selectedRegion, selectedAccountStatus, selectedRegistrationType, selectedSortField);
        super.setTotalRecords(this.memberService.countMembers(search));
        this.total = super.getTotalRecords();
        this.created = this.memberService.countMembers(search.copy().addFilterEqual("accountStatus", AccountStatus.Created));
        this.verified = this.memberService.countMembers(search.copy().addFilterEqual("accountStatus", AccountStatus.Verified));
        this.regisered = this.memberService.countMembers(search.copy().addFilterEqual("accountStatus", AccountStatus.Registered));

    }

    public void loadSelectedMember(Member member) {
        this.selectedMember = member;
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

    public void blockMember(Member member, String notes) {
        try {
            this.memberService.block(member, notes);
            super.reloadFilterReset();
            UiUtils.showMessageBox(member.composeFullName() + " has been blocked", "Action Successful");
        } catch (Exception e) {
            UiUtils.showMessageBox(e.getMessage(), "Action failed");
        }
    }

    public void activateMember(Member member) {
        try {
            this.memberService.activateMemberAccount(member);
            super.reloadFilterReset();
            UiUtils.showMessageBox(member.composeFullName() + " has been activatd", "Action Successful");
        } catch (Exception e) {
            e.printStackTrace();
            UiUtils.showMessageBox(e.getMessage(), "Action failed");
        }
    }

    public void extendMemberSubscription(Member member) {
        try {
            
            ApplicationContextProvider.getBean(SubscriptionService.class).extendSubscription(member, startDate, attachement);
            super.reloadFilterReset();
            UiUtils.showMessageBox(member.composeFullName() + "subscription has been extended", "Action Successful");
        } catch (Exception e) {
            UiUtils.showMessageBox("Action failed", e.getMessage());
        }
    }

    /*
    Upload images to cloudinary
     */
    public void imageUploadEvent(FileUploadEvent event) {
        try {
            attachement = IOUtils.toByteArray(event.getFile().getInputstream());

        } catch (Exception ex) {
            FacesMessage msg = new FacesMessage("Failed", "Image upload failed");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            Logger.getLogger(MemberProfileView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void unBlockMember(Member member, String notes) {
        try {
            this.memberService.unblock(member, notes);
            super.reloadFilterReset();
            UiUtils.showMessageBox(member.composeFullName() + " has been unblocked", "Action Successful");
        } catch (Exception e) {
            UiUtils.showMessageBox(e.getMessage(), "Action failed");
        }
    }

    public void saveUploadedMembers() throws ValidationFailedException, Exception {
        System.out.println("----Inside save uploads method-------");

        if (!this.uploadedMembers.isEmpty()) {
            for (Member member : uploadedMembers) {
                try {
                    member = this.memberService.saveUploadedMember(member);
                    this.memberService.activateMemberAccount(member);
                } catch (OperationFailedException ex) {
                    System.out.println("Some error occurred-------" + ex.getLocalizedMessage());
                }

            }

            UiUtils.showMessageBox("Save successfull", "Invalid rows were skipped");
            this.uploadedMembers = null;
            reloadFilterReset();
        }

    }

    public void handleCSVFileUpload(FileUploadEvent event) throws IOException, Exception {
        System.out.println("----Inside upload method-------");
        ExcelUploadHelper clientUploads = new ExcelUploadHelper();
        this.uploadedMembers = clientUploads.uploadMembersCSVFile(event);

        if (uploadedMembers != null) {
            this.counterMessage = "Successfully uploaded" + uploadedMembers.size() + " clients";
            UiUtils.showMessageBox("Finished upload", "Success fully uploaded " + uploadedMembers.size() + " communications");
        } else {
            UiUtils.ComposeFailure("Action failed", "No member was uploaded due to invalid records in your document. Please cross-check and try again");

        }

    }

    public StreamedContent loadTemplateForDownload() {
        InputStream stream = getClass().getResourceAsStream("/custom-files/aapu_members_import_template.csv");

        return new DefaultStreamedContent(stream, "text/csv", "AAPU_Members_UploadTemplate.csv");
    }

    public Boolean showSaveUploadsButton() {
        return this.uploadedMembers != null && !this.uploadedMembers.isEmpty();

    }

    private void resetInput() {
        this.searchTerm = "";
        selectedSortField = new SortField("dateCreated", "dateCreated", true);

        selectedRegion = null;
        selectedProfession = null;
        selectedAccountStatus = null;
    }

    public List<SortField> getSortFields() {
        return sortFields;
    }

    public void setSortFields(List<SortField> sortFields) {
        this.sortFields = sortFields;
    }

    public SortField getSelectedSortField() {
        return selectedSortField;
    }

    public void setSelectedSortField(SortField selectedSortField) {
        this.selectedSortField = selectedSortField;
    }

    public List<Region> getRegions() {
        return regions;
    }

    public void setRegions(List<Region> regions) {
        this.regions = regions;
    }

    public List<ProfessionValue> getProfessions() {
        return professions;
    }

    public void setProfessions(List<ProfessionValue> professions) {
        this.professions = professions;
    }

    public Region getSelectedRegion() {
        return selectedRegion;
    }

    public void setSelectedRegion(Region selectedRegion) {
        this.selectedRegion = selectedRegion;
    }

    public ProfessionValue getSelectedProfession() {
        return selectedProfession;
    }

    public void setSelectedProfession(ProfessionValue selectedProfession) {
        this.selectedProfession = selectedProfession;
    }

    public List<MemberRegistrationType> getRegistrationTypes() {
        return registrationTypes;
    }

    public void setRegistrationTypes(List<MemberRegistrationType> registrationTypes) {
        this.registrationTypes = registrationTypes;
    }

    public MemberRegistrationType getSelectedRegistrationType() {
        return selectedRegistrationType;
    }

    public void setSelectedRegistrationType(MemberRegistrationType selectedRegistrationType) {
        this.selectedRegistrationType = selectedRegistrationType;
    }

    public List<AccountStatus> getAccountStatuses() {
        return accountStatuses;
    }

    public void setAccountStatuses(List<AccountStatus> accountStatuses) {
        this.accountStatuses = accountStatuses;
    }

    public AccountStatus getSelectedAccountStatus() {
        return selectedAccountStatus;
    }

    public void setSelectedAccountStatus(AccountStatus selectedAccountStatus) {
        this.selectedAccountStatus = selectedAccountStatus;
    }

    public int getTotal() {
        return total;
    }

    public int getRegisered() {
        return regisered;
    }

    public int getCreated() {
        return created;
    }

    public int getVerified() {
        return verified;
    }

    public int getBlocked() {
        return blocked;
    }

    public String getCounterMessage() {
        return counterMessage;
    }

    public void setCounterMessage(String counterMessage) {
        this.counterMessage = counterMessage;
    }

    public List<Member> getUploadedMembers() {
        return uploadedMembers;
    }

    public void setUploadedMembers(List<Member> uploadedMembers) {
        this.uploadedMembers = uploadedMembers;
    }

    public Member getSelectedMember() {
        return selectedMember;
    }

    public void setSelectedMember(Member selectedMember) {
        this.selectedMember = selectedMember;
    }

    public String getBlockNotes() {
        return blockNotes;
    }

    public void setBlockNotes(String blockNotes) {
        this.blockNotes = blockNotes;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    
    

}
