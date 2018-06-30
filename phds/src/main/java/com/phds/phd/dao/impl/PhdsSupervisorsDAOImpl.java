package com.phds.phd.dao.impl;

import com.phds.phd.dao.PhdsSupervisorsDAO;
import com.phds.phd.model.Exam;
import com.phds.phd.model.PhdsSupervisors;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class PhdsSupervisorsDAOImpl implements PhdsSupervisorsDAO {
    private SessionFactory sessionFactory;

    @Autowired
    public PhdsSupervisorsDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<PhdsSupervisors> findByPhdId(int id) {
        Session session = sessionFactory.getCurrentSession();
        List<PhdsSupervisors> query = (List<PhdsSupervisors>) session.createQuery("from PhdsSupervisors e where e.phdId = :id")
                .setParameter("id", id)
                .list();
        return query;
    }

    @Override
    public List<PhdsSupervisors> getPhdsSupervisors() {
        Session session = sessionFactory.getCurrentSession();
        List<PhdsSupervisors> query = (List<PhdsSupervisors>)session.createQuery("from PhdsSupervisors e")
                .list();
        return query;
    }

    @Override
    public void insertPhdsSupervisor(PhdsSupervisors phdsSupervisors) {
        Session session = sessionFactory.getCurrentSession();
        session.save(phdsSupervisors);

    }

    @Override
    public void removePhdSupervisor(PhdsSupervisors phdsSupervisors) {
        Session session = sessionFactory.getCurrentSession();
        session.delete(phdsSupervisors);
    }
}
