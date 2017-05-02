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
@Table(name = "AnsweredQuiz")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AnsweredQuiz.findAll", query = "SELECT a FROM AnsweredQuiz a")
    , @NamedQuery(name = "AnsweredQuiz.findByName", query = "SELECT a FROM AnsweredQuiz a WHERE a.answeredQuizPK.name = :name")
    , @NamedQuery(name = "AnsweredQuiz.findByUserName", query = "SELECT a FROM AnsweredQuiz a WHERE a.answeredQuizPK.userName = :userName")
    , @NamedQuery(name = "AnsweredQuiz.findByAnsweredCorrect", query = "SELECT a FROM AnsweredQuiz a WHERE a.answeredCorrect = :answeredCorrect")})
public class AnsweredQuiz implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected AnsweredQuizPK answeredQuizPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "answeredCorrect")
    private boolean answeredCorrect;
    @JoinColumn(name = "name", referencedColumnName = "name", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Quiz quiz;
    @JoinColumn(name = "userName", referencedColumnName = "userName", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private User user;

    public AnsweredQuiz() {
    }

    public AnsweredQuiz(AnsweredQuizPK answeredQuizPK) {
        this.answeredQuizPK = answeredQuizPK;
    }

    public AnsweredQuiz(AnsweredQuizPK answeredQuizPK, boolean answeredCorrect) {
        this.answeredQuizPK = answeredQuizPK;
        this.answeredCorrect = answeredCorrect;
    }

    public AnsweredQuiz(String name, String userName) {
        this.answeredQuizPK = new AnsweredQuizPK(name, userName);
    }

    public AnsweredQuizPK getAnsweredQuizPK() {
        return answeredQuizPK;
    }

    public void setAnsweredQuizPK(AnsweredQuizPK answeredQuizPK) {
        this.answeredQuizPK = answeredQuizPK;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (answeredQuizPK != null ? answeredQuizPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AnsweredQuiz)) {
            return false;
        }
        AnsweredQuiz other = (AnsweredQuiz) object;
        if ((this.answeredQuizPK == null && other.answeredQuizPK != null) || (this.answeredQuizPK != null && !this.answeredQuizPK.equals(other.answeredQuizPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DBObjects.AnsweredQuiz[ answeredQuizPK=" + answeredQuizPK + " ]";
    }
    
}
