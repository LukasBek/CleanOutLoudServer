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
@Table(name = "Quiz")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Quiz.findAll", query = "SELECT q FROM Quiz q")
    , @NamedQuery(name = "Quiz.findByName", query = "SELECT q FROM Quiz q WHERE q.name = :name")
    , @NamedQuery(name = "Quiz.findByTextQuestion", query = "SELECT q FROM Quiz q WHERE q.textQuestion = :textQuestion")
    , @NamedQuery(name = "Quiz.findByFunFact", query = "SELECT q FROM Quiz q WHERE q.funFact = :funFact")})
public class Quiz implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "name")
    private String name;
    @Size(max = 200)
    @Column(name = "textQuestion")
    private String textQuestion;
    @Size(max = 200)
    @Column(name = "funFact")
    private String funFact;
    @OneToMany(mappedBy = "name")
    private Collection<QuizAnswer> quizAnswerCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "quiz")
    private Collection<AnsweredQuiz> answeredQuizCollection;

    public Quiz() {
    }

    public Quiz(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTextQuestion() {
        return textQuestion;
    }

    public void setTextQuestion(String textQuestion) {
        this.textQuestion = textQuestion;
    }

    public String getFunFact() {
        return funFact;
    }

    public void setFunFact(String funFact) {
        this.funFact = funFact;
    }

    @XmlTransient
    public Collection<QuizAnswer> getQuizAnswerCollection() {
        return quizAnswerCollection;
    }

    public void setQuizAnswerCollection(Collection<QuizAnswer> quizAnswerCollection) {
        this.quizAnswerCollection = quizAnswerCollection;
    }

    @XmlTransient
    public Collection<AnsweredQuiz> getAnsweredQuizCollection() {
        return answeredQuizCollection;
    }

    public void setAnsweredQuizCollection(Collection<AnsweredQuiz> answeredQuizCollection) {
        this.answeredQuizCollection = answeredQuizCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (name != null ? name.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Quiz)) {
            return false;
        }
        Quiz other = (Quiz) object;
        if ((this.name == null && other.name != null) || (this.name != null && !this.name.equals(other.name))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DBObjects.Quiz[ name=" + name + " ]";
    }
    
}
