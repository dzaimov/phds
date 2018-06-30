package com.phds.phd.dao;

import com.phds.phd.model.Conspect;

import java.util.List;

public interface ConspectDAO {
    List<Conspect> getConspects();

    void insertConspect(Conspect conspect);
}
