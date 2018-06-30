package com.phds.phd.dao.impl;

import com.phds.phd.dao.AttestationDAO;
import com.phds.phd.model.Attestation;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class AttestationDAOImpl implements AttestationDAO {

    private SessionFactory sessionFactory;

    @Autowired
    public AttestationDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public List<Attestation> getAttestation() {
        Session session = sessionFactory.getCurrentSession();
        List query = session.createQuery("from Attestation p")
                .list();
        return (List<Attestation>) query;
    }

    @Override
    public Attestation findById(int id) {
        Session session = sessionFactory.getCurrentSession();
        List query = session.createQuery("from Attestation p where p.id = :id")
                .setParameter("id", id)
                .list();
        return (Attestation) query.get(0);
    }

    @Override
    public void removeAttestation(Attestation attestation) {
        Session session = sessionFactory.getCurrentSession();
        session.delete(attestation);
    }

    @Override
    public int insertAttestation(Attestation attestation) {
        Session session = sessionFactory.getCurrentSession();
        session.save(attestation);
        return attestation.getId();
    }
}
