package com.phds.phd.dao;

import com.phds.phd.model.Supervisor;

import java.util.List;

public interface SupervisorDAO {
    List<Supervisor> getSupervisors();

    Supervisor findById(int id);

    void insertSupervisors(Supervisor supervisor);
}
