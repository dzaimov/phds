package com.phds.phd.dao;

import com.phds.phd.model.Phd;
import com.phds.phd.model.PhdsExams;

import java.util.List;

public interface PhdsExamsDAO {
    List<PhdsExams> findByPhdId(int id);

    List<PhdsExams> getPhdsExams();

    void insertPhdsExam(PhdsExams phdsExams);

    void removePhdExam(PhdsExams phdsExams);
}
