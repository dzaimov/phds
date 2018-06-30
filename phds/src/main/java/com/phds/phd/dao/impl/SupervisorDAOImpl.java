package com.phds.phd.dao.impl;

import com.phds.phd.dao.SupervisorDAO;
import com.phds.phd.model.Professional;
import com.phds.phd.model.Supervisor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class SupervisorDAOImpl implements SupervisorDAO {

    private SessionFactory sessionFactory;

    @Autowired
    public SupervisorDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Supervisor> getSupervisors() {
        Session session = sessionFactory.getCurrentSession();
        List<Supervisor> query = (List<Supervisor>)session.createQuery("from Supervisor s")
                .list();
        return query;
    }

    @Override
    public Supervisor findById(int id) {
        Session session = sessionFactory.getCurrentSession();
        List query = session.createQuery("from Supervisor s where s.id = :id")
                .setParameter("id", id)
                .list();
        return (Supervisor) query.get(0);
    }

    @Override
    public void insertSupervisors(Supervisor supervisor) {
        Session session = sessionFactory.getCurrentSession();
        session.save(supervisor);
    }
}
