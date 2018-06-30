package com.phds.phd.service;

import com.phds.phd.model.Phd;
import com.phds.phd.model.PhdsExams;
import com.phds.phd.model.PhdsSupervisors;

import java.util.List;

public interface PhdService {

    List<Phd> getPhds();

    Phd findById(int id);

    void removePhd(Phd phd);

    int insertPhd(Phd phd);

    void updatePhd(Phd phd);

    List<PhdsSupervisors> findByPhdId(int id);

    List<PhdsSupervisors> getPhdsSupervisors();

    void insertPhdsSupervisor(PhdsSupervisors phdsSupervisors);

    void removePhdSupervisor(PhdsSupervisors phdsSupervisors);

    List<PhdsExams> findExamByPhdId(int id);

    List<PhdsExams> getPhdsExams();

    void insertPhdsExam(PhdsExams phdsExams);

    void removePhdExam(PhdsExams phdsExams);

}
