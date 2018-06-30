package com.phds.phd.dao.impl;

import com.phds.phd.dao.PublicationDao;
import com.phds.phd.model.Publication;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class PublicationDAOImpl implements PublicationDao {
    private SessionFactory sessionFactory;

    @Autowired
    public PublicationDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Publication> getPublication() {
        Session session = sessionFactory.getCurrentSession();
        List query = session.createQuery("from Publication p")
                .list();
        return (List<Publication>) query;
    }

    @Override
    public Publication findById(int id) {
        Session session = sessionFactory.getCurrentSession();
        List query = session.createQuery("from Publication p where p.id = :id")
                .setParameter("id", id)
                .list();
        return (Publication) query.get(0);
    }

    @Override
    public void removePublication(Publication publication) {
        Session session = sessionFactory.getCurrentSession();
        session.delete(publication);
    }

    @Override
    public int insertPublication(Publication publication) {
        Session session = sessionFactory.getCurrentSession();
        session.save(publication);
        return publication.getId();
    }

    @Override
    public void updatePublication(Publication publication) {
        Session session = sessionFactory.getCurrentSession();
        session.update(publication);
    }
}
