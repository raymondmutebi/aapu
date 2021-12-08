/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pahappa.systems.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.sers.webutils.model.BaseEntity;

/**
 *
 * @author HP
 */
@Entity
@Table(name = "lookup_values")
public class LookUpValue extends BaseEntity{
    private String name;
    private int position;

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

   
@Column(name = "position")
    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
   
   
    
    
    @Override
    public String toString() {
        return name ;
    }
    
    
    @Override
    public boolean equals(Object object) {
        return object instanceof LookUpValue && (super.getId() != null) ? super.getId().equals(((LookUpValue) object).getId())
                : (object == this);
    }

    @Override
    public int hashCode() {
        return super.getId() != null ? this.getClass().hashCode() + super.getId().hashCode() : super.hashCode();
    }
}
