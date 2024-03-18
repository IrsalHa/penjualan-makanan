package com.vym.tugas.dao;

import com.vym.utils.db.StoreHibernateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import java.util.List;

public interface Dao<T> {
    Logger _logger = LogManager.getLogger(Dao.class);

    Class<T> getEntityClass();

    default List<T> getAll() {
        try (Session session = StoreHibernateUtil.getSessionFactory().openSession()) {
            List<T> result = null;
            try {
                session.beginTransaction();
                result = getAll(session);
                session.getTransaction().commit();
            } catch (Exception e) {
                _logger.error(e.getMessage(), e);
                session.getTransaction().rollback();
            }
            return result;
        }
    }

    default List<T> getAll(Session session) {
        return wheres(session, "0=0");
    }

    default List<T> wheresRaw(Session session, String filter) {
        Boolean isSingle = false;
        if (filter.contains("LIMIT 1")) {
            isSingle = true;
        }
        String sql = "SELECT * FROM " + getEntityClass().getSimpleName().toUpperCase() + " WHERE " + filter;

        if (isSingle) {
            return session.createNativeQuery(sql, getEntityClass()).list();
        }
        return session.createNativeQuery(sql, getEntityClass()).list();
    }

    default List<T> wheresRaw(String filter) {
        try (Session session = StoreHibernateUtil.getSessionFactory().openSession()) {
            List<T> result = null;
            try {
                session.beginTransaction();
                result = wheresRaw(session, filter);
                session.getTransaction().commit();
            } catch (Exception e) {
                _logger.error(e.getMessage(), e);
                session.getTransaction().rollback();
            }
            return result;
        }
    }

    default T whereRaw(String filter) {
        List<T> resultList = wheresRaw(filter + " LIMIT 1");
        return resultList.isEmpty() ? null : resultList.get(0);
    }

    default T whereRaw(Session session, String filter) {
        List<T> resultList = wheresRaw(session, filter + " LIMIT 1");
        return resultList.isEmpty() ? null : resultList.get(0);
    }

    default List<T> wheres(String filter) {
        List<T> result = null;
        try (Session session = StoreHibernateUtil.getSessionFactory().openSession()) {
            try {
                //session.beginTransaction();
                result = wheres(session, filter);
                //session.getTransaction().commit();
            } catch (Exception e) {
                _logger.error(e.getMessage(), e);
                //session.getTransaction().rollback();
            }
        }
        return result;
    }

    default List<T> wheres(Session session, String filter) {
        Boolean isSingle = false;
        if (filter.contains("LIMIT 1")) {
            isSingle = true;
            filter = filter.replace("LIMIT 1", "");
        }
        String sql = "FROM " + getEntityClass().getSimpleName() + " WHERE " + filter;
        if (isSingle) {
            return (List<T>) session.createQuery(sql).setMaxResults(1).list();
        }
        return (List<T>) session.createQuery(sql).list();
    }

    default T where(String filter) {
        List<T> resultList = wheres(filter + " LIMIT 1");
        return resultList.isEmpty() ? null : resultList.get(0);
    }

    default T where(Session session, String filter) {
        List<T> resultList = wheres(session, filter + " LIMIT 1");
        return resultList.isEmpty() ? null : resultList.get(0);
    }

    default void delete(T entity) {
        try (Session session = StoreHibernateUtil.getSessionFactory().openSession()) {
            try {
                session.beginTransaction();
                delete(session, entity);
                session.getTransaction().commit();
            } catch (Exception e) {
                session.getTransaction().rollback();
                _logger.error(e.getMessage(), e);
            }
        }
    }

    default void delete(Session session, T entity) {
        session.delete(entity);
    }

    default void saveOrUpdate(T entity) {
        try (Session session = StoreHibernateUtil.getSessionFactory().openSession()) {
            try {
                session.beginTransaction();
                saveOrUpdate(session, entity);
                session.getTransaction().commit();
            } catch (Exception e) {
                session.getTransaction().rollback();
                _logger.error(e.getMessage(), e);
            }
        }
    }

    default void saveOrUpdate(Session session, T entity) {
        session.saveOrUpdate(entity);
    }

    default int pageCount(String filter) throws Exception {
        int count = 0;
        if (filter == null || "".equals(filter))
            filter = "0 = 0";
        Session session = StoreHibernateUtil.getSessionFactory().openSession();
        count = ((Long) session.createQuery("select count(*) from " + getEntityClass().getSimpleName() + " where " + filter).uniqueResult()).intValue();
        session.close();
        return count;
    }

    default List<T> listPaging(int offset, int limit, String filter, String orderby) {
        Session session = StoreHibernateUtil.getSessionFactory().openSession();
        if (filter == null || "".equals(filter))
            filter = "0 = 0";
        String sql = "from " + getEntityClass().getSimpleName() + " where " + filter + " order by " + orderby + " DESC";
        System.out.println(sql);
        List<T> tList = session.createQuery(sql).setFirstResult(offset).setMaxResults(limit).list();
        session.close();
        return tList;
    }
}
