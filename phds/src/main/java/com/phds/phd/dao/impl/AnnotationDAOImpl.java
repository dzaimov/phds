package com.phds.phd.dao.impl;

import com.phds.phd.dao.AnnotationDAO;
import com.phds.phd.model.Annotation;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class AnnotationDAOImpl implements AnnotationDAO {

    private SessionFactory sessionFactory;

    @Autowired
    public AnnotationDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Annotation> getAnnotations() {
        Session session = sessionFactory.getCurrentSession();
        List query = session.createQuery("from annotations a")
                .list();
        return (List<Annotation>) query;
    }

    @Override
    public Annotation findById(int id) {
        Session session = sessionFactory.getCurrentSession();
        List query = session.createQuery("from Annotation a where a.id = :id")
                .setParameter("id", id)
                .list();
        return (Annotation) query.get(0);
    }

    @Override
    public int insert(Annotation annotation) {
        Session session = sessionFactory.getCurrentSession();
        session.save(annotation);
        return annotation.getId();
    }

    @Override
    public void updateAnnotation(Annotation annotation) {
        Session session = sessionFactory.getCurrentSession();
        session.update(annotation);
    }
}
