/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DBObjects;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Nicki
 */
@Entity
@Table(name = "QuizAnswer")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "QuizAnswer.findAll", query = "SELECT q FROM QuizAnswer q")
    , @NamedQuery(name = "QuizAnswer.findById", query = "SELECT q FROM QuizAnswer q WHERE q.id = :id")
    , @NamedQuery(name = "QuizAnswer.findByText", query = "SELECT q FROM QuizAnswer q WHERE q.text = :text")
    , @NamedQuery(name = "QuizAnswer.findByIsCorrect", query = "SELECT q FROM QuizAnswer q WHERE q.isCorrect = :isCorrect")})
public class QuizAnswer implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @Size(max = 45)
    @Column(name = "text")
    private String text;
    @Basic(optional = false)
    @NotNull
    @Column(name = "isCorrect")
    private boolean isCorrect;
    @JoinColumn(name = "name", referencedColumnName = "name")
    @ManyToOne
    private Quiz name;

    public QuizAnswer() {
    }

    public QuizAnswer(Integer id) {
        this.id = id;
    }

    public QuizAnswer(Integer id, boolean isCorrect) {
        this.id = id;
        this.isCorrect = isCorrect;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean getIsCorrect() {
        return isCorrect;
    }

    public void setIsCorrect(boolean isCorrect) {
        this.isCorrect = isCorrect;
    }

    public Quiz getName() {
        return name;
    }

    public void setName(Quiz name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof QuizAnswer)) {
            return false;
        }
        QuizAnswer other = (QuizAnswer) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DBObjects.QuizAnswer[ id=" + id + " ]";
    }
    
}
