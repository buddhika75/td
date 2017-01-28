package lk.gov.sp.healthdept.td.controllers;

import lk.gov.sp.healthdept.td.entity.PersonItem;
import lk.gov.sp.healthdept.td.controllers.util.JsfUtil;
import lk.gov.sp.healthdept.td.controllers.util.JsfUtil.PersistAction;
import lk.gov.sp.healthdept.td.facades.PersonItemFacade;

import java.io.Serializable;
import java.util.ArrayList;
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
import lk.gov.sp.healthdept.td.entity.Item;
import lk.gov.sp.healthdept.td.entity.ItemCategory;
import lk.gov.sp.healthdept.td.entity.Person;
import lk.gov.sp.healthdept.td.entity.PersonItemCategory;

@Named("personItemController")
@SessionScoped
public class PersonItemController implements Serializable {

    @EJB
    private lk.gov.sp.healthdept.td.facades.PersonItemFacade ejbFacade;
    private List<PersonItem> items = null;
    private PersonItem selected;
    
    Person selectedPerson;
    List<PersonItem> selectedPersonTrainingRequirements;
    Item selectedTrainingRequirement;
    PersonItem selectedPersonTrainingRequirement;
    
    public void fillSelectedPersonTrainingRequirements(){
        selectedPersonTrainingRequirements = new ArrayList<PersonItem>();
        if(selectedPerson==null){
            return;
        }
        String j;
        j="select pi "
                + " from PersonItem pi "
                + " where pi.person=:p "
                + " and pi.category=:ic";
        Map m = new HashMap();
        m.put("p", selectedPerson);
        m.put("ic", PersonItemCategory.Training_Requirement_Self);
        selectedPersonTrainingRequirements = getFacade().findBySQL(j, m);
    }
    
    public void addPersonTrainingRequirement(){
        if(selectedPerson==null){
            JsfUtil.addErrorMessage("Person?");
            return;
        }
        if(selectedTrainingRequirement==null){
            JsfUtil.addErrorMessage("Requirement");
            return;
        }
        PersonItem pi = new PersonItem();
        pi.setItem(selectedTrainingRequirement);
        pi.setPerson(selectedPerson);
        pi.setCategory(PersonItemCategory.Training_Requirement_Self);
        getFacade().create(pi);
        fillSelectedPersonTrainingRequirements();
        selectedTrainingRequirement = null;
    }
    
    public void removePersonTrainingRequirement(){
        if(selectedPersonTrainingRequirement==null){
            JsfUtil.addErrorMessage("Select");
            return;
        }
        getFacade().remove(selectedPersonTrainingRequirement);
        fillSelectedPersonTrainingRequirements();
    }

    public Item getSelectedTrainingRequirement() {
        return selectedTrainingRequirement;
    }

    public void setSelectedTrainingRequirement(Item selectedTrainingRequirement) {
        this.selectedTrainingRequirement = selectedTrainingRequirement;
    }

    public PersonItem getSelectedPersonTrainingRequirement() {
        return selectedPersonTrainingRequirement;
    }

    public void setSelectedPersonTrainingRequirement(PersonItem selectedPersonTrainingRequirement) {
        this.selectedPersonTrainingRequirement = selectedPersonTrainingRequirement;
    }
    
    
    
    public Person getSelectedPerson() {
        return selectedPerson;
    }

    public void setSelectedPerson(Person selectedPerson) {
        this.selectedPerson = selectedPerson;
    }

    public List<PersonItem> getSelectedPersonTrainingRequirements() {
        return selectedPersonTrainingRequirements;
    }

    public void setSelectedPersonTrainingRequirements(List<PersonItem> selectedPersonTrainingRequirements) {
        this.selectedPersonTrainingRequirements = selectedPersonTrainingRequirements;
    }
    
    
    

    public PersonItemController() {
    }

    public PersonItem getSelected() {
        return selected;
    }

    public void setSelected(PersonItem selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private PersonItemFacade getFacade() {
        return ejbFacade;
    }

    public PersonItem prepareCreate() {
        selected = new PersonItem();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle1").getString("PersonItemCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle1").getString("PersonItemUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle1").getString("PersonItemDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<PersonItem> getItems() {
        if (items == null) {
            items = getFacade().findAll();
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
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle1").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle1").getString("PersistenceErrorOccured"));
            }
        }
    }

    public PersonItem getPersonItem(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<PersonItem> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<PersonItem> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = PersonItem.class)
    public static class PersonItemControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            PersonItemController controller = (PersonItemController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "personItemController");
            return controller.getPersonItem(getKey(value));
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
            if (object instanceof PersonItem) {
                PersonItem o = (PersonItem) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), PersonItem.class.getName()});
                return null;
            }
        }

    }

}
