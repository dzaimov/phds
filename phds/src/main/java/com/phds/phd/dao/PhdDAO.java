package com.phds.phd.dao;

import com.phds.phd.model.Phd;

import java.util.List;

public interface PhdDAO {

    List<Phd> getPhds();

    Phd findById(int id);

    void removePhd(Phd phd);

    int insertPhd(Phd phd);

    void updatePhd(Phd phd);
}
