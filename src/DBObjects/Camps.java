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
@Table(name = "Camps", catalog = "CoL", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Camps.findAll", query = "SELECT c FROM Camps c")
    , @NamedQuery(name = "Camps.findByCampName", query = "SELECT c FROM Camps c WHERE c.campName = :campName")
    , @NamedQuery(name = "Camps.findByGarbageWeight", query = "SELECT c FROM Camps c WHERE c.garbageWeight = :garbageWeight")
    , @NamedQuery(name = "Camps.findByLocation", query = "SELECT c FROM Camps c WHERE c.location = :location")})
public class Camps implements Serializable {

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
    private int garbageWeight;
    @Size(max = 45)
    @Column(name = "location")
    private String location;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "camp")
    private Collection<Users> usersCollection;

    public Camps() {
    }

    public Camps(String campName) {
        this.campName = campName;
    }

    public Camps(String campName, int garbageWeight) {
        this.campName = campName;
        this.garbageWeight = garbageWeight;
    }

    public String getCampName() {
        return campName;
    }

    public void setCampName(String campName) {
        this.campName = campName;
    }

    public int getGarbageWeight() {
        return garbageWeight;
    }

    public void setGarbageWeight(int garbageWeight) {
        this.garbageWeight = garbageWeight;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @XmlTransient
    public Collection<Users> getUsersCollection() {
        return usersCollection;
    }

    public void setUsersCollection(Collection<Users> usersCollection) {
        this.usersCollection = usersCollection;
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
        if (!(object instanceof Camps)) {
            return false;
        }
        Camps other = (Camps) object;
        if ((this.campName == null && other.campName != null) || (this.campName != null && !this.campName.equals(other.campName))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DBObjects.Camps[ campName=" + campName + " ]";
    }
    
}
