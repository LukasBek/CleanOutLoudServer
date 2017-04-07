/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DBObjects;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Nicki
 */
@Entity
@Table(name = "Comments", catalog = "CoL", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Comments.findAll", query = "SELECT c FROM Comments c")
    , @NamedQuery(name = "Comments.findByCommentsId", query = "SELECT c FROM Comments c WHERE c.commentsId = :commentsId")
    , @NamedQuery(name = "Comments.findByText", query = "SELECT c FROM Comments c WHERE c.text = :text")
    , @NamedQuery(name = "Comments.findByDate", query = "SELECT c FROM Comments c WHERE c.date = :date")})
public class Comments implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "commentsId")
    private Integer commentsId;
    @Size(max = 200)
    @Column(name = "text")
    private String text;
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @JoinColumn(name = "messageId", referencedColumnName = "messageId")
    @ManyToOne(optional = false)
    private Messages messageId;
    @JoinColumn(name = "user", referencedColumnName = "userName")
    @ManyToOne
    private Users user;

    public Comments() {
    }

    public Comments(Integer commentsId) {
        this.commentsId = commentsId;
    }

    public Integer getCommentsId() {
        return commentsId;
    }

    public void setCommentsId(Integer commentsId) {
        this.commentsId = commentsId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Messages getMessageId() {
        return messageId;
    }

    public void setMessageId(Messages messageId) {
        this.messageId = messageId;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (commentsId != null ? commentsId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Comments)) {
            return false;
        }
        Comments other = (Comments) object;
        if ((this.commentsId == null && other.commentsId != null) || (this.commentsId != null && !this.commentsId.equals(other.commentsId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DBObjects.Comments[ commentsId=" + commentsId + " ]";
    }
    
}
