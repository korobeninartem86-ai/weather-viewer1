package com.weather.weatherviewer.dao;

import com.weather.weatherviewer.entity.Location;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LocationDao {
    private final SessionFactory sessionFactory;

    public LocationDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void save(Location location){
        Session session = null;
        Transaction transaction = null;

        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.persist(location);
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null){
                transaction.rollback();
            }
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
    public void deleteByIdAndUserId(int locationId , long userId ){

        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
                session.createQuery("""
                        DELETE FROM Location 
                        WHERE id=:locationId 
                        AND userId =:userId""")
                        .setParameter("locationId",locationId)
                        .setParameter("userId",userId)
                        .executeUpdate();
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null){
                transaction.rollback();
            }
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public List<Location> findAllByUserId(long userId){
    List<Location>locations = null;
    Session session = null;
    Transaction transaction = null;

        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            locations = session.createQuery
                    ("FROM Location WHERE userId = : userId",Location.class)
                    .setParameter("userId",userId).getResultList();
        } catch (HibernateException e) {
            if (transaction != null){
                transaction.rollback();
            }
        } finally {
            if (session != null){
                session.close();
            }
        }
        return locations;
    }

}
