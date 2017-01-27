/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.gov.sp.healthdept.td.controllers;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import lk.gov.sp.healthdept.td.entity.ItemCategory;

/**
 *
 * @author pdhs-sp
 */
@Named(value = "enumController")
@SessionScoped
public class EnumController implements Serializable {

    /**
     * Creates a new instance of EnumController
     */
    public EnumController() {
    }
    
    public ItemCategory[] getItemCategorys(){
        return ItemCategory.values();
    }
    
}
