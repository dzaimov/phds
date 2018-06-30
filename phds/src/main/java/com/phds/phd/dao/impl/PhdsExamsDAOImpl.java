package com.phds.phd.dao.impl;

import com.phds.phd.dao.PhdsExamsDAO;
import com.phds.phd.model.PhdsExams;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class PhdsExamsDAOImpl implements PhdsExamsDAO {

    private SessionFactory sessionFactory;

    @Autowired
    public PhdsExamsDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<PhdsExams> findByPhdId(int id) {
        Session session = sessionFactory.getCurrentSession();
        List<PhdsExams> query = (List<PhdsExams>) session.createQuery("from PhdsExams p where p.phdId = :id")
                .setParameter("id", id)
                .list();
        return query;
    }

    @Override
    public List<PhdsExams> getPhdsExams() {
        Session session = sessionFactory.getCurrentSession();
        List<PhdsExams> query = (List<PhdsExams>)session.createQuery("from PhdsExams p")
                .list();
        return query;
    }

    @Override
    public void insertPhdsExam(PhdsExams phdsExams) {
        Session session = sessionFactory.getCurrentSession();
        session.save(phdsExams);
    }

    @Override
    public void removePhdExam(PhdsExams phdsExams) {
        Session session = sessionFactory.getCurrentSession();
        session.delete(phdsExams);
    }
}
