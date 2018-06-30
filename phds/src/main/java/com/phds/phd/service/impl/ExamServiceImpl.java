package com.phds.phd.service.impl;

import com.phds.phd.dao.ConspectDAO;
import com.phds.phd.dao.ExamDAO;
import com.phds.phd.model.Exam;
import com.phds.phd.service.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExamServiceImpl implements ExamService {

    private ExamDAO examDAO;

    @Autowired
    public ExamServiceImpl (ExamDAO examDAO) {
        this.examDAO = examDAO;
    }


    @Override
    public Exam findById(int id) {
        return examDAO.findById(id);
    }

    @Override
    public List<Exam> getExams() {
        return examDAO.getExams();
    }

    @Override
    public void insertExam(Exam exam) {
        examDAO.insertExam(exam);
    }
}
