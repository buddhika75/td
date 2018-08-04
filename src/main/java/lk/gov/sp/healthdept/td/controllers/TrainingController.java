package lk.gov.sp.healthdept.td.controllers;

import lk.gov.sp.healthdept.td.controllers.util.JsfUtil;
import lk.gov.sp.healthdept.td.controllers.util.JsfUtil.PersistAction;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.persistence.TemporalType;
import lk.gov.sp.healthdept.td.entity.Department;
import lk.gov.sp.healthdept.td.entity.MonthlyTrainings;
import lk.gov.sp.healthdept.td.entity.Person;
import lk.gov.sp.healthdept.td.entity.PersonTraining;
import lk.gov.sp.healthdept.td.entity.PersonTrainingCategory;
import lk.gov.sp.healthdept.td.entity.Training;
import lk.gov.sp.healthdept.td.entity.TrainingCategory;
import lk.gov.sp.healthdept.td.entity.TrainingFeedback;
import lk.gov.sp.healthdept.td.entity.TrainingSession;
import lk.gov.sp.healthdept.td.facades.PersonTrainingFacade;
import lk.gov.sp.healthdept.td.facades.TrainingFacade;
import lk.gov.sp.healthdept.td.facades.TrainingFeedbackFacade;
import org.apache.commons.beanutils.BeanUtils;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.PieChartModel;

@Named("trainingController")
@SessionScoped
public class TrainingController implements Serializable {

    @EJB
    private lk.gov.sp.healthdept.td.facades.TrainingFacade ejbFacade;
    @EJB
    TrainingFeedbackFacade trainingFeedbackFacade;
    @EJB
    PersonTrainingFacade personTrainingFacade;
    private List<Training> items = null;
    private Training selected;
    private Training selectedScheduled;
    private Training selectedCompleted;
    Department department;
    Date from;
    Date to;
    private List<Training> selectedItems = null;
    List<MonthlyTrainings> selectedMonthlyTrainings;
    List<PersonTraining> presentTrainees;
    List<PersonTraining> absentTrainees;
    PersonTraining selectedPersonTraining;
    PersonTraining removingPersonTraining;
    Person presentTrainee;
    Person absentTrainee;
    TrainingFeedback selectedFeedback;
    TrainingFeedback removingFeedback;
    List<TrainingFeedback> selectedFeedbacks;
    TrainingSession selectedTrainingSession;
    private PieChartModel pieModel1;
    private BarChartModel barModel;

    Long completeCount;
    Long incompleteCount;

    public void fillFeedabcks() {
        String j;
        Map m = new HashMap();
        j = "select tp "
                + " from TrainingFeedback tp "
                + " where tp.training=:t ";
        m.put("t", selectedCompleted);
        selectedFeedbacks = getTrainingFeedbackFacade().findBySQL(j, m);
    }

    public void fillPresentPersons() {
        String j;
        Map m = new HashMap();
        j = "select tp "
                + " from PersonTraining tp "
                + " where tp.training=:t "
                + " and tp.category=:c";
        m.put("t", selectedCompleted);
        m.put("c", PersonTrainingCategory.Training_Completed);
        presentTrainees = getPersonTrainingFacade().findBySQL(j, m);
    }

    public void fillAbsentPersons() {
        String j;
        Map m = new HashMap();
        j = "select tp "
                + " from PersonTraining tp "
                + " where tp.training=:t "
                + " and tp.category=:c";
        m.put("t", selectedCompleted);
        m.put("c", PersonTrainingCategory.Absent_Not_Informed);
        absentTrainees = getPersonTrainingFacade().findBySQL(j, m);
    }

    public void saveFeedback() {
        if (selectedCompleted == null) {
            JsfUtil.addErrorMessage("Please select a training");
            return;
        }
        if (selectedFeedback == null) {
            JsfUtil.addErrorMessage("Feedback?");
            return;
        }
        getTrainingFeedbackFacade().edit(selectedFeedback);
        selectedFeedback = null;
        getSelectedFeedback();
        fillFeedabcks();
    }

    public void addFeedback() {
        if (selectedCompleted == null) {
            JsfUtil.addErrorMessage("Please select a training");
            return;
        }
        selectedFeedback = new TrainingFeedback();
        selectedFeedback.setTraining(selectedCompleted);
        getTrainingFeedbackFacade().create(selectedFeedback);
    }

