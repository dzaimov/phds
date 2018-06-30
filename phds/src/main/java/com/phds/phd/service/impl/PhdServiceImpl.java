package com.phds.phd.service.impl;

import com.phds.phd.dao.PhdDAO;
import com.phds.phd.dao.PhdsExamsDAO;
import com.phds.phd.dao.PhdsSupervisorsDAO;
import com.phds.phd.model.Phd;
import com.phds.phd.model.PhdsExams;
import com.phds.phd.model.PhdsSupervisors;
import com.phds.phd.service.PhdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PhdServiceImpl implements PhdService {

    private PhdDAO phdDAO;
    private PhdsSupervisorsDAO phdsSupervisorsDAO;
    private PhdsExamsDAO phdsExamsDAO;


    @Autowired
    public PhdServiceImpl(PhdDAO phdDAO, PhdsSupervisorsDAO phdsSupervisorsDAO, PhdsExamsDAO phdsExamsDAO) {
        this.phdDAO = phdDAO;
        this.phdsSupervisorsDAO  = phdsSupervisorsDAO;
        this.phdsExamsDAO = phdsExamsDAO;
    }

    @Override
    public List<Phd> getPhds() {
        return phdDAO.getPhds();
    }

    @Override
    public Phd findById(int id) {
        return phdDAO.findById(id);
    }

    @Override
    public void removePhd(Phd phd) {
        phdDAO.removePhd(phd);
    }

    @Override
    public int insertPhd(Phd phd) {
        return phdDAO.insertPhd(phd);
    }

    @Override
    public void updatePhd(Phd phd) {
        phdDAO.updatePhd(phd);
    }

    @Override
    public List<PhdsSupervisors> findByPhdId(int id) {
        return phdsSupervisorsDAO.findByPhdId(id);
    }

    @Override
    public List<PhdsSupervisors> getPhdsSupervisors() {
        return phdsSupervisorsDAO.getPhdsSupervisors();
    }

    @Override
    public void insertPhdsSupervisor(PhdsSupervisors phdsSupervisors) {
        phdsSupervisorsDAO.insertPhdsSupervisor(phdsSupervisors);
    }

    @Override
    public void removePhdSupervisor(PhdsSupervisors phdsSupervisors) {
        phdsSupervisorsDAO.removePhdSupervisor(phdsSupervisors);
    }

    @Override
    public List<PhdsExams> findExamByPhdId(int id) {
        return phdsExamsDAO.findByPhdId(id);
    }

    @Override
    public List<PhdsExams> getPhdsExams() {
        return phdsExamsDAO.getPhdsExams();
    }

    @Override
    public void insertPhdsExam(PhdsExams phdsExams) {
        phdsExamsDAO.insertPhdsExam(phdsExams);
    }

    @Override
    public void removePhdExam(PhdsExams phdsExams) {
        phdsExamsDAO.removePhdExam(phdsExams);
    }

}
