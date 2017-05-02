/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DBObjects;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Nicki
 */
@Entity
@Table(name = "Camp")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Camp.findAll", query = "SELECT c FROM Camp c")
    , @NamedQuery(name = "Camp.findByCampName", query = "SELECT c FROM Camp c WHERE c.campName = :campName")
    , @NamedQuery(name = "Camp.findByGarbageWeight", query = "SELECT c FROM Camp c WHERE c.garbageWeight = :garbageWeight")
    , @NamedQuery(name = "Camp.findByLocation", query = "SELECT c FROM Camp c WHERE c.location = :location")})
public class Camp implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "campName")
    private String campName;
    @Basic(optional = false)
    @NotNull
    @Column(name = "garbageWeight")
    private float garbageWeight;
    @Size(max = 45)
    @Column(name = "location")
    private String location;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "camp")
    private Collection<User> userCollection;

    public Camp() {
    }

    public Camp(String campName) {
        this.campName = campName;
    }

    public Camp(String campName, float garbageWeight) {
        this.campName = campName;
        this.garbageWeight = garbageWeight;
    }

    public String getCampName() {
        return campName;
    }

    public void setCampName(String campName) {
        this.campName = campName;
    }

    public float getGarbageWeight() {
        return garbageWeight;
    }

    public void setGarbageWeight(float garbageWeight) {
        this.garbageWeight = garbageWeight;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @XmlTransient
    public Collection<User> getUserCollection() {
        return userCollection;
    }

    public void setUserCollection(Collection<User> userCollection) {
        this.userCollection = userCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (campName != null ? campName.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Camp)) {
            return false;
        }
        Camp other = (Camp) object;
        if ((this.campName == null && other.campName != null) || (this.campName != null && !this.campName.equals(other.campName))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DBObjects.Camp[ campName=" + campName + " ]";
    }
    
}
