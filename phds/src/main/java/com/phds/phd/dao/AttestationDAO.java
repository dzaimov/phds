package com.phds.phd.dao;

import com.phds.phd.model.Attestation;

import java.util.List;

public interface AttestationDAO {
    List<Attestation> getAttestation();

    Attestation findById(int id);

    void removeAttestation(Attestation attestation);

    int insertAttestation(Attestation attestation);

}
