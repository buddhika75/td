package lk.gov.sp.healthdept.td.controllers;

import lk.gov.sp.healthdept.td.controllers.util.JsfUtil;
import lk.gov.sp.healthdept.td.controllers.util.JsfUtil.PersistAction;

import java.io.Serializable;
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
import lk.gov.sp.healthdept.td.entity.Department;
import lk.gov.sp.healthdept.td.entity.MonthlyTrainings;
import lk.gov.sp.healthdept.td.entity.Training;
import lk.gov.sp.healthdept.td.entity.TrainingCategory;
import lk.gov.sp.healthdept.td.facades.TrainingFacade;

@Named("trainingController")
@SessionScoped
public class TrainingController implements Serializable {

    @EJB
    private lk.gov.sp.healthdept.td.facades.TrainingFacade ejbFacade;
    private List<Training> items = null;
    private Training selected;
    Department department;
    Date from;
    Date to;
    private List<Training> selectedItems = null;
    List<MonthlyTrainings> selectedMonthlyTrainings;

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
        for(int i = 0; i < monthsInBetween;i++){
            MonthlyTrainings mt = new MonthlyTrainings();
            Calendar fc = Calendar.getInstance();
            fc.setTime(from);
            fc.add(Calendar.MONTH, i);
            mt.setMonthDate(fc.getTime());
            mt.setTrainings(searchSchedules(JsfUtil.firstDayOfMonth(fc.getTime()),JsfUtil.lastDayOfMonth(fc.getTime())));
            System.out.println("fc = " + fc);
            System.out.println("mt = " + mt);
            selectedMonthlyTrainings.add(mt);
        }
        return "/training/print_schedules";
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
