package com.phds.phd.service.impl;

import com.phds.phd.dao.SupervisorDAO;
import com.phds.phd.model.Supervisor;
import com.phds.phd.service.SupervisorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupervisorServiceImpl implements SupervisorService {

    private SupervisorDAO supervisorDAO;

    @Autowired
    public SupervisorServiceImpl (SupervisorDAO supervisorDAO) {
        this.supervisorDAO = supervisorDAO;
    }

    @Override
    public List<Supervisor> getSupervisors() {
        return supervisorDAO.getSupervisors();
    }

    @Override
    public Supervisor findById(int id) {
        return supervisorDAO.findById(id);
    }

    @Override
    public void insertSupervisors(Supervisor supervisor) {
supervisorDAO.insertSupervisors(supervisor);
    }
}
