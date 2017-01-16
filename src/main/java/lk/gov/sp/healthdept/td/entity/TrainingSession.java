/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.gov.sp.healthdept.td.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;

/**
 *
 * @author pdhs-sp
 */
@Entity
public class TrainingSession implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    Training training;
    String sessionNo;
    @Temporal(javax.persistence.TemporalType.DATE)
    Date trainingDate;
    @Temporal(javax.persistence.TemporalType.DATE)
    Date trainingFrom;
    @Temporal(javax.persistence.TemporalType.DATE)
    Date trainingTo;
    int expectedCount;
    int participantCount;
    int informedAbsenteesCount;
    int nonInformedAbsenteesCount;

    public Training getTraining() {
        return training;
    }

    public void setTraining(Training training) {
        this.training = training;
    }

    public String getSessionNo() {
        return sessionNo;
    }

    public void setSessionNo(String sessionNo) {
        this.sessionNo = sessionNo;
    }

    public Date getTrainingDate() {
        return trainingDate;
    }

    public void setTrainingDate(Date trainingDate) {
        this.trainingDate = trainingDate;
    }

    public Date getTrainingFrom() {
        return trainingFrom;
    }

    public void setTrainingFrom(Date trainingFrom) {
        this.trainingFrom = trainingFrom;
    }

    public Date getTrainingTo() {
        return trainingTo;
    }

    public void setTrainingTo(Date trainingTo) {
        this.trainingTo = trainingTo;
    }

    public int getExpectedCount() {
        return expectedCount;
    }

    public void setExpectedCount(int expectedCount) {
        this.expectedCount = expectedCount;
    }

    public int getParticipantCount() {
        return participantCount;
    }

    public void setParticipantCount(int participantCount) {
        this.participantCount = participantCount;
    }

    public int getInformedAbsenteesCount() {
        return informedAbsenteesCount;
    }

    public void setInformedAbsenteesCount(int informedAbsenteesCount) {
        this.informedAbsenteesCount = informedAbsenteesCount;
    }

    public int getNonInformedAbsenteesCount() {
        return nonInformedAbsenteesCount;
    }

    public void setNonInformedAbsenteesCount(int nonInformedAbsenteesCount) {
        this.nonInformedAbsenteesCount = nonInformedAbsenteesCount;
    }
    
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        if (!(object instanceof TrainingSession)) {
            return false;
        }
        TrainingSession other = (TrainingSession) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "lk.gov.sp.healthdept.td.entity.TrainingSession[ id=" + id + " ]";
    }
    
}
