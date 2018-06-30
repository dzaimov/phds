package com.phds.phd.dao.impl;

import com.phds.phd.dao.PhdDAO;
import com.phds.phd.model.Phd;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class PhdDAOImpl implements PhdDAO {

    private SessionFactory sessionFactory;

    @Autowired
    public PhdDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Phd> getPhds() {
        Session session = sessionFactory.getCurrentSession();
        List query = session.createQuery("from Phd p")
                .list();
        return (List<Phd>) query;
    }

    @Override
    public Phd findById(int id) {
        Session session = sessionFactory.getCurrentSession();
        List query = session.createQuery("from Phd p where p.id = :id")
                .setParameter("id", id)
                .list();
        return (Phd) query.get(0);
    }

    @Override
    public void removePhd(Phd phd) {
        Session session = sessionFactory.getCurrentSession();
        session.delete(phd);
    }

    @Override
    public int insertPhd(Phd phd) {
        Session session = sessionFactory.getCurrentSession();
        session.save(phd);
        return phd.getId();
    }

    @Override
    public void updatePhd(Phd phd) {
        Session session = sessionFactory.getCurrentSession();
        session.update(phd);
    }
}
