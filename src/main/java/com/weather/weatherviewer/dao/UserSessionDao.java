package com.weather.weatherviewer.dao;

import com.weather.weatherviewer.entity.UserSession;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@RequiredArgsConstructor
@Repository
public class UserSessionDao {
    private final SessionFactory sessionFactory;

    public void save(UserSession userSession) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.persist(userSession);
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException(e);
        }
    }

    public UserSession findBySessionId(UUID sessionId) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(UserSession.class, sessionId);
        }
    }

    public void deleteBySessionId(UUID sessionId) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            UserSession userSession = session.get(UserSession.class, sessionId);
            if (userSession == null) {
                transaction.commit();
                return;
            }
            session.remove(userSession);
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException(e);
        }
    }

    public List<UserSession> findAllByUserId(long userId) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM UserSession where userId = :userId", UserSession.class)
                    .setParameter("userId", userId)
                    .list();
        }
    }


}
