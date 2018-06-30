package com.phds.phd.service;

import com.phds.phd.model.Conspect;

import java.util.List;

public interface ConspectService {
    List<Conspect> getConspects();

    void insertConspect(Conspect conspect);
}
