package com.weather.weatherviewer.dao;

import com.weather.weatherviewer.entity.Location;
import lombok.RequiredArgsConstructor;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import java.util.List;
@RequiredArgsConstructor
@Repository
public class LocationDao {
    private final SessionFactory sessionFactory;

    public void save(Location location) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.persist(location);
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException(e);
        }
    }

    public void deleteByIdAndUserId(int locationId, long userId) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.createMutationQuery("""
                            DELETE FROM Location 
                            WHERE id = :locationId 
                            AND userId = :userId
                            """)
                    .setParameter("locationId", locationId)
                    .setParameter("userId", userId)
                    .executeUpdate();
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException(e);
        }
    }

    public List<Location> findAllByUserId(long userId) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Location WHERE userId = :userId", Location.class)
                    .setParameter("userId", userId)
                    .getResultList();
        }

    }
    public  Location findLocationById(int locationId){
        try(Session session = sessionFactory.openSession()){
            return session.createQuery("FROM Location WHERE id = :locationId",Location.class)
                    .setParameter("locationId",locationId)
                    .uniqueResult();
        }
    }

}
