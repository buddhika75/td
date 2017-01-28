package lk.gov.sp.healthdept.td.controllers;

import lk.gov.sp.healthdept.td.entity.TrainingItem;
import lk.gov.sp.healthdept.td.controllers.util.JsfUtil;
import lk.gov.sp.healthdept.td.controllers.util.JsfUtil.PersistAction;
import lk.gov.sp.healthdept.td.facades.TrainingItemFacade;

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
import lk.gov.sp.healthdept.td.entity.Training;
import lk.gov.sp.healthdept.td.entity.TrainingItemCategory;

@Named("trainingItemController")
@SessionScoped
public class TrainingItemController implements Serializable {

    @EJB
    private lk.gov.sp.healthdept.td.facades.TrainingItemFacade ejbFacade;
    private List<TrainingItem> items = null;
    private TrainingItem selected;
    Training selectedTraining;
    Item selectedItem;
    List<TrainingItem> selectedTrainingItems;
    TrainingItem selectedTrainingItem;

    
    public String toAddTrainingKeywords(){
        if(selectedTraining==null){
            JsfUtil.addErrorMessage("Select a training");
            return "";
        }
        fillSelectedTrainingItems();
        return "/personItem/person_training_requirements";
    }
    
    public void fillSelectedTrainingItems() {
        selectedTrainingItems = new ArrayList<TrainingItem>();
        String j = "Select ti "
                + " from TrainingItem ti "
                + " where ti.training=:t "
                + " and ti.category=:c";
        Map m = new HashMap();
        m.put("t", selectedTraining);
        m.put("c", TrainingItemCategory.Training_Keyword);
        selectedTrainingItems = getFacade().findBySQL(j, m);
    }

    public void addSelectedTrainingItem() {
        if (selectedTraining == null) {
            return;
        }
        if (selectedItem == null) {
            JsfUtil.addErrorMessage("Keyword ?");
            return;
        }
        TrainingItem ti = new TrainingItem();
        ti.setTraining(selectedTraining);
        ti.setCategory(TrainingItemCategory.Training_Keyword);
        ti.setItem(selectedItem);
        getFacade().create(ti);
        fillSelectedTrainingItems();
        selectedItem = null;

    }

    public void removeSelectedTrainingItem() {
        if (selectedTrainingItem==null) {
            JsfUtil.addErrorMessage("Select");
            return;
        }
        getFacade().remove(selectedTrainingItem);
        fillSelectedTrainingItems();
    }

    public Training getSelectedTraining() {
        return selectedTraining;
    }

    public void setSelectedTraining(Training selectedTraining) {
        this.selectedTraining = selectedTraining;
    }

    public Item getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(Item selectedItem) {
        this.selectedItem = selectedItem;
    }

    public List<TrainingItem> getSelectedTrainingItems() {
        return selectedTrainingItems;
    }

    public void setSelectedTrainingItems(List<TrainingItem> selectedTrainingItems) {
        this.selectedTrainingItems = selectedTrainingItems;
    }

    public TrainingItem getSelectedTrainingItem() {
        return selectedTrainingItem;
    }

    public void setSelectedTrainingItem(TrainingItem selectedTrainingItem) {
        this.selectedTrainingItem = selectedTrainingItem;
    }

    
    
    public TrainingItemController() {
    }

    public TrainingItem getSelected() {
        return selected;
    }

    public void setSelected(TrainingItem selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private TrainingItemFacade getFacade() {
        return ejbFacade;
    }

    public TrainingItem prepareCreate() {
        selected = new TrainingItem();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle2").getString("TrainingItemCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle2").getString("TrainingItemUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle2").getString("TrainingItemDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<TrainingItem> getItems() {
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
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle2").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle2").getString("PersistenceErrorOccured"));
            }
        }
    }

    public TrainingItem getTrainingItem(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<TrainingItem> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<TrainingItem> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = TrainingItem.class)
    public static class TrainingItemControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            TrainingItemController controller = (TrainingItemController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "trainingItemController");
            return controller.getTrainingItem(getKey(value));
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
            if (object instanceof TrainingItem) {
                TrainingItem o = (TrainingItem) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), TrainingItem.class.getName()});
                return null;
            }
        }

    }

}
