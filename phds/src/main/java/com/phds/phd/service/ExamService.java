package com.phds.phd.service;

import com.phds.phd.model.Exam;

import java.util.List;

public interface ExamService {

    Exam findById(int id);

    List<Exam> getExams();

    void insertExam(Exam exam);
}
