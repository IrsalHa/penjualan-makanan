/*
 * Last Edited By : Irsal Hakim Alamsyah
 * @irsalha
 * Last Edited : 24 - 4 - 2024
 */

package com.vym.tugas.dao;

import com.vym.tugas.domain.TcounterEngine;
import com.vym.utils.db.StoreHibernateUtil;
import org.apache.poi.ss.formula.functions.T;
import org.hibernate.Session;

import java.math.BigDecimal;

public class TcounterEngineDao implements Dao<TcounterEngine> {
    @Override
    public Class<TcounterEngine> getEntityClass() {
        return TcounterEngine.class;
    }

    public String getValue(String key){
        String value;

        try(Session session = StoreHibernateUtil.getSessionFactory().openSession()){
            try{
                session.beginTransaction();
                TcounterEngine tcounterEngine = (TcounterEngine) session.createQuery("from TcounterEngine where key=:key").setParameter("key", key).uniqueResultOptional().orElse(null);

                if(tcounterEngine != null){
                    tcounterEngine.setValue(new BigDecimal(tcounterEngine.getValue()).add(new BigDecimal(1)).toPlainString());
                    session.save(tcounterEngine);
                    value = tcounterEngine.getKey()+tcounterEngine.getValue();
                }else{
                    tcounterEngine = new TcounterEngine();
                    tcounterEngine.setKey(key);
                    tcounterEngine.setValue("1");
                    session.save(tcounterEngine);
                    value = tcounterEngine.getKey()+tcounterEngine.getValue();
                }

                session.getTransaction().commit();
                return value;
            }catch (Exception e){
                session.getTransaction().rollback();
                throw e;
            }
        }catch (Exception e){
            throw e;
        }
    }
}
