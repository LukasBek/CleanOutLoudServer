/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DBObjects;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Nicki
 */
@Entity
@Table(name = "AnsweredQuizzes", catalog = "CoL", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AnsweredQuizzes.findAll", query = "SELECT a FROM AnsweredQuizzes a")
    , @NamedQuery(name = "AnsweredQuizzes.findByName", query = "SELECT a FROM AnsweredQuizzes a WHERE a.answeredQuizzesPK.name = :name")
    , @NamedQuery(name = "AnsweredQuizzes.findByUserName", query = "SELECT a FROM AnsweredQuizzes a WHERE a.answeredQuizzesPK.userName = :userName")
    , @NamedQuery(name = "AnsweredQuizzes.findByAnsweredCorrect", query = "SELECT a FROM AnsweredQuizzes a WHERE a.answeredCorrect = :answeredCorrect")})
public class AnsweredQuizzes implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected AnsweredQuizzesPK answeredQuizzesPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "answeredCorrect")
    private boolean answeredCorrect;
    @JoinColumn(name = "name", referencedColumnName = "name", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Quiz quiz;
    @JoinColumn(name = "userName", referencedColumnName = "userName", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Users users;

    public AnsweredQuizzes() {
    }

    public AnsweredQuizzes(AnsweredQuizzesPK answeredQuizzesPK) {
        this.answeredQuizzesPK = answeredQuizzesPK;
    }

    public AnsweredQuizzes(AnsweredQuizzesPK answeredQuizzesPK, boolean answeredCorrect) {
        this.answeredQuizzesPK = answeredQuizzesPK;
        this.answeredCorrect = answeredCorrect;
    }

    public AnsweredQuizzes(String name, String userName) {
        this.answeredQuizzesPK = new AnsweredQuizzesPK(name, userName);
    }

    public AnsweredQuizzesPK getAnsweredQuizzesPK() {
        return answeredQuizzesPK;
    }

    public void setAnsweredQuizzesPK(AnsweredQuizzesPK answeredQuizzesPK) {
        this.answeredQuizzesPK = answeredQuizzesPK;
    }

    public boolean getAnsweredCorrect() {
        return answeredCorrect;
    }

    public void setAnsweredCorrect(boolean answeredCorrect) {
        this.answeredCorrect = answeredCorrect;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (answeredQuizzesPK != null ? answeredQuizzesPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AnsweredQuizzes)) {
            return false;
        }
        AnsweredQuizzes other = (AnsweredQuizzes) object;
        if ((this.answeredQuizzesPK == null && other.answeredQuizzesPK != null) || (this.answeredQuizzesPK != null && !this.answeredQuizzesPK.equals(other.answeredQuizzesPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DBObjects.AnsweredQuizzes[ answeredQuizzesPK=" + answeredQuizzesPK + " ]";
    }
    
}