    public void addPresentPerson() {
        if (selectedCompleted == null) {
            JsfUtil.addErrorMessage("Please select a training");
            return;
        }
        if (presentTrainee == null) {
            JsfUtil.addErrorMessage("Person?");
            return;
        }
        PersonTraining pt = new PersonTraining();
        pt.setCategory(PersonTrainingCategory.Training_Completed);
        pt.setTraining(selectedCompleted);
        pt.setPerson(presentTrainee);
        getPersonTrainingFacade().create(pt);
        presentTrainee = null;
        fillPresentPersons();
    }

    public void addAbsentPerson() {
        if (selectedCompleted == null) {
            JsfUtil.addErrorMessage("Please select a training");
            return;
        }
        if (absentTrainee == null) {
            JsfUtil.addErrorMessage("Person?");
            return;
        }
        PersonTraining pt = new PersonTraining();
        pt.setCategory(PersonTrainingCategory.Absent_Not_Informed);
        pt.setTraining(selectedCompleted);
        pt.setPerson(absentTrainee);
        getPersonTrainingFacade().create(pt);
        absentTrainee = null;
        fillAbsentPersons();
    }

    public void removeFeedback() {
        if (removingFeedback == null) {
            JsfUtil.addErrorMessage("Feedback");
            return;
        }
        getTrainingFeedbackFacade().remove(removingFeedback);
        removingFeedback = null;
        fillFeedabcks();
    }

    public void removePersonTraining() {
        if (removingPersonTraining == null) {
            JsfUtil.addErrorMessage("Feedback?");
            return;
        }
        getPersonTrainingFacade().remove(removingPersonTraining);
        removingPersonTraining = null;
        fillAbsentPersons();
        fillPresentPersons();
    }

    public PersonTraining getRemovingPersonTraining() {
        return removingPersonTraining;
    }

    public void setRemovingPersonTraining(PersonTraining removingPersonTraining) {
        this.removingPersonTraining = removingPersonTraining;
    }

    public TrainingFeedback getRemovingFeedback() {
        return removingFeedback;
    }

    public void setRemovingFeedback(TrainingFeedback removingFeedback) {
        this.removingFeedback = removingFeedback;
    }

    public PersonTrainingFacade getPersonTrainingFacade() {
        return personTrainingFacade;
    }

    public TrainingFeedbackFacade getTrainingFeedbackFacade() {
        return trainingFeedbackFacade;
    }

    public PersonTraining getSelectedPersonTraining() {
        return selectedPersonTraining;
    }

    public void setSelectedPersonTraining(PersonTraining selectedPersonTraining) {
        this.selectedPersonTraining = selectedPersonTraining;
    }

    public TrainingFeedback getSelectedFeedback() {
//        if (selectedFeedback == null) {
//            selectedFeedback = new TrainingFeedback();
//        }
        return selectedFeedback;
    }

    public void setSelectedFeedback(TrainingFeedback selectedFeedback) {
        this.selectedFeedback = selectedFeedback;
    }

    public List<TrainingFeedback> getSelectedFeedbacks() {
        return selectedFeedbacks;
    }

    public void setSelectedFeedbacks(List<TrainingFeedback> selectedFeedbacks) {
        this.selectedFeedbacks = selectedFeedbacks;
    }

    public TrainingSession getSelectedTrainingSession() {
        return selectedTrainingSession;
    }

    public void setSelectedTrainingSession(TrainingSession selectedTrainingSession) {
        this.selectedTrainingSession = selectedTrainingSession;
    }

    public List<PersonTraining> getPresentTrainees() {
        return presentTrainees;
    }

    public void setPresentTrainees(List<PersonTraining> presentTrainees) {
        this.presentTrainees = presentTrainees;
    }

    public List<PersonTraining> getAbsentTrainees() {
        return absentTrainees;
    }

    public void setAbsentTrainees(List<PersonTraining> absentTrainees) {
        this.absentTrainees = absentTrainees;
    }

    public Person getPresentTrainee() {
        return presentTrainee;
    }

    public void setPresentTrainee(Person presentTrainee) {
        this.presentTrainee = presentTrainee;
    }

