/*
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pahappa.systems.models;

import java.util.Date;
import java.util.List;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.pahappa.systems.constants.CommunicationType;
import org.sers.webutils.model.BaseEntity;

/**
 *
 * @author Ray Gdhrt
 */
@Entity
@Table(name = "courses")
public class Course extends BaseEntity {

    private String title;
    private String description;
    private List<String> links;
    private LookUpValue courseType;
    private Date startDate;
    private Date endDate;
    private String imageUrl;

    @Column(name = "title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

     @Column(name = "description",columnDefinition = "TEXT" )
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

     @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "string_links")
    public List<String> getLinks() {
        return links;
    }

    public void setLinks(List<String> links) {
        this.links = links;
    }

    @ManyToOne
    @JoinColumn(name = "course_type" )
    public LookUpValue getCourseType() {
        return courseType;
    }

    public void setCourseType(LookUpValue courseType) {
        this.courseType = courseType;
    }

    @Temporal(TemporalType.DATE)
     @Column(name = "start_date" )
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

     @Temporal(TemporalType.DATE)
     @Column(name = "end_date" )
    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Column(name = "image_url", length = 1000)
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
    
   

    @Override
    public boolean equals(Object object) {
        return object instanceof Course && (super.getId() != null) ? super.getId().equals(((Course) object).getId())
                : (object == this);
    }

    @Override
    public int hashCode() {
        return super.getId() != null ? this.getClass().hashCode() + super.getId().hashCode() : super.hashCode();
    }

}
