package com.phds.phd.service.impl;

import com.phds.phd.dao.ConspectDAO;
import com.phds.phd.model.Conspect;
import com.phds.phd.service.ConspectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConspectServiceImpl implements ConspectService {

    private ConspectDAO conspectDAO;

    @Autowired
    public ConspectServiceImpl (ConspectDAO conspectDAO) {
        this.conspectDAO = conspectDAO;
    }

    @Override
    public List<Conspect> getConspects() {
        return conspectDAO.getConspects();
    }

    @Override
    public void insertConspect(Conspect conspect) {
        conspectDAO.insertConspect(conspect);
    }
}
