package com.phds.phd.service;

import com.phds.phd.model.Phd;
import com.phds.phd.model.Professional;

import java.util.List;

public interface ProfessionalService {
    List<Professional> getProfessionals();

    Professional findById(int id);

    void insertProfessional(Professional professional);
}
