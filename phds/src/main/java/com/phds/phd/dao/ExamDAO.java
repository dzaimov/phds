package com.phds.phd.dao;

import com.phds.phd.model.Exam;

import java.util.List;

public interface ExamDAO {

    Exam findById(int id);

    List<Exam> getExams();

    void insertExam(Exam exam);
}
