/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.gov.sp.healthdept.td.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;

/**
 *
 * @author pdhs-sp
 */
@Entity
public class Training implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    String name;
    String numberStr;
    Long numberLong;
    @Temporal(javax.persistence.TemporalType.DATE)
    Date startDate;
    @Lob
    String objectives;
    @Lob
    String contents;
    @Lob
    String methodology;
    @Lob
    String participants;
    Integer numberOfDays;
    String conductedBy;
    String comments;
    @ManyToOne
    Department department;
    @ManyToOne
    Institute institute;

    @ManyToOne
    Department heldAtdepartment;
    @ManyToOne
    Institute headAtinstitute;
    
    
    
    @Enumerated(EnumType.STRING)
    TrainingCategory trainingCategory;

    Boolean completed;
    
    @OneToOne
    Training completedTraining;
    
    @OneToOne(mappedBy = "completedTraining")
    Training scheduledTraining;

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public Training getCompletedTraining() {
        return completedTraining;
    }

    public void setCompletedTraining(Training completedTraining) {
        this.completedTraining = completedTraining;
    }

    public Training getScheduledTraining() {
        return scheduledTraining;
    }

    public void setScheduledTraining(Training scheduledTraining) {
        this.scheduledTraining = scheduledTraining;
    }
    
    
    
    
    
    public TrainingCategory getTrainingCategory() {
        return trainingCategory;
    }

    public void setTrainingCategory(TrainingCategory trainingCategory) {
        this.trainingCategory = trainingCategory;
    }
    
    

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Institute getInstitute() {
        return institute;
    }

    public void setInstitute(Institute institute) {
        this.institute = institute;
    }

    public Department getHeldAtdepartment() {
        return heldAtdepartment;
    }

    public void setHeldAtdepartment(Department heldAtdepartment) {
        this.heldAtdepartment = heldAtdepartment;
    }

    public Institute getHeadAtinstitute() {
        return headAtinstitute;
    }

    public void setHeadAtinstitute(Institute headAtinstitute) {
        this.headAtinstitute = headAtinstitute;
    }

    
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumberStr() {
        return numberStr;
    }

    public void setNumberStr(String numberStr) {
        this.numberStr = numberStr;
    }

    public Long getNumberLong() {
        return numberLong;
    }

    public void setNumberLong(Long numberLong) {
        this.numberLong = numberLong;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getObjectives() {
        return objectives;
    }

    public void setObjectives(String objectives) {
        this.objectives = objectives;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getMethodology() {
        return methodology;
    }

    public void setMethodology(String methodology) {
        this.methodology = methodology;
    }

    public String getParticipants() {
        return participants;
    }

    public void setParticipants(String participants) {
        this.participants = participants;
    }

    public Integer getNumberOfDays() {
        return numberOfDays;
    }

    public void setNumberOfDays(Integer numberOfDays) {
        this.numberOfDays = numberOfDays;
    }

    public String getConductedBy() {
        return conductedBy;
    }

    public void setConductedBy(String conductedBy) {
        this.conductedBy = conductedBy;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
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
        if (!(object instanceof Training)) {
            return false;
        }
        Training other = (Training) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "lk.gov.sp.healthdept.td.entity.Training[ id=" + id + " ]";
    }

}
