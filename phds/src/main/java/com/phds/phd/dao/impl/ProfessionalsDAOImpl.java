package com.phds.phd.dao.impl;

import com.phds.phd.dao.ProfessionalsDAO;
import com.phds.phd.model.Conspect;
import com.phds.phd.model.Phd;
import com.phds.phd.model.Professional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class ProfessionalsDAOImpl implements ProfessionalsDAO {

    private SessionFactory sessionFactory;

    @Autowired
    public ProfessionalsDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public List<Professional> getProfessionals() {
        Session session = sessionFactory.getCurrentSession();
        List<Professional> query = (List<Professional>)session.createQuery("from Professional p")
                .list();
        return query;
    }

    @Override
    public Professional findById(int id) {
        Session session = sessionFactory.getCurrentSession();
        List query = session.createQuery("from Professional p where p.id = :id")
                .setParameter("id", id)
                .list();
        return (Professional) query.get(0);
    }

    @Override
    public void insertProfessional(Professional professional) {
        Session session = sessionFactory.getCurrentSession();
        session.save(professional);
    }
}
