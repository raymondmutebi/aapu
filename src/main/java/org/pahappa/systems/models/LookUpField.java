/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pahappa.systems.models;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.apache.commons.lang.WordUtils;
import org.sers.webutils.model.BaseEntity;

/**
 *
 * @author HP
 */
@Entity
@Table(name = "look_up_fields")
public class LookUpField extends BaseEntity {

    private String name;
    private String description;
    private Boolean isMigration=false;
    private Set<LookUpValue> lookUpValues;
    private List<String> stringLookupValues;

    @Column(name = "name",unique = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "allows_multiple")
    public Boolean getIsMigration() {
        return isMigration;
    }

    public void setIsMigration(Boolean isMigration) {
        this.isMigration = isMigration;
    }

    @OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinTable(name = "lookup_values_in_field", joinColumns = @JoinColumn(name = "field_id"), inverseJoinColumns = @JoinColumn(name = "value_id"))
    public Set<LookUpValue> getLookUpValues() {
        return lookUpValues;
    }

    public void setLookUpValues(Set<LookUpValue> lookUpValues) {
        this.lookUpValues = lookUpValues;
    }

    public void addLookupValue(LookUpValue lookUpValue) {

        if (this.lookUpValues == null) {
            
            this.lookUpValues = new HashSet<LookUpValue>();

        }
        
        lookUpValue.setName(WordUtils.capitalize(lookUpValue.getName().trim()));
        this.lookUpValues.add(lookUpValue);
    }

    public void removeLookupValue(LookUpValue lookUpValue) {

        this.lookUpValues.remove(lookUpValue);
    }
    
   

    @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "string_lookup_values")
    public List<String> getStringLookupValues() {
        return stringLookupValues;
    }

    public void setStringLookupValues(List<String> stringLookupValues) {
        this.stringLookupValues = stringLookupValues;
    }

    @Override
    public String toString() {
        return name ;
    }
    
    
    
      @Override
    public boolean equals(Object object) {
        return object instanceof LookUpField && (super.getId() != null) ? super.getId().equals(((LookUpField) object).getId())
                : (object == this);
    }

    @Override
    public int hashCode() {
        return super.getId() != null ? this.getClass().hashCode() + super.getId().hashCode() : super.hashCode();
    }
  
    
}
