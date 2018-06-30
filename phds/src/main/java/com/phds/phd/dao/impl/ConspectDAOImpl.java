package com.phds.phd.dao.impl;

import com.phds.phd.dao.ConspectDAO;
import com.phds.phd.model.Conspect;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class ConspectDAOImpl implements ConspectDAO {

    private SessionFactory sessionFactory;

    @Autowired
    public ConspectDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Conspect> getConspects() {
        Session session = sessionFactory.getCurrentSession();
        List<Conspect> query = (List<Conspect>)session.createQuery("from Conspect c")
                .list();
        return query;    }

    @Override
    public void insertConspect(Conspect conspect) {
        Session session = sessionFactory.getCurrentSession();
        session.save(conspect);
    }
}
