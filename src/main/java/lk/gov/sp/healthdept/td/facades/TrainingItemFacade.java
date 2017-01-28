/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.gov.sp.healthdept.td.facades;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lk.gov.sp.healthdept.td.entity.TrainingItem;

/**
 *
 * @author pdhs-sp
 */
@Stateless
public class TrainingItemFacade extends AbstractFacade<TrainingItem> {
    @PersistenceContext(unitName = "pu")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TrainingItemFacade() {
        super(TrainingItem.class);
    }
    
}