    public Person getAbsentTrainee() {
        return absentTrainee;
    }

    public void setAbsentTrainee(Person absentTrainee) {
        this.absentTrainee = absentTrainee;
    }

    public String editCompletedTraining() {
        if (selectedCompleted == null) {
            JsfUtil.addErrorMessage("Training?");
            return "";
        }
        fillAbsentPersons();
        fillPresentPersons();
        fillFeedabcks();
        return "/training/Edit_Training_Completed";
    }

    public String makeSheculeTrainingComplete() {
        System.out.println("makeSheculeTrainingComplete");
        System.out.println("selectedScheduled = " + selectedScheduled);
        if (selectedScheduled == null) {
            JsfUtil.addErrorMessage("Select a training");
            return "";
        }
        System.out.println("selectedScheduled = " + selectedScheduled);
        System.out.println("selectedScheduled.getCompleted() = " + selectedScheduled.getCompleted());
        if (selectedScheduled.getCompleted() == null) {
            selectedScheduled.setCompleted(Boolean.FALSE);
        }

        if (selectedScheduled.getCompleted() == true) {
            JsfUtil.addErrorMessage("Already Completed");
        }

        selectedCompleted = new Training();

        try {
            BeanUtils.copyProperties(selectedCompleted, selectedScheduled);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(TrainingController.class.getName()).log(Level.SEVERE, null, ex);
            JsfUtil.addErrorMessage("Copy Error");
            return "";
        } catch (InvocationTargetException ex) {
            JsfUtil.addErrorMessage("Copy Error");
            Logger.getLogger(TrainingController.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }

        selectedCompleted.setCompleted(null);
        selectedCompleted.setId(null);
        selectedCompleted.setScheduledTraining(selectedScheduled);
        selectedCompleted.setTrainingCategory(TrainingCategory.Completed_Training);

        selectedScheduled.setCompleted(Boolean.TRUE);
        selectedScheduled.setCompletedTraining(selectedCompleted);

        getFacade().create(selectedCompleted);
        getFacade().edit(selectedScheduled);

        fillAbsentPersons();
        fillPresentPersons();
        fillFeedabcks();

        return "/training/Edit_Training_Completed";

    }

    public String saveCompletedTraining() {
        if (selectedCompleted == null) {
            JsfUtil.addErrorMessage("Please select");
            return "";
        }
        getFacade().edit(selectedCompleted);
        return "/training/Search";
    }

    public Training getSelectedScheduled() {
        return selectedScheduled;
    }

    public void setSelectedScheduled(Training selectedScheduled) {
        this.selectedScheduled = selectedScheduled;
    }

    public Training getSelectedCompleted() {
        return selectedCompleted;
    }

    public void setSelectedCompleted(Training selectedCompleted) {
        this.selectedCompleted = selectedCompleted;
    }

    public void makeAllTrainingsSchedulesSchedules() {
        List<Training> ts = getFacade().findAll();
        int i = 0;
        for (Training t : ts) {
            t.setTrainingCategory(TrainingCategory.Scheduled_Training);
            t.setCompleted(Boolean.FALSE);
            getFacade().edit(t);
            i++;
            System.out.println("t = " + t);
        }
    }

    public String searchSchedules() {
        selectedItems = searchSchedules(from, to);
        return "";
    }

    public List<Training> searchSchedules(Date fromDate, Date toDate) {
        String j = "Select t from Training t "
                + " where t.startDate between :fd and :td "
                + " and t.trainingCategory= :tc ";
        Map m = new HashMap();
        m.put("fd", fromDate);
        m.put("td", toDate);
        m.put("tc", TrainingCategory.Scheduled_Training);
        if (department != null) {
            j += " and t.department=:dept";
            m.put("dept", department);
        }
        j += " order by t.startDate";
        return getFacade().findBySQL(j, m);
    }

    public String searchAndPrintSchedules() {
        int monthsInBetween = JsfUtil.monthsInBetweenTwoDays(from, to);
        System.out.println("monthsInBetween = " + monthsInBetween);
        selectedMonthlyTrainings = new ArrayList<MonthlyTrainings>();
        for (int i = 0; i < (monthsInBetween + 1); i++) {
            MonthlyTrainings mt = new MonthlyTrainings();
            Calendar fc = Calendar.getInstance();
            fc.setTime(from);
            fc.add(Calendar.MONTH, i);

            Date fromDate = JsfUtil.firstDayOfMonth(fc.getTime());
            System.out.println("fromDate = " + fromDate);
            Date toDate = JsfUtil.lastDayOfMonth(fc.getTime());
            System.out.println("toDate = " + toDate);
            mt.setTrainings(searchSchedules(fromDate, toDate));
            System.out.println("fc = " + fc);
            System.out.println("mt = " + mt);

            fc.setTime(fromDate);
            fc.add(Calendar.DATE, 5);

            System.out.println("fromDate = " + fromDate);

            mt.setMonthDate(fromDate);
            selectedMonthlyTrainings.add(mt);
        }
        return "/training/print_schedules";
    }

    public long calculateTrainingCount(Date fromDate, Date toDate, boolean completed) {
        String j;
        Map m;
        if (completed) {
            j = "Select count(t) from Training t "
                    + " where t.startDate between :fd and :td "
                    + " and t.trainingCategory= :tc "
                    + " and t.completed=:c";
            m = new HashMap();
            m.put("fd", fromDate);
            m.put("td", toDate);
            m.put("tc", TrainingCategory.Scheduled_Training);
            m.put("c", completed);
        } else {
            j = "Select count(t) from Training t "
                    + " where t.startDate between :fd and :td "
                    + " and t.trainingCategory= :tc "
                    + " and (t.completed=:c or t.completed is null)";
            m = new HashMap();
            m.put("fd", fromDate);
            m.put("td", toDate);
            m.put("tc", TrainingCategory.Scheduled_Training);
            m.put("c", completed);

        }
        if (department != null) {
            j += " and t.department=:dept";
            m.put("dept", department);
        }
        return getFacade().findLongByJpql(j, m, TemporalType.DATE);

    }

    public String createCompleteBarChart() {
        barModel = new BarChartModel();
        int monthsInBetween = JsfUtil.monthsInBetweenTwoDays(from, to);

        ChartSeries completed = new ChartSeries();
        completed.setLabel("Completed");
        for (int i = 0; i < (monthsInBetween + 1); i++) {
            MonthlyTrainings mt = new MonthlyTrainings();
            Calendar fc = Calendar.getInstance();
            fc.setTime(from);
            fc.add(Calendar.MONTH, i);

            Date fromDate = JsfUtil.firstDayOfMonth(fc.getTime());
            Date toDate = JsfUtil.lastDayOfMonth(fc.getTime());

            Long count = calculateTrainingCount(fromDate, toDate, true);
            System.out.println("count = " + count);

            fc.setTime(fromDate);
            fc.add(Calendar.DATE, 5);
            mt.setMonthDate(fromDate);

            DateFormat df = new SimpleDateFormat("MMMM");
            String reportDate = df.format(fc.getTime());
            System.out.println("reportDate = " + reportDate);

            completed.set(reportDate, count);

        }

        ChartSeries notcompleted = new ChartSeries();
        notcompleted.setLabel("Not Completed");
        for (int i = 0; i < (monthsInBetween + 1); i++) {
            MonthlyTrainings mt = new MonthlyTrainings();
            Calendar fc = Calendar.getInstance();
            fc.setTime(from);
            fc.add(Calendar.MONTH, i);

            Date fromDate = JsfUtil.firstDayOfMonth(fc.getTime());
            Date toDate = JsfUtil.lastDayOfMonth(fc.getTime());

            Long count = calculateTrainingCount(fromDate, toDate, false);
            System.out.println("count = " + count);

            fc.setTime(fromDate);
            fc.add(Calendar.DATE, 5);
            mt.setMonthDate(fromDate);

            DateFormat df = new SimpleDateFormat("MMMM");
            String reportDate = df.format(fc.getTime());
            System.out.println("reportDate = " + reportDate);

            notcompleted.set(reportDate, count);

        }

        barModel.addSeries(completed);
        barModel.addSeries(notcompleted);
        barModel.setShowPointLabels(true);
        barModel.setShowPointLabels(true);
        return "";
    }

    public String createCompletePieChart() {
        pieModel1 = new PieChartModel();
        String j;

        completeCount = calculateTrainingCount(from, to, true);
        incompleteCount = calculateTrainingCount(from, to, false);

        pieModel1.set("Completed Trainings", completeCount);
        pieModel1.set("Incomplete Trainings", incompleteCount);

        pieModel1.setTitle("Training Schedules");
        pieModel1.setLegendPosition("w");
        pieModel1.setShowDataLabels(true);
        return "";
    }

    public String searchAndPrintSchedulesIndex() {
        int monthsInBetween = JsfUtil.monthsInBetweenTwoDays(from, to);
        System.out.println("monthsInBetween = " + monthsInBetween);
        selectedMonthlyTrainings = new ArrayList<MonthlyTrainings>();
        for (int i = 0; i < (monthsInBetween + 1); i++) {
            MonthlyTrainings mt = new MonthlyTrainings();
            Calendar fc = Calendar.getInstance();
            fc.setTime(from);
            fc.add(Calendar.MONTH, i);

            Date fromDate = JsfUtil.firstDayOfMonth(fc.getTime());
            System.out.println("fromDate = " + fromDate);
            Date toDate = JsfUtil.lastDayOfMonth(fc.getTime());
            System.out.println("toDate = " + toDate);
            mt.setTrainings(searchSchedules(fromDate, toDate));
            System.out.println("fc = " + fc);
            System.out.println("mt = " + mt);

            fc.setTime(fromDate);
            fc.add(Calendar.DATE, 5);

            System.out.println("fromDate = " + fromDate);

            mt.setMonthDate(fromDate);
            selectedMonthlyTrainings.add(mt);
        }
        return "/training/print_schedules_index";
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Date getFrom() {
        if (from == null) {
            from = JsfUtil.firstDayOfYear(new Date());
        }
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public Date getTo() {
        if (to == null) {
            to = JsfUtil.lastDayOfYear(new Date());
        }
        return to;
    }

    public void setTo(Date to) {
        this.to = to;
    }

    public List<MonthlyTrainings> getSelectedMonthlyTrainings() {
        return selectedMonthlyTrainings;
    }

    public void setSelectedMonthlyTrainings(List<MonthlyTrainings> selectedMonthlyTrainings) {
        this.selectedMonthlyTrainings = selectedMonthlyTrainings;
    }

    public List<Training> getSelectedItems() {
        return selectedItems;
    }

    public void setSelectedItems(List<Training> selectedItems) {
        this.selectedItems = selectedItems;
    }

    public TrainingController() {
    }

    public Training getSelected() {
        return selected;
    }

    public void setSelected(Training selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private TrainingFacade getFacade() {
        return ejbFacade;
    }

    public Training prepareCreate() {
        selected = new Training();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("TrainingCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void createSchedule() {
        selected.setTrainingCategory(TrainingCategory.Scheduled_Training);
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("TrainingCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("TrainingUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("TrainingDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Training> getItems() {
        if (items == null) {
            String j = "select t from Training t order by t.startDate";
            items = getFacade().findBySQL(j);
        }
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    getFacade().edit(selected);
                } else {
                    getFacade().remove(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }

    public Training getTraining(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<Training> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Training> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    public PieChartModel getPieModel1() {
        return pieModel1;
    }

    public void setPieModel1(PieChartModel pieModel1) {
        this.pieModel1 = pieModel1;
    }

    public Long getCompleteCount() {
        return completeCount;
    }

    public void setCompleteCount(Long completeCount) {
        this.completeCount = completeCount;
    }

    public Long getIncompleteCount() {
        return incompleteCount;
    }

    public void setIncompleteCount(Long incompleteCount) {
        this.incompleteCount = incompleteCount;
    }

    public BarChartModel getBarModel() {
        return barModel;
    }

    public void setBarModel(BarChartModel barModel) {
        this.barModel = barModel;
    }

    @FacesConverter(forClass = Training.class)
    public static class TrainingControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            TrainingController controller = (TrainingController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "trainingController");
            return controller.getTraining(getKey(value));
        }

        java.lang.Long getKey(String value) {
            java.lang.Long key;
            key = Long.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Long value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Training) {
                Training o = (Training) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Training.class.getName()});
                return null;
            }
        }

    }

}
