package com.phds.phd.dao;

import com.phds.phd.model.PhdsSupervisors;

import java.util.List;

public interface PhdsSupervisorsDAO {
    List<PhdsSupervisors> findByPhdId(int id);

    List<PhdsSupervisors> getPhdsSupervisors();

    void insertPhdsSupervisor(PhdsSupervisors phdsSupervisors);

    void removePhdSupervisor(PhdsSupervisors phdsSupervisors);
}
