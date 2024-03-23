/*
 * Last Edited By : Irsal Hakim Alamsyah
 * @irsalha
 * Last Edited : 18 - 3 - 2024
 */

package com.vym.tugas.dao;

import com.vym.tugas.domain.Muser;
import com.vym.utils.db.StoreHibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

public class MuserDao implements Dao<Muser> {
    @Override
    public Class<Muser> getEntityClass() {
        return Muser.class;
    }

    public Long totalUser() {
        Long totalUser = null;

        try (Session session = StoreHibernateUtil.getSessionFactory().openSession()) {
            Query<?> query = session.createQuery(
                    "select count(*) from Muser");

            totalUser = (Long) query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return totalUser;
    }
}
