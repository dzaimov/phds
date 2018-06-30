package com.phds.phd.dao.impl;

import com.phds.phd.dao.ExamDAO;
import com.phds.phd.model.Exam;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class ExamDAOImpl implements ExamDAO {

    private SessionFactory sessionFactory;

    @Autowired
    public ExamDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Exam findById(int id) {
        Session session = sessionFactory.getCurrentSession();
        List query = session.createQuery("from Exam e where e.id = :id")
                .setParameter("id", id)
                .list();
        return (Exam) query.get(0);
    }

    @Override
    public List<Exam> getExams() {
        Session session = sessionFactory.getCurrentSession();
        List<Exam> query = (List<Exam>)session.createQuery("from Exam e")
                .list();
        return query;
    }

    @Override
    public void insertExam(Exam exam) {
        Session session = sessionFactory.getCurrentSession();
        session.save(exam);
    }
}
