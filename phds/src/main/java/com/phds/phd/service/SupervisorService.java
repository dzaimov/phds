package com.phds.phd.service;

import com.phds.phd.model.Supervisor;

import java.util.List;

public interface SupervisorService {

    List<Supervisor> getSupervisors();

    Supervisor findById(int id);

    void insertSupervisors(Supervisor supervisor);
}
