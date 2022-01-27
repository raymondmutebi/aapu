package org.pahappa.systems.views;

import com.googlecode.genericdao.search.Search;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.pahappa.systems.models.Course;
import org.pahappa.systems.security.HyperLinks;
import org.sers.webutils.client.views.presenters.PaginatedTableView;
import org.sers.webutils.client.views.presenters.ViewPath;
import org.sers.webutils.model.RecordStatus;
import org.sers.webutils.server.core.service.excel.reports.ExcelReport;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;
import org.pahappa.systems.core.services.CourseService;
import org.pahappa.systems.core.utils.UiUtils;

@ManagedBean(name = "coursesListView")
@SessionScoped
@ViewPath(path = HyperLinks.COURSES_LIST_VIEW)
public class CoursesListView extends PaginatedTableView<Course, CoursesListView, CoursesListView> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private CourseService courseService;
    private String searchTerm;

    private Search search;

    @PostConstruct
    public void init() {
        this.courseService = ApplicationContextProvider.getBean(CourseService.class);
        super.setMaximumresultsPerpage(10);
        reloadFilterReset();
    }

    @Override
    public void reloadFromDB(int offset, int limit, Map<String, Object> arg2) throws Exception {
        super.setDataModels(this.courseService.getInstances(search, offset, limit));
        System.out.println(super.getDataModels());
    }

    @Override
    public void reloadFilterReset() {
        search = new Search().addFilterEqual("recordStatus", RecordStatus.ACTIVE).addSortDesc("dateCreated");
        super.setTotalRecords(this.courseService.countInstances(search));
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

    public void deleteManufacturer(Course course) {
        try {
            this.courseService.deleteInstance(course);
            super.reloadFilterReset();
            UiUtils.showMessageBox("Message has been deleted", "Action Successful");
        } catch (Exception e) {
            UiUtils.showMessageBox(e.getMessage(), "Action failed");
        }
    }

    private void resetInput() {
        this.searchTerm = "";
    }

}
