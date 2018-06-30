package com.phds.phd.dao;

import com.phds.phd.model.Professional;

import java.util.List;

public interface ProfessionalsDAO {
    List<Professional> getProfessionals();

    Professional findById(int id);

    void insertProfessional(Professional professional);
}
