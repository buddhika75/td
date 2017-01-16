/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.gov.sp.healthdept.td.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

/**
 *
 * @author pdhs-sp
 */
@Entity
public class TrainingFeedback implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    Training training;
    @Enumerated(EnumType.STRING)
    FeedbackCategory subject_matters_adequate;
    @Enumerated(EnumType.STRING)
    FeedbackCategory duration_adequate;
    @Enumerated(EnumType.STRING)
    FeedbackCategory practicality;
    @Enumerated(EnumType.STRING)
    FeedbackCategory usefullness;
    @Enumerated(EnumType.STRING)
    FeedbackCategory practicle_and_theory_concurancy;
    @Enumerated(EnumType.STRING)
    FeedbackCategory lecture_halls;
    @Enumerated(EnumType.STRING)
    FeedbackCategory staff_coperation;
    @Enumerated(EnumType.STRING)
    FeedbackCategory meals;
    @Enumerated(EnumType.STRING)
    FeedbackCategory toilets;

    @ManyToOne
    Person trainer1;
    @Enumerated(EnumType.STRING)
    FeedbackCategory trainer1Presentation;
    @Enumerated(EnumType.STRING)
    FeedbackCategory trainer1TimeManagement;
    @Enumerated(EnumType.STRING)
    FeedbackCategory trainer1SubjectAdequacy;
    @Enumerated(EnumType.STRING)
    FeedbackCategory trainer1relavanceToTopic;
    @Enumerated(EnumType.STRING)
    FeedbackCategory trainer1Practicles;
    @Enumerated(EnumType.STRING)
    FeedbackCategory trainer1Lecturing;

    @ManyToOne
    Person trainer2;
    @Enumerated(EnumType.STRING)
    FeedbackCategory trainer2Presentation;
    @Enumerated(EnumType.STRING)
    FeedbackCategory trainer2TimeManagement;
    @Enumerated(EnumType.STRING)
    FeedbackCategory trainer2SubjectAdequacy;
    @Enumerated(EnumType.STRING)
    FeedbackCategory trainer2relavanceToTopic;
    @Enumerated(EnumType.STRING)
    FeedbackCategory trainer2Practicles;
    @Enumerated(EnumType.STRING)
    FeedbackCategory trainer2Lecturing;

    @ManyToOne
    Person trainer3;
    @Enumerated(EnumType.STRING)
    FeedbackCategory trainer3Presentation;
    @Enumerated(EnumType.STRING)
    FeedbackCategory trainer3TimeManagement;
    @Enumerated(EnumType.STRING)
    FeedbackCategory trainer3SubjectAdequacy;
    @Enumerated(EnumType.STRING)
    FeedbackCategory trainer3relavanceToTopic;
    @Enumerated(EnumType.STRING)
    FeedbackCategory trainer3Practicles;
    @Enumerated(EnumType.STRING)
    FeedbackCategory trainer3Lecturing;

    @Lob
    String comments;

    public Training getTraining() {
        return training;
    }

    public void setTraining(Training training) {
        this.training = training;
    }

    public FeedbackCategory getSubject_matters_adequate() {
        return subject_matters_adequate;
    }

    public void setSubject_matters_adequate(FeedbackCategory subject_matters_adequate) {
        this.subject_matters_adequate = subject_matters_adequate;
    }

    public FeedbackCategory getDuration_adequate() {
        return duration_adequate;
    }

    public void setDuration_adequate(FeedbackCategory duration_adequate) {
        this.duration_adequate = duration_adequate;
    }

    public FeedbackCategory getPracticality() {
        return practicality;
    }

    public void setPracticality(FeedbackCategory practicality) {
        this.practicality = practicality;
    }

    public FeedbackCategory getUsefullness() {
        return usefullness;
    }

    public void setUsefullness(FeedbackCategory usefullness) {
        this.usefullness = usefullness;
    }

    public FeedbackCategory getPracticle_and_theory_concurancy() {
        return practicle_and_theory_concurancy;
    }

    public void setPracticle_and_theory_concurancy(FeedbackCategory practicle_and_theory_concurancy) {
        this.practicle_and_theory_concurancy = practicle_and_theory_concurancy;
    }

    public FeedbackCategory getLecture_halls() {
        return lecture_halls;
    }

    public void setLecture_halls(FeedbackCategory lecture_halls) {
        this.lecture_halls = lecture_halls;
    }

    public FeedbackCategory getStaff_coperation() {
        return staff_coperation;
    }

    public void setStaff_coperation(FeedbackCategory staff_coperation) {
        this.staff_coperation = staff_coperation;
    }

    public FeedbackCategory getMeals() {
        return meals;
    }

    public void setMeals(FeedbackCategory meals) {
        this.meals = meals;
    }

    public FeedbackCategory getToilets() {
        return toilets;
    }

    public void setToilets(FeedbackCategory toilets) {
        this.toilets = toilets;
    }

    public Person getTrainer1() {
        return trainer1;
    }

    public void setTrainer1(Person trainer1) {
        this.trainer1 = trainer1;
    }

    public FeedbackCategory getTrainer1Presentation() {
        return trainer1Presentation;
    }

    public void setTrainer1Presentation(FeedbackCategory trainer1Presentation) {
        this.trainer1Presentation = trainer1Presentation;
    }

    public FeedbackCategory getTrainer1TimeManagement() {
        return trainer1TimeManagement;
    }

    public void setTrainer1TimeManagement(FeedbackCategory trainer1TimeManagement) {
        this.trainer1TimeManagement = trainer1TimeManagement;
    }

    public FeedbackCategory getTrainer1SubjectAdequacy() {
        return trainer1SubjectAdequacy;
    }

    public void setTrainer1SubjectAdequacy(FeedbackCategory trainer1SubjectAdequacy) {
        this.trainer1SubjectAdequacy = trainer1SubjectAdequacy;
    }

    public FeedbackCategory getTrainer1relavanceToTopic() {
        return trainer1relavanceToTopic;
    }

    public void setTrainer1relavanceToTopic(FeedbackCategory trainer1relavanceToTopic) {
        this.trainer1relavanceToTopic = trainer1relavanceToTopic;
    }

    public FeedbackCategory getTrainer1Practicles() {
        return trainer1Practicles;
    }

    public void setTrainer1Practicles(FeedbackCategory trainer1Practicles) {
        this.trainer1Practicles = trainer1Practicles;
    }

    public FeedbackCategory getTrainer1Lecturing() {
        return trainer1Lecturing;
    }

    public void setTrainer1Lecturing(FeedbackCategory trainer1Lecturing) {
        this.trainer1Lecturing = trainer1Lecturing;
    }

    public Person getTrainer2() {
        return trainer2;
    }

    public void setTrainer2(Person trainer2) {
        this.trainer2 = trainer2;
    }

    public FeedbackCategory getTrainer2Presentation() {
        return trainer2Presentation;
    }

    public void setTrainer2Presentation(FeedbackCategory trainer2Presentation) {
        this.trainer2Presentation = trainer2Presentation;
    }

    public FeedbackCategory getTrainer2TimeManagement() {
        return trainer2TimeManagement;
    }

    public void setTrainer2TimeManagement(FeedbackCategory trainer2TimeManagement) {
        this.trainer2TimeManagement = trainer2TimeManagement;
    }

    public FeedbackCategory getTrainer2SubjectAdequacy() {
        return trainer2SubjectAdequacy;
    }

    public void setTrainer2SubjectAdequacy(FeedbackCategory trainer2SubjectAdequacy) {
        this.trainer2SubjectAdequacy = trainer2SubjectAdequacy;
    }

    public FeedbackCategory getTrainer2relavanceToTopic() {
        return trainer2relavanceToTopic;
    }

    public void setTrainer2relavanceToTopic(FeedbackCategory trainer2relavanceToTopic) {
        this.trainer2relavanceToTopic = trainer2relavanceToTopic;
    }

    public FeedbackCategory getTrainer2Practicles() {
        return trainer2Practicles;
    }

    public void setTrainer2Practicles(FeedbackCategory trainer2Practicles) {
        this.trainer2Practicles = trainer2Practicles;
    }

    public FeedbackCategory getTrainer2Lecturing() {
        return trainer2Lecturing;
    }

    public void setTrainer2Lecturing(FeedbackCategory trainer2Lecturing) {
        this.trainer2Lecturing = trainer2Lecturing;
    }

    public Person getTrainer3() {
        return trainer3;
    }

    public void setTrainer3(Person trainer3) {
        this.trainer3 = trainer3;
    }

    public FeedbackCategory getTrainer3Presentation() {
        return trainer3Presentation;
    }

    public void setTrainer3Presentation(FeedbackCategory trainer3Presentation) {
        this.trainer3Presentation = trainer3Presentation;
    }

    public FeedbackCategory getTrainer3TimeManagement() {
        return trainer3TimeManagement;
    }

    public void setTrainer3TimeManagement(FeedbackCategory trainer3TimeManagement) {
        this.trainer3TimeManagement = trainer3TimeManagement;
    }

    public FeedbackCategory getTrainer3SubjectAdequacy() {
        return trainer3SubjectAdequacy;
    }

    public void setTrainer3SubjectAdequacy(FeedbackCategory trainer3SubjectAdequacy) {
        this.trainer3SubjectAdequacy = trainer3SubjectAdequacy;
    }

    public FeedbackCategory getTrainer3relavanceToTopic() {
        return trainer3relavanceToTopic;
    }

    public void setTrainer3relavanceToTopic(FeedbackCategory trainer3relavanceToTopic) {
        this.trainer3relavanceToTopic = trainer3relavanceToTopic;
    }

    public FeedbackCategory getTrainer3Practicles() {
        return trainer3Practicles;
    }

    public void setTrainer3Practicles(FeedbackCategory trainer3Practicles) {
        this.trainer3Practicles = trainer3Practicles;
    }

    public FeedbackCategory getTrainer3Lecturing() {
        return trainer3Lecturing;
    }

    public void setTrainer3Lecturing(FeedbackCategory trainer3Lecturing) {
        this.trainer3Lecturing = trainer3Lecturing;
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
        if (!(object instanceof TrainingFeedback)) {
            return false;
        }
        TrainingFeedback other = (TrainingFeedback) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "lk.gov.sp.healthdept.td.entity.TrainingFeedback[ id=" + id + " ]";
    }

}
