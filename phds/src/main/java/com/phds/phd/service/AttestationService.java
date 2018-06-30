package com.phds.phd.service;

import com.phds.phd.model.Attestation;

import java.util.List;

public interface AttestationService {
    List<Attestation> getAttestation();

    Attestation findById(int id);

    void removeAttestation(Attestation attestation);

    int insertAttestation(Attestation attestation);
}
