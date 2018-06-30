package com.phds.phd.service.impl;

import com.phds.phd.dao.AttestationDAO;
import com.phds.phd.model.Attestation;
import com.phds.phd.service.AttestationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AttestationServiceImpl implements AttestationService {

    private AttestationDAO attestationDAO;

    @Autowired
    public AttestationServiceImpl (AttestationDAO attestationDAO) {
        this.attestationDAO = attestationDAO;
    }

    @Override
    public List<Attestation> getAttestation() {
        return attestationDAO.getAttestation();
    }

    @Override
    public Attestation findById(int id) {
        return attestationDAO.findById(id);
    }

    @Override
    public void removeAttestation(Attestation attestation) {
attestationDAO.removeAttestation(attestation);
    }

    @Override
    public int insertAttestation(Attestation attestation) {
        return attestationDAO.insertAttestation(attestation);
    }
}
