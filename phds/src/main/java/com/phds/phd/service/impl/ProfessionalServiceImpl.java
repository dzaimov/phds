package com.phds.phd.service.impl;

import com.phds.phd.dao.ProfessionalsDAO;
import com.phds.phd.model.Professional;
import com.phds.phd.service.ProfessionalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfessionalServiceImpl implements ProfessionalService {

    private ProfessionalsDAO professionalsDAO;

    @Autowired
    public ProfessionalServiceImpl (ProfessionalsDAO professionalsDAO) {
        this.professionalsDAO = professionalsDAO;
    }


    @Override
    public List<Professional> getProfessionals() {
        return professionalsDAO.getProfessionals();
    }

    @Override
    public Professional findById(int id) {
        return professionalsDAO.findById(id);
    }

    @Override
    public void insertProfessional(Professional professional) {
        professionalsDAO.insertProfessional(professional);
    }
}
